package app.yummy.stocky

import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import app.yummy.stocky.base.BaseActivity
import app.yummy.stocky.databinding.ActivityMainBinding
import app.yummy.stocky.screens.home.HomeViewModel

class MainActivity : BaseActivity<HomeViewModel, ActivityMainBinding>(R.layout.activity_main) {

    private val navController: NavController by lazy { findNavController(R.id.nav_host_fragment) }

    override fun getViewModel(): HomeViewModel = ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]

    private val appBarConfiguration: AppBarConfiguration by lazy {
        AppBarConfiguration(setOf(R.id.navigation_home))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item)
    }

}