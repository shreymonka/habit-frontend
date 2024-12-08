package com.dalhousie.habit.util.components

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Network helper is specially for checking if device is connected to internet or not
 */
interface NetworkHelper {

    /**
     * Returns True if internet is connected, false otherwise
     */
    fun isNetworkAvailable(): Boolean
}

/**
 * Implementation of [NetworkHelper].
 */
@Singleton
class NetworkHelperImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : NetworkHelper {

    override fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetwork?.let { network ->
            connectivityManager.getNetworkCapabilities(network)?.let { networkCapabilities ->
                when {
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    else -> false
                }
            } ?: false
        } ?: false
    }
}
