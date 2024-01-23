import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stockportfoliotracker.R

class YearListAdapter(
    val context: Context,
    val yearList: List<String>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class YearViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val filterText: TextView = itemView.findViewById(R.id.filterText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.sort_filter_item, parent, false)
        return YearViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is YearViewHolder) {
            val year = yearList[position]
            holder.filterText.text = year

            // Handle click or other actions if needed
            holder.itemView.setOnClickListener {
                // Handle the item click if needed
            }
        }
    }

    override fun getItemCount(): Int {
        return yearList.size
    }
}
