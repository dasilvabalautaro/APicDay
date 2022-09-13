package com.globalhiddenodds.apicday.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.globalhiddenodds.apicday.overview.LikeView
import com.globalhiddenodds.apicday.R
import kotlinx.coroutines.launch

@Composable
fun ListImageContent(
    listImage: List<LikeView>?,
    onDetail: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (listImage != null && listImage.isNotEmpty()) {
        val listState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()
        LazyColumn(
            state = listState,
            modifier = modifier
        ) {
            items(items = listImage, key = { it.id }) { lv ->
                ItemBestImage(likeView = lv, onDetail)
            }

        }
        val showButton = remember {
            derivedStateOf {
                listState.firstVisibleItemIndex > 0
            }
        }

        AnimatedVisibility(visible = showButton.value) {
            coroutineScope.launch {
                listState.firstVisibleItemIndex
            }
        }
    } else {
        Column {
            Text(
                text = stringResource(R.string.lbl_not_found_favorites),
                style = MaterialTheme.typography.h4,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 10.dp)
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
        }
    }
}