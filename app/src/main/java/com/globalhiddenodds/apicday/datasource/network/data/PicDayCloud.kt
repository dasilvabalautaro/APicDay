package com.globalhiddenodds.apicday.datasource.network.data

import com.globalhiddenodds.apicday.datasource.database.data.PicDay
import com.squareup.moshi.Json

data class PicDayCloud(
    @field:Json(name = "id") var id: Int = 0,
    @field:Json(name = "date") var date: String = "",
    @field:Json(name = "explanation") var explanation: String = "",
    @field:Json(name = "hdurl") var hdurl: String = "",
    @field:Json(name = "media_type") var media_type: String = "",
    @field:Json(name = "title") var title: String = "",
    @field:Json(name = "url") var url: String = ""
    //@field:Json(name = "base64") var base64: String = ""
)

fun PicDayCloud.toPicDay(): PicDay = PicDay(0, date, explanation,
        hdurl, media_type, title, url)