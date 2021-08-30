package app.yummy.stocky.base

import android.content.Context
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerAdapter<T>(private val context: Context) : RecyclerView.Adapter<BaseViewHolder>(),
    Filterable {

    protected var filteredItems: MutableList<T> = ArrayList()

    private var items: MutableList<T> = ArrayList()

    abstract fun getViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
        getViewHolder(parent, viewType)

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int {
        return filteredItems.size
    }

    open fun clearItems() {
        this.items.clear()
        this.filteredItems = this.items
        notifyDataSetChanged()
    }

    open fun addItems(items: List<T>) {
        this.items.clear()
        this.items.addAll(items)
        this.filteredItems = this.items
        notifyDataSetChanged()
    }

    open fun addItem(item: T, position: Int) {
        if (position == -1) {
            this.items.add(item)
            this.filteredItems = this.items
            notifyItemInserted(items.size - 1)
        } else {
            this.items.add(position, item)
            this.filteredItems = this.items
            notifyItemInserted(position)
        }
    }

    fun getItems(): List<T> {
        return filteredItems
    }

    fun getBaseItems(): List<T> {
        return items
    }

    open fun getItem(position: Int): T {
        return filteredItems[position]
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                filterResults.values = filterData(constraint)
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredItems = results?.values as ArrayList<T>
                notifyDataSetChanged()
            }

        }
    }

    open fun filterData(query: CharSequence?): List<T> = filteredItems
}
