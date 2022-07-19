package com.globalhiddenodds.apicday.datasource.database.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.globalhiddenodds.apicday.ui.data.PicDayView

@Entity(tableName = "picday")
data class PicDay(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "explanation") val explanation: String,
    @ColumnInfo(name = "hdurl") val hdurl: String,
    @ColumnInfo(name = "mediaType") val mediaType: String,
    @ColumnInfo(name = "serviceVersion") val serviceVersion: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "base64") val base64: String
)

fun PicDay.toPicDayView(): PicDayView = PicDayView(
    id, date, explanation, hdurl, mediaType, serviceVersion, url, title, base64)
