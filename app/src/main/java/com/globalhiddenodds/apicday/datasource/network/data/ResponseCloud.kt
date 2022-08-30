package com.globalhiddenodds.apicday.datasource.network.data

import com.squareup.moshi.Json

data class ResponseCloud(
    @field:Json(name = "success") val success: Boolean,
    @field:Json(name = "data") val data: PicDayCloud
)
