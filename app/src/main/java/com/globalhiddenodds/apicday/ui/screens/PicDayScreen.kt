package com.globalhiddenodds.apicday.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.globalhiddenodds.apicday.R
import com.globalhiddenodds.apicday.ui.data.PicDayView
import com.globalhiddenodds.apicday.ui.viewmodels.CrudDatabaseViewModel
import com.globalhiddenodds.apicday.utils.Utils
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun PicDayBody() {
    val viewModel: CrudDatabaseViewModel = hiltViewModel()
    val pic by viewModel.picDay.observeAsState()
    pic?.let {
        DrawScreen(it)
    }
}

@Composable
private fun DrawScreen(picDay: PicDayView) {
    Card(
        backgroundColor = MaterialTheme.colors.primary,
        modifier = Modifier.padding(4.dp, 8.dp)
    ) {
        CardContentPic(picDay)
    }
}

@Composable
private fun CardContentPic(picDay: PicDayView){
    Column(modifier = Modifier.padding(10.dp)){
        Title(name = picDay.title)
        SubTitle(name = picDay.date)
        val bitmap = Utils.decodeBase64(picDay.base64)
        ImagePost(bitmap = bitmap)
        BodyText(body = picDay.explanation)
    }
}

@Composable
fun Title(name: String) {
    Text(
        text = name,
        style = MaterialTheme.typography.subtitle1,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp, vertical = 10.dp)
            .wrapContentWidth(Alignment.CenterHorizontally)
    )
}

@Composable
fun SubTitle(name: String) {
    Text(
        text = name,
        style = MaterialTheme.typography.h3,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp, vertical = 10.dp)
            .wrapContentWidth(Alignment.CenterHorizontally)
    )
}

@Composable
fun BodyText(body: String) {
    Text(
        text = body,
        style = MaterialTheme.typography.body1,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp, vertical = 10.dp)
            .wrapContentWidth(Alignment.Start)
    )
}
@Composable
fun ImagePost(bitmap: Bitmap){
    CoilImage(
        imageModel = bitmap,
        contentScale = ContentScale.Inside,
        placeHolder = ImageVector.vectorResource(id = R.drawable.loading_animation),
        error = ImageVector.vectorResource(id = R.drawable.ic_broken_image)
    )
}