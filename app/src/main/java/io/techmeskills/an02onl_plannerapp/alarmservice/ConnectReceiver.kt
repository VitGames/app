package io.techmeskills.an02onl_plannerapp.alarmservice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

class ConnectReceiver: BroadcastReceiver() {

companion object{
    var connectivityReceiverListener: ConnectivityReceiverListener? = null
}
    override fun onReceive(context: Context?, p1: Intent?) {
        if (connectivityReceiverListener != null) {
            connectivityReceiverListener!!.onNetworkConnectionChanged(isConnectedOrConnecting(context!!))
        }
    }
    private fun isConnectedOrConnecting(context: Context): Boolean {
        val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }

    interface ConnectivityReceiverListener {
        fun onNetworkConnectionChanged(isConnected: Boolean)
    }
}