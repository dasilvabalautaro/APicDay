package com.globalhiddenodds.apicday.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.globalhiddenodds.apicday.overview.LikeView

@Composable
fun ItemBestImage(
    likeView: LikeView,
    onDetail: () -> Unit
) {

    Card(
        modifier = Modifier.padding(4.dp).drawBehind { drawRect(Color.Black)  },
        elevation = 5.dp) {
        Column(modifier = Modifier.fillMaxWidth()) {
            TitleCenter(title = likeView.title)
            SubTitle(title =  "My Votes ${likeView.like}")
            TextRight(label = likeView.date)
            MediaImage(url = likeView.url, type = likeView.mediaType, onDetail)
        }
    }
}