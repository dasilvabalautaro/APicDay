package com.globalhiddenodds.apicday.ui.data

data class PicDayView(
    val id: Int,
    val date: String,
    var explanation: String,
    val hdurl: String,
    val media_type: String,
    val service_version: String,
    val url: String,
    var title: String = "",
    var base64: String = ""
)