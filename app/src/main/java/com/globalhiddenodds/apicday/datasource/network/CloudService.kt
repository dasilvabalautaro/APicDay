package com.globalhiddenodds.apicday.datasource.network

import com.globalhiddenodds.apicday.datasource.network.data.PicDayCloud
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val baseUrl = "https://api.nasa.gov/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofitIDay = Retrofit.Builder()
    .baseUrl(baseUrl)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

interface PicDayCloudService {
    @GET("planetary/apod")
    suspend fun searchPicDay(
        @Query("date") date: String,
        @Query("api_key") key: String = "d5lJakXvDaJ5tBUOg9PDohG6FLoQy19qXgAtIczq",
        ): PicDayCloud
}

object GetPicDay {
    val retrofitPicDayService: PicDayCloudService by lazy {
        retrofitIDay.create(PicDayCloudService::class.java)
    }
}

