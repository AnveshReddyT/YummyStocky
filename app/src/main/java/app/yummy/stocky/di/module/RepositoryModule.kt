package app.yummy.stocky.di.module

import app.yummy.stocky.network.StockAPI
import app.yummy.stocky.repository.StockRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideStockRepository(stockAPI: StockAPI) = StockRepository(stockAPI)
}