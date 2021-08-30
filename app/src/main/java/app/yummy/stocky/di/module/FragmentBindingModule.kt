package app.yummy.stocky.di.module

import app.yummy.stocky.di.scopes.FragmentScoped
import app.yummy.stocky.screens.details.DetailFragment
import app.yummy.stocky.screens.home.HomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBindingModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideHomeFragment(): HomeFragment

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideDetaileFragment(): DetailFragment
}