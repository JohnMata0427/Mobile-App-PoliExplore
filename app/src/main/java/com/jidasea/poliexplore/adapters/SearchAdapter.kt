
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jidasea.poliexplore.BuildingDetailsFragment
import com.jidasea.poliexplore.R
import com.jidasea.poliexplore.models.Search

class SearchAdapter(private var results: List<Search>,
                    private val fragmentManager: androidx.fragment.app.FragmentManager) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val numeroEdificio: TextView = itemView.findViewById(R.id.numeroEdificio)
        val nombreEdificio: TextView = itemView.findViewById(R.id.nombreEdificio)
        val navigateButton: LinearLayout = itemView.findViewById(R.id.navigateButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_result, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.numeroEdificio.text = "Edificio ${result.id}"
        holder.nombreEdificio.text = result.name
        holder.navigateButton.setOnClickListener {
            val fragment = BuildingDetailsFragment.newInstance(result.id)
            fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment) // Asegúrate de que "fragment_container" es el ID del contenedor de Fragment en tu actividad
                .addToBackStack(null)
                .commit()
        }
    }

    override fun getItemCount(): Int = results.size

    fun updateResults(newResults: List<Search>) {
        results = newResults
        notifyDataSetChanged()
    }
}