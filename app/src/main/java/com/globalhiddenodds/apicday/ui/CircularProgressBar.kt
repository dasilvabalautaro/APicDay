package com.globalhiddenodds.apicday.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CircularProgressBar(isDisplay: Boolean){
    if (isDisplay){
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(100.dp),
        horizontalArrangement = Arrangement.Center) {
            CircularProgressIndicator(
                color = MaterialTheme.colors.onSecondary)
        }
    }
}