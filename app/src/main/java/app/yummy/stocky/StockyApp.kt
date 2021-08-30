package app.yummy.stocky

import android.app.Application
import app.yummy.stocky.di.module.*
import app.yummy.stocky.di.viewmodel.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        ActivityBindingModule::class,
        FragmentBindingModule::class,
        AppModule::class,
        NetworkModule::class,
        ViewModelModule::class,
        RepositoryModule::class]
)
interface AppComponent : AndroidInjector<StockyApp> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}

class StockyApp : DaggerApplication() {

    lateinit var appComponent: AppComponent

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        appComponent = DaggerAppComponent.builder().application(this)
            .build()
        return appComponent
    }

}
