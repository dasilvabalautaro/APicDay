package com.globalhiddenodds.apicday.ui.screens


import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.globalhiddenodds.apicday.navigation.NavigationDirections
import com.globalhiddenodds.apicday.overview.DownloadViewModel
import com.globalhiddenodds.apicday.overview.PersistenceViewModel
import com.globalhiddenodds.apicday.overview.TranslateViewModel
import com.globalhiddenodds.apicday.toolbox.Tools
import com.globalhiddenodds.apicday.ui.ImageContent
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun ImageScreen(
    navController: NavHostController,
    persistenceViewModel: PersistenceViewModel,
    modifier: Modifier = Modifier,
    downloadViewModel: DownloadViewModel = viewModel(),
    translateViewModel: TranslateViewModel = viewModel(),
) {
    Column(
        modifier = modifier
    ) {
        translateViewModel.setLanguage()
        val isDisplayProgress = remember { mutableStateOf(true) }
        val dateSearch = remember { mutableStateOf(Tools.formatDate()) }
        val translate = remember { mutableStateOf(false) }
        ImageContent(
            dataCloud =  downloadViewModel.dataImage,
            onSearch = {
                downloadViewModel.getImage(dateSearch.value)
            },
            isDisplayProgress,
            dateSearch,
            translate,
            onClickTranslate = {
                translateViewModel.execute(downloadViewModel.dataImage)
                translate.value = true
            },
            dataTranslate = translateViewModel.dataTranslate,
            likesCurrent = persistenceViewModel.likesCurrent,
            onClickFavorite = {
                persistenceViewModel
                    .getLikesOfId(downloadViewModel.dataImage!!.data.id)
            },
            onUpdateFavorite = {
                persistenceViewModel.updateLikes(
                    downloadViewModel.dataImage!!.data,
                    persistenceViewModel.likesCurrent
                )
            },
            onSendZoomImage = {
                val url = downloadViewModel.dataImage!!.data.hdurl.ifEmpty {
                    downloadViewModel.dataImage!!.data.url
                }
                val encodedUrl = URLEncoder.encode(
                    url,
                    StandardCharsets.UTF_8.toString()
                )
                navController.navigate(NavigationDirections.zoom(encodedUrl).destination)
            }
        )
    }
}