package com.globalhiddenodds.apicday.ui.data

data class PicDayView(
    val id: Int,
    val date: String,
    var explanation: String,
    val hdurl: String,
    val media_type: String,
    val service_version: String,
    var title: String = "",
    val copyright: String = "",
    val url: String,
    var base64: String = ""
)