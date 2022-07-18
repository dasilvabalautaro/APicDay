package com.globalhiddenodds.apicday.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.work.WorkInfo
import com.globalhiddenodds.apicday.R
import com.globalhiddenodds.apicday.ui.activities.MainActivity
import com.globalhiddenodds.apicday.ui.viewmodels.CrudDatabaseViewModel
import com.globalhiddenodds.apicday.ui.viewmodels.DownloadPicDayViewModel
import com.globalhiddenodds.apicday.utils.Utils
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun SplashBody() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .semantics { contentDescription = "Splash Screen" }) {
        LoadImageBackground()
    }
}

@Composable
fun LoadImageBackground(viewModel: CrudDatabaseViewModel = hiltViewModel()) {
    val context = LocalContext.current
    viewModel.setSearchDate(MainActivity.dateSearch)
    val list by viewModel.lisPicDay.observeAsState()
    list?.let {
        if (it.isNotEmpty()){
            val pic = it[0]
            val bitmap = Utils.decodeBase64(pic.base64)
            CoilImage(
                imageModel = bitmap,
                modifier = Modifier.fillMaxWidth(),
                contentDescription = "background image",
                contentScale = ContentScale.Crop //ContentScale.FillBounds
            )
            Utils.notify(context, stringResource(R.string.lbl_pic_of_day))
        }
        else {
            CoilImage(
                imageModel = (R.drawable.background),
                modifier = Modifier.fillMaxWidth(),
                contentDescription = "background image",
                contentScale = ContentScale.Crop
            )
            Utils.notify(context, stringResource(id = R.string.lbl_down_pic_day))
            val viewModelDown: DownloadPicDayViewModel = hiltViewModel()
            DownloadPickDay(viewModelDown)
        }
    }
}

@Composable
fun DownloadPickDay(viewModel: DownloadPicDayViewModel) {
    val context = LocalContext.current
    if (Utils.isConnect(context)) {
        ObserverWorkInfo(viewModel)
        if (viewModel.status.value == false) {
            viewModel.downPicDay(MainActivity.dateSearch)
        }
    } else {
        Utils.notify(context, "Connectivity fail.")
    }
}

@Composable
private fun ObserverWorkInfo(downloadPicDay: DownloadPicDayViewModel) {
    val context = LocalContext.current
    val list by downloadPicDay.outputWorkInfo.observeAsState()
    list?.let {
        if (it.isNotEmpty()) {
            val workInfo = it[0]
            when {
                workInfo.state.isFinished -> {
                    SavePicDay()
                }
                workInfo.state == WorkInfo.State.FAILED -> {
                    Utils.notify(context, stringResource(R.string.lbl_down_failed))
                }
            }
        }
    }
}

@Composable
private fun SavePicDay(viewModel: CrudDatabaseViewModel = hiltViewModel()) {
    val context = LocalContext.current
    ObserverCrudTaskResult(viewModel)
    viewModel.insert()
    Utils.notify(context, stringResource(R.string.lbl_down_finished))
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