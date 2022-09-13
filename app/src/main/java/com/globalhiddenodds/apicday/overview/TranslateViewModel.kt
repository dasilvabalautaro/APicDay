package com.globalhiddenodds.apicday.overview

import android.util.LruCache
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.globalhiddenodds.apicday.network.DataCloud
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.*
import java.util.*

class TranslateViewModel : ViewModel() {
    companion object {
        private const val NUM_TRANSLATORS = 2
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
    private val sourceLang = MutableLiveData<Language>()
    private val targetLang = MutableLiveData<Language>()
    private val sourceText = MutableLiveData<String>()
    private val translatedText = MediatorLiveData<ResultOrError>()
    private val availableModels = MutableLiveData<List<String>>()

    ///////
    private var valueTranslate: String? = null
    private val translateState = mutableStateOf(valueTranslate)
    val dataTranslate: String?
        get() = translateState.value


    private val models = Observer<List<String>> {
        it?.let { list ->
            list.map { l -> println(l) }
        }
    }

    private val observerTranslate = Observer<ResultOrError> {
        it?.let {
            if (it.error != null) {
                it.error!!.localizedMessage?.let { it1 ->
                    println(it1)
                }
            } else if (it.result!!.isNotEmpty()) {
                val result = it.result!!
                translateState.value = result
            } else {
                println("EMPTY TRANSLATE")
            }
        }
    }

    private val availableLanguages: List<Language> =
        TranslateLanguage.getAllLanguages()
            .map { Language(it) }

    init {
        val processTranslation =
            OnCompleteListener {
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

    fun setLanguage() {
        sourceLang.value = Language("en")
        targetLang.value = Language("es")
        val availableLang = availableLanguages
        if (availableLang.isEmpty()) {
            downloadLanguage(sourceLang.value!!)
            downloadLanguage(targetLang.value!!)
            availableModels.observeForever(models)
        }
    }

    fun execute(dataCloud: DataCloud?) {
        if (dataCloud != null) {
            sourceText.value = dataCloud.data.title + "*" + dataCloud.data.explanation
            translatedText.observeForever(observerTranslate)
        }
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

    private fun downloadLanguage(language: Language) {
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
                    Tasks.forException(
                        it.exception ?: Exception(
                            "Fail Download"
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
        availableModels.removeObserver(models)
        translatedText.removeObserver(observerTranslate)
        super.onCleared()
        translators.evictAll()
    }
}