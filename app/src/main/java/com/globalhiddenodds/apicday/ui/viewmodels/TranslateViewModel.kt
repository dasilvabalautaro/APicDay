package com.globalhiddenodds.apicday.ui.viewmodels

import android.app.Application
import android.util.LruCache
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions

class TranslateViewModel(application: Application):
    AndroidViewModel(application) {
        companion object {
            private const val NUM_TRANSLATORS = 3
        }
    private val modelManager:
            RemoteModelManager = RemoteModelManager.getInstance()
    private val translators = object:
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
    val sourceLang = MutableLiveData<TranslateLanguage.Language>()
    val targetLang = MutableLiveData<TranslateLanguage.Language>()
    val sourceText = MutableLiveData<String>()

}