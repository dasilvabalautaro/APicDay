package com.globalhiddenodds.apicday.ui.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.util.LruCache
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.globalhiddenodds.apicday.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class TranslateViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    @ApplicationContext val context: Context): ViewModel() {

    companion object {
        private const val NUM_TRANSLATORS = 3
    }

    private val modelManager:
            RemoteModelManager = RemoteModelManager.getInstance()
    private val translators = object :
        LruCache<TranslatorOptions, Translator>(NUM_TRANSLATORS) {
        override fun create(key: TranslatorOptions?): Translator {
            return Translation.getClient(key!!)
        }

        override fun entryRemoved(
            evicted: Boolean,
            key: TranslatorOptions?,
            oldValue: Translator?,
            newValue: Translator?
        ) {
            oldValue!!.close()
        }
    }
    val sourceLang = MutableLiveData<Language>()
    val targetLang = MutableLiveData<Language>()
    val sourceText = MutableLiveData<String>()
    val translatedText = MediatorLiveData<ResultOrError>()
    val availableModels = MutableLiveData<List<String>>()

    val availableLanguages: List<Language> =
        TranslateLanguage.getAllLanguages()
            .map { Language(it) }

    init {
        val processTranslation =
            OnCompleteListener<String> {
                if (it.isSuccessful) {
                    translatedText.value = ResultOrError(it.result, null)
                } else {
                    translatedText.value = ResultOrError(null, it.exception)
                }
                fetchDownloadedModels()
            }
        translatedText.addSource(sourceText) {
            translate().addOnCompleteListener(processTranslation)
        }
        val languageObserver = Observer<Language> {
            translate().addOnCompleteListener(processTranslation)
        }
        translatedText.addSource(sourceLang, languageObserver)
        translatedText.addSource(targetLang, languageObserver)

        fetchDownloadedModels()
    }

    private fun getModel(languageCode: String): TranslateRemoteModel {
        return TranslateRemoteModel.Builder(languageCode).build()
    }

    private fun fetchDownloadedModels() {
        modelManager
            .getDownloadedModels(TranslateRemoteModel::class.java)
            .addOnSuccessListener { remoteModels ->
                availableModels.value =
                    remoteModels.sortedBy { it.language }
                        .map { it.language }
            }
    }

    internal fun downloadLanguage(language: Language) {
        val model = getModel(
            TranslateLanguage
                .fromLanguageTag(language.code)!!
        )
        modelManager.download(
            model,
            DownloadConditions.Builder().build()
        ).addOnCompleteListener {
            fetchDownloadedModels()
        }
    }

    internal fun deleteLanguage(language: Language) {
        val model = getModel(
            TranslateLanguage
                .fromLanguageTag(language.code)!!
        )
        modelManager.deleteDownloadedModel(model)
            .addOnCompleteListener { fetchDownloadedModels() }
    }

    private fun translate(): Task<String> {
        val text = sourceText.value
        val source = sourceLang.value
        val target = targetLang.value

        if (source == null || target == null ||
            text == null || text.isEmpty()
        ) {
            return Tasks.forResult("")
        }
        val sourceLangCode = TranslateLanguage.fromLanguageTag(source.code)!!
        val targetLangCode = TranslateLanguage.fromLanguageTag(target.code)!!
        val options =
            TranslatorOptions.Builder()
                .setSourceLanguage(sourceLangCode)
                .setTargetLanguage(targetLangCode)
                .build()
        return translators[options]
            .downloadModelIfNeeded().continueWithTask {
                if (it.isSuccessful) {
                    translators[options].translate(text)
                } else {
                    Tasks.forException<String>(
                        it.exception ?: Exception(
                            context
                                .getString(R.string.lbl_unknown_error)
                        )
                    )
                }
            }
    }

    inner class ResultOrError(var result: String?, var error: Exception?)

    class Language(val code: String) : Comparable<Language> {
        private val displayName: String
            get() = Locale(code).displayName

        override fun equals(other: Any?): Boolean {
            if (other === this) {
                return true
            }
            if (other !is Language) {
                return false
            }
            val otherLang = other as Language?
            return otherLang!!.code == code
        }

        override fun toString(): String {
            return "$code - $displayName"
        }

        override fun compareTo(other: Language): Int {
            return this.displayName.compareTo(other.displayName)
        }

        override fun hashCode(): Int {
            return code.hashCode()
        }
    }

    override fun onCleared() {
        super.onCleared()
        translators.evictAll()
    }
}