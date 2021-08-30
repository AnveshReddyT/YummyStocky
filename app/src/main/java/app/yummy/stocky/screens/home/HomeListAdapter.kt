package app.yummy.stocky.screens.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import app.yummy.stocky.R
import app.yummy.stocky.base.BaseRecyclerAdapter
import app.yummy.stocky.base.BaseViewHolder
import app.yummy.stocky.databinding.ItemStockBinding
import app.yummy.stocky.model.Stock

class HomeListAdapter(private val context: Context) : BaseRecyclerAdapter<Stock>(context) {

    var onItemClicked: (id: String) -> Unit = { }

    override fun getViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder =
        MenuItemViewModel(
            DataBindingUtil.inflate(
                LayoutInflater.from(context), R.layout.item_stock, parent, false
            )
        )

    override fun getItemViewType(position: Int): Int = position

    inner class MenuItemViewModel(private val binding: ItemStockBinding) : BaseViewHolder(binding) {
        override fun onBind(position: Int) {
            val item = getItem(position)
            this.binding.apply {
                dataModel = item
                try {
                    val drawable = when (item.trend) {
                        1 -> R.drawable.ic_baseline_arrow_upward_24
                        -1 -> R.drawable.ic_baseline_arrow_downward_24
                        else -> 0
                    }
                    price.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable, 0)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                root.setOnClickListener { onItemClicked(item.id) }
            }
        }
    }

    override fun filterData(query: CharSequence?) = getBaseItems().filter {
        it.name.lowercase().startsWith(query.toString().lowercase())
    }

}