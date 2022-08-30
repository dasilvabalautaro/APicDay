package com.globalhiddenodds.apicday.datasource.network

import com.globalhiddenodds.apicday.datasource.network.data.BodyCloud
import com.globalhiddenodds.apicday.datasource.network.data.ResponseCloud
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

private const val baseUrl = "https://heroapicday.herokuapp.com/"
//private const val baseUrl = "http://192.168.0.2:3000/service/"
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofitIDay = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(baseUrl)
    .build()

interface PicDayCloudService {
    @Headers("Content-Type: application/json")
    @POST("apicday")
    suspend fun searchPicDay(@Body body: BodyCloud): ResponseCloud?
}

object GetPicDay {
    val retrofitPicDayService: PicDayCloudService by lazy {
        retrofitIDay.create(PicDayCloudService::class.java)
    }
}

