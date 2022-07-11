package com.globalhiddenodds.apicday.datasource.network.data

import com.globalhiddenodds.apicday.datasource.database.data.PicDay

data class PicDayCloud(
    val date: String,
    val explanation: String,
    val hdurl: String,
    val media_type: String,
    val service_version: String,
    val title: String,
    val url: String,
    var base64: String = ""
){
    fun toPicDay(): PicDay {
        return PicDay(0, date, explanation,
            hdurl, media_type, service_version, title, url, base64 )
    }
}