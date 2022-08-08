package com.globalhiddenodds.apicday.datasource.network

import com.globalhiddenodds.apicday.datasource.network.data.BodyCloud
import com.globalhiddenodds.apicday.datasource.network.data.ResponseCloud
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

private const val baseUrl = "http://nodejsexampleaws.us-east-1.elasticbeanstalk.com/"
//private const val baseUrl = "http://192.168.0.2:3000/service/"
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofitIDay = Retrofit.Builder()
    .baseUrl(baseUrl)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

//    Interceptor of Head Rest out certificate
// .client(getUnsafeOkHttpClient().build())
//private fun getUnsafeOkHttpClient(): OkHttpClient.Builder {
//    try {
//        // Create a trust manager that does not validate certificate chains
//        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
//            @Throws(CertificateException::class)
//            override fun checkClientTrusted(
//                chain: Array<java.security.cert.X509Certificate>,
//                authType: String
//            ) {
//            }
//
//            @Throws(CertificateException::class)
//            override fun checkServerTrusted(
//                chain: Array<java.security.cert.X509Certificate>,
//                authType: String
//            ) {
//            }
//
//            override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
//                return arrayOf()
//            }
//        })
//
//        // Install the all-trusting trust manager
//        val sslContext = SSLContext.getInstance("SSL")
//        sslContext.init(null, trustAllCerts, java.security.SecureRandom())
//
//        // Create an ssl socket factory with our all-trusting manager
//        val sslSocketFactory = sslContext.socketFactory
//
//        val builder = OkHttpClient.Builder()
//        builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
//        builder.hostnameVerifier { _, _ -> true }
//        if (BuildConfig.DEBUG) {
//            val loggingInterceptor =
//                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
//            builder.addInterceptor(loggingInterceptor)
//        }
//        return builder
//    } catch (e: Exception) {
//        throw RuntimeException(e)
//    }
//}

interface PicDayCloudService {
    @POST("apicday")
    suspend fun searchPicDay(@Body body: BodyCloud): ResponseCloud?
}

object GetPicDay {
    val retrofitPicDayService: PicDayCloudService by lazy {
        retrofitIDay.create(PicDayCloudService::class.java)
    }
}

