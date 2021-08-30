package app.yummy.stocky.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import app.yummy.stocky.di.viewmodel.ViewModelFactory
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

abstract class BaseFragment<VM : BaseViewModel, DB : ViewDataBinding>(private val layoutId: Int) :
    Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    companion object {
        const val PERMISSION_ALL: Int = 1
    }

    protected val navController by lazy { findNavController() }

    protected lateinit var mViewModel: VM

    protected lateinit var mBinding: DB

    abstract fun getViewModel(): VM

    protected val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                onResult(data)
            }
        }

    protected open fun onResult(data: Intent?) {}

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewModel = getViewModel()
        mBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        setHasOptionsMenu(true)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.lifecycleOwner = this
        mBinding.executePendingBindings()

        mViewModel.init()

        if (!hasPermissions(requireActivity(), requestPermissions())) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                requestPermissions(),
                PERMISSION_ALL
            )
        }
    }

    protected open fun requestPermissions(): Array<String> = arrayOf()

    private fun hasPermissions(context: Context?, permissions: Array<String>?): Boolean {
        if (context != null && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        permission
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

    protected open fun hasPermission(permission: String) {

    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>, grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ALL) {
            grantResults.isEmpty().apply {
                Toast.makeText(requireActivity(), "Permission Denied", Toast.LENGTH_LONG).show()
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
