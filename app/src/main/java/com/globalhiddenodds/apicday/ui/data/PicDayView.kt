package com.globalhiddenodds.apicday.ui.data

data class PicDayView(
    val id: Int,
    val date: String,
    val explanation: String,
    val hdurl: String,
    val media_type: String,
    val service_version: String,
    val title: String,
    val url: String,
    var base64: String = ""
)