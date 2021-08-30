package app.yummy.stocky.di.module

import app.yummy.stocky.MainActivity
import app.yummy.stocky.di.scopes.ActivityScoped
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector
    abstract fun provideMainActivity(): MainActivity

}