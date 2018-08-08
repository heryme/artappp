package arvaan.dowanloaddemo.comman

import android.util.Log
import android.widget.Filter
import com.artproficiencyapp.test.expandable_list_view.adapter.ThirdCategoriesAdapter
import com.artproficiencyapp.test.expandable_list_view.model.CategoryModel


/**
 * Created by admin on 12-Dec-17.
 */
class SearchFilter(private val filteredCardList: List<CategoryModel.Category_?>, private val adapter: ThirdCategoriesAdapter) : Filter() {
    override fun performFiltering(charSequence: CharSequence?): Filter.FilterResults {
        var charSequence = charSequence
        val filterResults = Filter.FilterResults()

        if (charSequence != null && charSequence.isNotEmpty()) {
            charSequence = charSequence.toString().toLowerCase()
            val filteredCards = ArrayList<CategoryModel.Category_?>()

            for (i in filteredCardList.indices) {
                if (filteredCardList[i]?.name!!.toLowerCase().contains(charSequence)) {
                    filteredCards.add(filteredCardList[i])
                    Log.e("","Valueee"+filteredCardList[i])
                }
            }

            filterResults.count = filteredCards.size
            filterResults.values = filteredCards
        } else {
            filterResults.count = filteredCardList.size
            filterResults.values = filteredCardList
        }

        return filterResults
    }

    override fun publishResults(charSequence: CharSequence, filterResults: Filter.FilterResults) {
        adapter.subCategory = filterResults.values as  ArrayList<CategoryModel.Category_?>
        adapter.notifyDataSetChanged()
    }
}