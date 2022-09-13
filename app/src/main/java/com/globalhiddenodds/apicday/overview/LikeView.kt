package com.globalhiddenodds.apicday.overview

data class LikeView(
    val id: Int,
    val title: String,
    val like: Int,
    val date: String,
    val url: String,
    val mediaType: String)