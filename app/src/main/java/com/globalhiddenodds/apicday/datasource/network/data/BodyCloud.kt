package com.globalhiddenodds.apicday.datasource.network.data

import com.squareup.moshi.Json

data class BodyCloud(
    @field:Json(name = "url") val url: String)