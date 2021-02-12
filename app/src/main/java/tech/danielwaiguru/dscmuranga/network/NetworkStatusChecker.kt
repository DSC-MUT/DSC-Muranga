package tech.danielwaiguru.dscmuranga.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

class NetworkStatusChecker(context: Context) {
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
    as ConnectivityManager
    inline fun performIfConnected(notConnectedAction:()->Unit, connectedAction:()->Unit) {
        if (isConnected()) {
            connectedAction()
        } else {
            notConnectedAction()
        }
    }
    fun isConnected(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager
                .getNetworkCapabilities(activeNetwork) ?: return false
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
                    && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                    && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            var capableNetwork = false
            for (network in connectivityManager.allNetworks){
                val capabilities = connectivityManager
                    .getNetworkCapabilities(network) ?: return false
                capableNetwork = capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
                        && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            }
            capableNetwork
        }
    }
}