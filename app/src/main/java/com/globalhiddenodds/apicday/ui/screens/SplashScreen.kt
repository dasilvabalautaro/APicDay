package com.globalhiddenodds.apicday.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import com.globalhiddenodds.apicday.ui.viewmodels.CrudDatabaseViewModel
import com.globalhiddenodds.apicday.ui.viewmodels.DownloadPicDayViewModel
import com.globalhiddenodds.apicday.utils.Utils
import com.skydoves.landscapist.coil.CoilImage
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun SplashBody(viewModel: DownloadPicDayViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .semantics { contentDescription = "Splash Screen" }) {
        LoadImageBackground()
        DownloadPickDay(viewModel)
    }
}

@Composable
fun LoadImageBackground() {
    CoilImage(
        imageModel = (R.drawable.background),
        modifier = Modifier.fillMaxSize(),
        contentDescription = "background image",
        contentScale = ContentScale.FillBounds
    )
}

@Composable
private fun DownloadPickDay(viewModel: DownloadPicDayViewModel) {
    val context = LocalContext.current
    if (Utils.isConnect(context)) {
        ObserverWorkInfo(viewModel)
        if (viewModel.status.value == false) {
            val df = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val date = LocalDate.parse("yyyy-MM-dd", df)

            viewModel.downPicDay(date.toString())
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