package app.yummy.stocky.base

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import app.yummy.stocky.di.viewmodel.ViewModelFactory
import dagger.android.AndroidInjection
import javax.inject.Inject

abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding>(val layout: Int) :
    AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    protected lateinit var mViewModel: VM

    protected lateinit var mBinding: DB

    abstract fun getViewModel(): VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        mViewModel = getViewModel()
        mBinding = DataBindingUtil.setContentView(this, layout)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        mBinding.lifecycleOwner = this
        mBinding.executePendingBindings()

        mViewModel.init()

        if (!hasPermissions(this, requestPermissions())) {
            ActivityCompat.requestPermissions(
                this, requestPermissions(), BaseFragment.PERMISSION_ALL
            )
        }
    }

    protected open fun requestPermissions(): Array<String> = arrayOf()

    protected open fun hasPermission(permission: String) {

    }

    private fun hasPermissions(context: Context?, permissions: Array<String>?): Boolean {
        if (context != null && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        context, permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                } else {
                    hasPermission(permission)
                }
            }
        }
        return true
    }
}