package com.globalhiddenodds.apicday.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.work.WorkInfo
import com.globalhiddenodds.apicday.R
import com.globalhiddenodds.apicday.ui.activities.MainActivity
import com.globalhiddenodds.apicday.ui.components.CircularIndeterminateProgressBar
import com.globalhiddenodds.apicday.ui.viewmodels.CrudDatabaseViewModel
import com.globalhiddenodds.apicday.ui.viewmodels.DownloadPicDayViewModel
import com.globalhiddenodds.apicday.utils.Utils
import com.skydoves.landscapist.coil.CoilImage

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
    var bitmap: Bitmap? = null
    val lblMessage = remember {
        mutableStateOf(value = "")
    }
    viewModel.setSearchDate(MainActivity.dateSearch) //Utils.formatDateNow()
    val list by viewModel.lisPicDay.observeAsState()
    list?.let {
        if (it.isNotEmpty()) {
            MainActivity.availableOptionPicDay = true
            val pic = it[0]
            if (pic.media_type == "image") {
                bitmap = Utils.decodeBase64(pic.base64)
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
            ImageBackground(option, bitmap)
        }
        Label(lblMessage)
    }
}

@Composable
fun Label(lblMessage: MutableState<String>) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .wrapContentWidth(Alignment.CenterHorizontally)
            .wrapContentHeight(Alignment.CenterVertically)
    ) {
        Text(
            text = lblMessage.value,
            style = MaterialTheme.typography.h6
        )
    }
}

@Composable
private fun ImageBackground(
    option: Int,
    bitmap: Bitmap?
) {
    var model: Any? = null
    when (option) {
        1 -> model = bitmap!!
        2 -> model = (R.drawable.background)
        3 -> model = (R.drawable.background)
    }
    CoilImage(
        imageModel = model,
        modifier = Modifier.fillMaxWidth(),
        contentDescription = "background image",
        contentScale = ContentScale.Crop
    )

}

@Composable
fun DownloadPickDay(
    viewModel: DownloadPicDayViewModel,
    lblMessage: MutableState<String> = mutableStateOf("")
) {
    val context = LocalContext.current
    if (Utils.isConnect(context)) {
        if (viewModel.status.value == false) {
            viewModel.downPicDay(MainActivity.dateSearch)
            ObserverWorkInfo(viewModel, lblMessage)
        }
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
    val list by downloadPicDay.outputWorkInfo.observeAsState()
    list?.let {
        val size = it.size - 1
        val workInfo = it[size]
        when (workInfo.state) {
            WorkInfo.State.SUCCEEDED -> {
                CircularIndeterminateProgressBar(isDisplayed = false)
                SavePicDay(lblMessage)
            }
            WorkInfo.State.FAILED -> {
                CircularIndeterminateProgressBar(isDisplayed = false)
                Utils.notify(context, stringResource(R.string.lbl_down_failed))
            }
            else -> {
                CircularIndeterminateProgressBar(isDisplayed = true)
            }
        }

    }
}

@Composable
private fun SavePicDay(lblMessage: MutableState<String>) {
    lblMessage.value = stringResource(R.string.lbl_down_finished)
    MainActivity.availableOptionPicDay = true
}

@Composable
private fun ObserverCrudTaskResult(
    crudDatabaseViewModel: CrudDatabaseViewModel
) {
    val context = LocalContext.current
    val result by crudDatabaseViewModel.taskResult.observeAsState()
    result?.let {
        Utils.notify(context, it)
    }
}
