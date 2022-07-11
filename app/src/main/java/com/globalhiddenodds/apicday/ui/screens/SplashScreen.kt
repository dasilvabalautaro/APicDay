package com.globalhiddenodds.apicday.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.globalhiddenodds.apicday.R
import com.globalhiddenodds.apicday.ui.viewmodels.DownloadPicDayViewModel
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun SplashBody(viewModel: DownloadPicDayViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .semantics { contentDescription = "Splash Screen" }) {
        LoadImageBackground()
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
private fun DownloadPickDay(viewModel: DownloadPicDayViewModel){

}