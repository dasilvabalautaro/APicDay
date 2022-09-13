package com.globalhiddenodds.apicday.toolbox

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object Tools {

    fun formatDate(): String {
        val df = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.now()
        return df.format(date)
    }

    fun connectToCloud(context: Context): Boolean {
        try {
            val connectivityManager = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } catch (e: Exception) {
            return false
        }
    }
}