package com.globalhiddenodds.apicday.ui.controls

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.globalhiddenodds.apicday.ui.data.PicDayView
import com.globalhiddenodds.apicday.ui.viewmodels.TranslateViewModel
import com.globalhiddenodds.apicday.utils.Utils

@Composable
fun Translate(picDay: PicDayView) {
    val context = LocalContext.current
    val viewModel: TranslateViewModel = hiltViewModel()
    viewModel.sourceLang.value = TranslateViewModel.Language("en")
    viewModel.targetLang.value = TranslateViewModel.Language("es")

    val availableLang = viewModel.availableLanguages
    if (availableLang.isEmpty()) {
        viewModel.downloadLanguage(viewModel.sourceLang.value!!)
        viewModel.downloadLanguage(viewModel.targetLang.value!!)
        val model by viewModel.availableModels.observeAsState()
        model?.let { list ->
            list.map { Utils.notify(context, it) }
        }
    }

    val dataTranslate = "${picDay.title} * ${picDay.explanation}"
    viewModel.sourceText.value = dataTranslate

    val translate by viewModel.translatedText.observeAsState()
    translate?.let {
        if (it.error != null) {
            it.error!!.localizedMessage?.let { it1 ->
                Utils.notify(context, it1)
            }
        } else if (it.result!!.isNotEmpty()) {
            val result = it.result!!
            val arrResult = result.split("*")
            picDay.title = arrResult[0]
            picDay.explanation = arrResult[1]
        } else {
            println("EMPTY TRANSLATE")
        }
    }
}
