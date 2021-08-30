package app.yummy.stocky.screens.home

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import app.yummy.stocky.base.BaseViewModel
import app.yummy.stocky.model.Stock
import com.google.gson.Gson
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.util.HashMap
import javax.inject.Inject


class HomeViewModel @Inject constructor(application: Application, private val gson: Gson) : BaseViewModel(application) {

    private val _stocks: MutableLiveData<List<Stock>> = MutableLiveData()

    val companyTypesUpdated: MutableLiveData<Boolean> = MutableLiveData()

    val stocks: LiveData<List<Stock>> = _stocks

    val companyTypes: HashSet<String> = HashSet(listOf())

    val selectedFilters = ArrayList<String>()

    private val stockMap = HashMap<String, Stock>()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun init() {
        if (_networkStatus.value == false) Toast.makeText(
            context, "You are disconnected from Internet!", Toast.LENGTH_LONG
        ).show()
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                _networkStatus.postValue(true)
            }

            override fun onLost(network: Network) {
                _networkStatus.postValue(false)
                Toast.makeText(context, "You are disconnected from Internet!", Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    fun updateStocksData(update: JSONArray) {
        viewModelScope.launch {
            for (i in 0 until update.length()) {
                val stock = gson.fromJson(update.getJSONObject(i).toString(), Stock::class.java)
                if (stockMap.containsKey(stock.id)) {
                    val price = stockMap[stock.id]!!.price
                    if (price != stock.price) {
                        stockMap[stock.id]!!.price = stock.price
                        stockMap[stock.id]!!.trend = if (price > stock.price) -1 else 1
                    }
                } else {
                    stockMap[stock.id] = stock
                    companyTypes.addAll(stock.companyType)
                    companyTypesUpdated.postValue(true)

                }
            }
            val filteredList = ArrayList<Stock>()
            stockMap.values.forEach{
                if (it.hasSelectedFilter(selectedFilters)){
                    filteredList.add(it)
                }
            }

            _stocks.value = if(selectedFilters.isEmpty()) stockMap.values.toList() else filteredList
        }
    }

    fun applyFilter(filter: String, checked: Boolean) {
        if(checked) selectedFilters.add(filter) else selectedFilters.remove(filter)
        if(selectedFilters.isEmpty())  _stocks.value = stockMap.values.toList()
        else _stocks.value = stockMap.values.filter { it.hasSelectedFilter(selectedFilters) }
    }
}