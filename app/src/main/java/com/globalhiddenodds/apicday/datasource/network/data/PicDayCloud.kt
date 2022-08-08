package com.globalhiddenodds.apicday.datasource.network.data

import com.globalhiddenodds.apicday.datasource.database.data.PicDay

data class PicDayCloud(
    var date: String = "",
    var explanation: String = "",
    var hdurl: String = "",
    var media_type: String = "",
    var service_version: String = "",
    var copyright: String = "",
    var title: String = "",
    var url: String = "",
    var base64: String = ""
){
    fun toPicDay(): PicDay {
        return PicDay(0, date, explanation,
            hdurl, media_type, service_version, title, copyright, url, base64 )
    }
}