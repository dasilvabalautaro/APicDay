package com.globalhiddenodds.apicday.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.hilt.navigation.compose.hiltViewModel
import com.globalhiddenodds.apicday.R
import com.globalhiddenodds.apicday.ui.activities.MainActivity
import com.globalhiddenodds.apicday.ui.components.CircularIndeterminateProgressBar
import com.globalhiddenodds.apicday.ui.controls.ImageBackground
import com.globalhiddenodds.apicday.ui.controls.Label
import com.globalhiddenodds.apicday.ui.viewmodels.CrudDatabaseViewModel
import com.globalhiddenodds.apicday.ui.viewmodels.DownloadPicDayViewModel
import com.globalhiddenodds.apicday.utils.Utils

@Composable
fun SplashBody(viewModel: CrudDatabaseViewModel = hiltViewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .semantics { contentDescription = "Splash Screen" }) {
        Box(modifier = Modifier.fillMaxSize()) {
            LoadImageBackground(viewModel)

        }
    }
}

@Composable
fun LoadImageBackground(viewModel: CrudDatabaseViewModel) {
    var option = 2
    var url = ""
    val lblMessage = remember {
        mutableStateOf(value = "")
    }
    viewModel.setSearchDate(MainActivity.dateSearch)
    val list by viewModel.lisPicDay.observeAsState()
    list?.let {
        if (it.isNotEmpty()) {
            MainActivity.availableOptionPicDay = true
            val pic = it[0]
            if (pic.media_type == "image") {
                url = pic.url
                option = 1
            } else {
                option = 3
            }
        } else {
            val viewModelDown: DownloadPicDayViewModel = hiltViewModel()
            DownloadPickDay(viewModelDown, lblMessage)
        }
        when (option) {
            1 -> lblMessage.value = stringResource(R.string.lbl_pic_of_day)
            2 -> lblMessage.value = stringResource(id = R.string.lbl_down_pic_day)
            3 -> lblMessage.value = stringResource(id = R.string.lbl_video_download)
        }
        Column(modifier = Modifier.fillMaxSize()) {
            ImageBackground(option, url)
        }
        Label(lblMessage)
    }
}

@Composable
fun DownloadPickDay(
    viewModel: DownloadPicDayViewModel,
    lblMessage: MutableState<String> = mutableStateOf("")
) {
    val context = LocalContext.current
    if (Utils.isConnect(context)) {
        ObserverWorkInfo(viewModel, lblMessage)
        viewModel.downPicDay(MainActivity.dateSearch)
    } else {
        Utils.notify(context, "Connectivity fail.")
    }
}

@Composable
private fun ObserverWorkInfo(
    downloadPicDay: DownloadPicDayViewModel,
    lblMessage: MutableState<String>
) {
    val context = LocalContext.current
    CircularIndeterminateProgressBar(isDisplayed = true)
    val list by downloadPicDay.outputWorkInfo.observeAsState()
    list?.let {
        if (it) {
            SavePicDay(lblMessage)
        } else {
            Utils.notify(context, downloadPicDay.messageError) //stringResource(R.string.lbl_down_failed
        }
        CircularIndeterminateProgressBar(isDisplayed = false)
    }
}

@Composable
private fun SavePicDay(lblMessage: MutableState<String>) {
    lblMessage.value = stringResource(R.string.lbl_down_finished)
    MainActivity.availableOptionPicDay = true
}
