package com.globalhiddenodds.apicday.overview

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globalhiddenodds.apicday.network.AstroImageService
import com.globalhiddenodds.apicday.network.DataCloud
import com.globalhiddenodds.apicday.toolbox.Tools
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DownloadViewModel : ViewModel() {
    private var dataCloud: DataCloud? = null
    private val dataCloudState = mutableStateOf(dataCloud)
    val dataImage: DataCloud?
        get() = dataCloudState.value

    init {
        getImage(Tools.formatDate())
    }

    fun getImage(date: String) {
        viewModelScope.launch(
            viewModelScope.coroutineContext + Dispatchers.IO
        ) {
            try {
                dataCloudState.value = AstroImageService
                    .retrofitAstroService.getAstronomicImage(date)

            } catch (ex: Exception) {
                println(ex.message)
            }
        }
    }

}