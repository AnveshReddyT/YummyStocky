package app.yummy.stocky.base

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {

    protected val context: Context by lazy { getApplication<Application>().applicationContext }

    var isLoading: ObservableBoolean = ObservableBoolean()
        private set

    protected val _networkStatus: MutableLiveData<Boolean> by lazy {
        MutableLiveData(
            isNetworkConnected(context)
        )
    }

    val networkStatus: LiveData<Boolean> = _networkStatus

    abstract fun init()

    private fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }
}