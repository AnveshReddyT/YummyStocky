package app.yummy.stocky.screens.details

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import app.yummy.stocky.base.BaseViewModel
import app.yummy.stocky.model.Stock
import app.yummy.stocky.repository.StockRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

class DetailsViewModel @Inject constructor(private val stockRepository: StockRepository, application: Application) :
    BaseViewModel(application) {

    private val _stock: MutableLiveData<Stock> = MutableLiveData()

    val stock: LiveData<Stock> = _stock

    override fun init() {
    }

    fun goToWebsite() {
        stock.value!!.website?.apply {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse(this)
            val intent = Intent(Intent.ACTION_VIEW,openURL.data)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    fun fetchStockDetails(id: String) {
        viewModelScope.launch {
            try {
                isLoading.set(true)
                val response = stockRepository.getStockDetails(id)
                _stock.postValue(response)
            } catch (e: Exception) {
                when (e) {
                    is HttpException -> Toast.makeText(
                        context, e.message(), Toast.LENGTH_LONG
                    ).show()
                }
            } finally {
                isLoading.set(false)
            }
        }
    }
}