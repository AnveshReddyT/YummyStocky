package app.yummy.stocky.network

import app.yummy.stocky.model.Stock
import retrofit2.http.GET
import retrofit2.http.Path

interface StockAPI {

    @GET("")
    suspend fun getStocks(): List<Stock>

    @GET("/api/stocks/{id}")
    suspend fun getStockDetails(@Path("id") stockId: String): Stock
}