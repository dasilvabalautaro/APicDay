package com.globalhiddenodds.apicday.ui.controls

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Title(name: String) {
    Text(
        text = name,
        style = MaterialTheme.typography.subtitle1,
        modifier = Modifier
            .padding(start = 15.dp, top = 5.dp, end = 60.dp, bottom = 5.dp)
            .wrapContentWidth(Alignment.Start)
    )
}

@Composable
fun CopyRight(name: String) {
    Text(
        text = name,
        style = MaterialTheme.typography.overline,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .wrapContentWidth(Alignment.End)
    )
}

@Composable
fun SubTitle(name: String) {
    Text(
        text = name,
        style = MaterialTheme.typography.subtitle2,
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
fun Label(lblMessage: MutableState<String>) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .wrapContentWidth(Alignment.CenterHorizontally)
            .wrapContentHeight(Alignment.CenterVertically)
    ) {
        Text(
            text = lblMessage.value,
            style = MaterialTheme.typography.h6
        )
    }
}
