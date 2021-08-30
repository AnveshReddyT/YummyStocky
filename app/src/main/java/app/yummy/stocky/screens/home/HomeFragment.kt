package app.yummy.stocky.screens.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import app.yummy.stocky.BuildConfig
import app.yummy.stocky.R
import app.yummy.stocky.base.BaseFragment
import app.yummy.stocky.databinding.FragmentHomeBinding
import com.google.android.material.chip.Chip
import com.neovisionaries.ws.client.WebSocket
import com.neovisionaries.ws.client.WebSocketAdapter
import io.socket.client.Socket
import org.json.JSONArray
import com.neovisionaries.ws.client.WebSocketFactory

import com.neovisionaries.ws.client.HostnameUnverifiedException

import com.neovisionaries.ws.client.OpeningHandshakeException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>(R.layout.fragment_home) {

    private lateinit var ws: WebSocket

    override fun getViewModel(): HomeViewModel = ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = HomeListAdapter(requireContext())
        try {
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                initialiseSocket()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mBinding.viewModel = mViewModel
        mBinding.stocksList.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        adapter.onItemClicked = { onItemClicked(it) }

        mViewModel.stocks.observe(viewLifecycleOwner) { it ->
            val filter = it.filter {
                it.name.lowercase().startsWith(mBinding.searchView.query.toString().lowercase())
            }
            adapter.addItems(filter)
        }
        mBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter.filter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })

        mViewModel.companyTypesUpdated.observe(viewLifecycleOwner,
            {
//                mBinding.filterGroup.removeAllViews()
                mViewModel.companyTypes.forEach {
                    val chip = LayoutInflater.from(context).inflate(R.layout.chip_filter, mBinding.filterGroup, false) as Chip
                    chip.text = it
                    if(mViewModel.selectedFilters.contains(it)) { chip.isChecked = true }
                    chip.setOnCheckedChangeListener { _, isChecked ->
                        mViewModel.applyFilter(it, isChecked)
                        mBinding.searchView.apply {
                            setQuery("", true)
                            clearFocus()
                        }
                    }
                    mBinding.filterGroup.addView(chip)
                }
            }
        )
    }

    private fun onItemClicked(id: String) {
        navController.navigate(HomeFragmentDirections.navigateToDetails(id))
    }


    private fun initialiseSocket() {
        try {
            ws = WebSocketFactory().createSocket(BuildConfig.WSS_URL)

            ws.addListener(object :WebSocketAdapter() {
                override fun onTextMessage(websocket: WebSocket?, text: String?) {
                    super.onTextMessage(websocket, text)
               text?.let {
                   if (text.isNotEmpty()) {
                       mViewModel.updateStocksData(JSONArray(text))
                   }
                 }
               }
            });

            try {
                ws.connect()
            } catch (e: OpeningHandshakeException) {
                // A violation against the WebSocket protocol was detected
                e.printStackTrace()
                // during the opening handshake.
            } catch (e: HostnameUnverifiedException) {
                e.printStackTrace()
                // The certificate of the peer does not match the expected hostname.
            } catch (e: Exception) {
                e.printStackTrace()
                // Failed to establish a WebSocket connection.
            }
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        ws.disconnect()
    }

}
