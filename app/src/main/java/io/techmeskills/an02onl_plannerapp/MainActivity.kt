package io.techmeskills.an02onl_plannerapp

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import io.techmeskills.an02onl_plannerapp.alarmservice.AlarmReceiver
import io.techmeskills.an02onl_plannerapp.alarmservice.ConnectReceiver
import io.techmeskills.an02onl_plannerapp.databinding.ActivityMainBinding
import io.techmeskills.an02onl_plannerapp.support.SupportActivityInset
import io.techmeskills.an02onl_plannerapp.support.setWindowTransparency

class MainActivity : SupportActivityInset<ActivityMainBinding>(),
    ConnectReceiver.ConnectivityReceiverListener {

    override lateinit var viewBinding: ActivityMainBinding

    private val navHostFragment by lazy { (supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment) }
    private val navController: NavController by lazy { navHostFragment.navController }
    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        // setWindowTransparency(this)
        registerReceiver(ConnectReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun getActiveFragment(): Fragment? {
        return navHostFragment.childFragmentManager.fragments[0]
    }

    override fun onResume() {
        super.onResume()
        ConnectReceiver.connectivityReceiverListener = this
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showNetworkMessage(isConnected)
    }

    private fun showNetworkMessage(isConnected: Boolean) {
        if (!isConnected) {
            snackbar = Snackbar.make(findViewById(android.R.id.content),
                "Ожидание соединения...",
                Snackbar.LENGTH_LONG)
            snackbar?.duration = BaseTransientBottomBar.LENGTH_INDEFINITE
            snackbar?.show()
        } else {
            snackbar?.dismiss()
        }
    }
}