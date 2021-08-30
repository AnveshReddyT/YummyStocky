package app.yummy.stocky.model

data class Stock(
    val id: String,
    val name: String,
    var price: Double,
    val companyType: ArrayList<String>,
    val allTimeHigh: Double,
    val address: String,
    val imageUrl: String,
    val website: String?,
    var trend: Int = 0
) {
    override fun equals(other: Any?) = id == (other as Stock).id

    override fun hashCode() = id.hashCode()

    fun hasSelectedFilter(filters: List<String>): Boolean {
        companyType.forEach {
            if(filters.contains(it)) return true
        }
        return false
    }
}