package com.globalhiddenodds.apicday.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Translate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.globalhiddenodds.apicday.ui.activities.MainActivity
import com.globalhiddenodds.apicday.ui.controls.*
import com.globalhiddenodds.apicday.ui.data.PicDayView
import com.globalhiddenodds.apicday.ui.viewmodels.CrudDatabaseViewModel
import com.globalhiddenodds.apicday.ui.viewmodels.DownloadPicDayViewModel

@Composable
fun PicDayBody(viewModel: CrudDatabaseViewModel = hiltViewModel()) {
    val dateSearch = MainActivity.dateSearch
    SearchPicDay(
        viewModel = viewModel, date = dateSearch
    )
}

@Composable
fun SearchPicDay(
    viewModel: CrudDatabaseViewModel, date: String
) {
    viewModel.setSearchDate(date)
    val list by viewModel.lisPicDay.observeAsState()
    list?.let {
        if (it.isNotEmpty()) {
            DrawScreen(picDays = it)
        } else {
            val viewModelDown: DownloadPicDayViewModel = hiltViewModel()
            MainActivity.availableOptionPicDay = false
            DownloadPickDay(viewModelDown)
        }
    }
}

@Composable
private fun DrawScreen(
    picDays: List<PicDayView>
) {
    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
        items(items = picDays) { pic ->
            Greet(pic = pic)
        }
    }
}

@Composable
private fun Greet(
    pic: PicDayView
) {
    Card(
        backgroundColor = MaterialTheme.colors.primary,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        CardContentPic(picDay = pic)
    }
}

@Composable
private fun CardContentPic(
    picDay: PicDayView
) {
    val translateLanguage = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
    ) {
        Title(name = picDay.title)
        SubTitle(name = picDay.date)
        if (picDay.media_type == "image") {
            ImagePost(url = picDay.url)
        } else {
            VideoPlayer(videoUrl = picDay.url)
        }
        CopyRight(name = "Image Credit: NASA")
        BodyText(body = picDay.explanation)
        if (translateLanguage.value) {
            Translate(picDay = picDay)
        }
    }
    ShowCalendar()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp, vertical = 35.dp),
        horizontalArrangement = Arrangement.End
    ) {
        IconButton(onClick = {
            translateLanguage.value = !translateLanguage.value
        }) {
            Icon(
                imageVector = if (translateLanguage.value) Icons.Filled.Refresh else Icons.Filled.Translate,
                contentDescription = "Translate"
            )
        }
    }
}
