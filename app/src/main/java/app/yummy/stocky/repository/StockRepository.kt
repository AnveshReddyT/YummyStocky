package app.yummy.stocky.repository

import app.yummy.stocky.model.Stock
import app.yummy.stocky.network.StockAPI
import javax.inject.Inject

class StockRepository @Inject constructor(private val service: StockAPI) {

    suspend fun getStocks(): List<Stock> = service.getStocks()

    suspend fun getStockDetails(stockId: String): Stock = service.getStockDetails(stockId)
}