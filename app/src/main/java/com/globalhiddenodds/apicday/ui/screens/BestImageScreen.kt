package com.globalhiddenodds.apicday.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.globalhiddenodds.apicday.overview.LikeView
import com.globalhiddenodds.apicday.ui.ListImageContent

@Composable
fun BestImageScreen(
    listLikes: List<LikeView>?,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        ListImageContent(
            listImage = remember { listLikes },
            onDetail = {}
        )
    }
}