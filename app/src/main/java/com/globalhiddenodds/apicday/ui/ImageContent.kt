package com.globalhiddenodds.apicday.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Translate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.globalhiddenodds.apicday.network.DataCloud
import com.globalhiddenodds.apicday.toolbox.Tools
import com.globalhiddenodds.apicday.R
import dev.patrickgold.compose.tooltip.tooltip
import kotlinx.coroutines.launch


@Composable
fun ImageContent(
    dataCloud: DataCloud?,
    onSearch: () -> Unit,
    isDisplayProgress: MutableState<Boolean>,
    dateSearch: MutableState<String>,
    translate: MutableState<Boolean>,
    onClickTranslate: () -> Unit,
    dataTranslate: String?,
    likesCurrent: Int,
    onClickFavorite: () -> Unit,
    onUpdateFavorite: () -> Unit,
    onSendZoomImage: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(hostState = it) { data ->
                Snackbar(
                    modifier = Modifier.border(2.dp, MaterialTheme.colors.secondary),
                    snackbarData = data
                )
            }
        },
        floatingActionButton = {
            if (Tools.connectToCloud(context)) {
                FloatingActionButton(
                    onClick = onSearch,
                    modifier = Modifier.tooltip(stringResource(R.string.tip_down_select))
                ) {
                    Icon(imageVector = Icons.Default.Download, contentDescription = null)
                }
            } else {
                scope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(context.getString(R.string.lbl_fail_connect))
                }
            }
        }
    ) {
        CircularProgressBar(isDisplay = isDisplayProgress.value)
        LazyColumn(modifier = Modifier.padding(it)) {
            item {
                Card(
                    backgroundColor = MaterialTheme.colors.primary,
                    modifier = modifier
                        .padding(
                            vertical = 4.dp,
                            horizontal = 4.dp
                        )
                ) {
                    if (dataCloud != null) {
                        Column(
                            modifier = modifier
                                .fillMaxWidth()
                        ) {
                            isDisplayProgress.value = false
                            ContentCard(
                                dataCloud = dataCloud,
                                likesCurrent = likesCurrent,
                                onUpdateFavorite,
                                onClickFavorite,
                                onSendZoomImage
                            )

                        }
                        ShowCalendar(dateSearch)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 35.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            IconButton(
                                onClick = onClickTranslate,
                                modifier = Modifier.tooltip("Translate text to spanish")
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Translate,
                                    contentDescription = "Translate"
                                )
                            }
                        }
                        if (dataTranslate != null && translate.value) {
                            val translateTask = dataTranslate.split("*")
                            dataCloud.data.title = translateTask[0]
                            dataCloud.data.explanation = translateTask[1]
                            translate.value = false
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ContentCard(
    dataCloud: DataCloud,
    likesCurrent: Int,
    onUpdateFavorite: () -> Unit,
    onClickFavorite: () -> Unit,
    onSendZoomImage: () -> Unit
) {
    onClickFavorite()
    Title(title = dataCloud.data.title)
    SubTitle(title = dataCloud.data.date)
    MediaImage(url = dataCloud.data.url, type = dataCloud.data.mediaType, onSendZoomImage)
    Favorites(likes = likesCurrent, author = "Image Credit: NASA", onUpdateFavorite)
    BodyText(body = dataCloud.data.explanation)
}