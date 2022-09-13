package com.globalhiddenodds.apicday.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Title(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.subtitle1,
        modifier = Modifier
            .padding(start = 10.dp, top = 5.dp, end = 60.dp, bottom = 5.dp)
            .wrapContentWidth(Alignment.Start)
    )
}

@Composable
fun TitleCenter(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.subtitle1,
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
            .wrapContentWidth(Alignment.CenterHorizontally)
    )
}

@Composable
fun SubTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.subtitle2,
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 10.dp)
            .wrapContentWidth(Alignment.CenterHorizontally)
    )
}

@Composable
fun TextRight(label: String) {
    Text(
        text = label,
        style = MaterialTheme.typography.overline,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 5.dp, end = 10.dp)
            .wrapContentWidth(Alignment.End)
    )
}

@Composable
fun Favorites(likes: Int, author: String, onUpdateFavorite: () -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        IconButton(onClick = onUpdateFavorite) {
            Icon(
                imageVector = Icons.Filled.FavoriteBorder,
                contentDescription = "My likes"
            )
        }
        Text(
            text = likes.toString(),
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(start = 15.dp, top = 8.dp)
        )
        TextRight(author)
    }
}


@Composable
fun BodyText(body: String) {
    Text(
        text = body,
        style = MaterialTheme.typography.body1,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .wrapContentWidth(Alignment.Start)
    )
}
