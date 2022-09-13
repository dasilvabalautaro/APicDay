package com.globalhiddenodds.apicday.network

import com.globalhiddenodds.apicday.database.Likes
import com.squareup.moshi.Json

data class ImageCloud(
    var id: Int = 0,
    var date: String = "",
    var explanation: String = "",
    var hdurl: String = "",
    @Json(name = "media_type") var mediaType: String = "",
    var title: String = "",
    var url: String = "",
    var like: Int = 0
)

fun ImageCloud.toLikes(): Likes = Likes(
    id,
    like,
    date,
    title,
    url,
    mediaType
)
