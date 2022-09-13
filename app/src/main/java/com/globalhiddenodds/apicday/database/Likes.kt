package com.globalhiddenodds.apicday.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.globalhiddenodds.apicday.overview.LikeView

@Entity(tableName = "likes")
data class Likes(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "favorite") val like: Int,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "mediaType") val mediaType: String
)

fun Likes.toLikeView(): LikeView = LikeView(
    id,
    title,
    like,
    date,
    url,
    mediaType
)
