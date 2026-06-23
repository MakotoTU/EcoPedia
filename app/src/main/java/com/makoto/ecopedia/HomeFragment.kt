package com.makoto.ecopedia

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.lifecycleScope
import com.makoto.ecopedia.data.DatabaseSeeder
import com.makoto.ecopedia.data.EcoPediaDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadCategories(view)
    }

    private fun loadCategories(view: View) {
        val db = EcoPediaDatabase.getInstance(requireContext())
        val dao = db.wasteDao()

        viewLifecycleOwner.lifecycleScope.launch {
            // Seed database on first run
            withContext(Dispatchers.IO) {
                DatabaseSeeder.seedDatabase(dao)
            }

            val entities = dao.getAllCategories()
            val categories = entities.map { entity ->
                val resId = resources.getIdentifier(
                    entity.icon, "drawable", requireContext().packageName
                )
                Category(entity.id, entity.name, if (resId != 0) resId else R.drawable.leaf)
            }

            if (isAdded) {
                val rvCategories = view.findViewById<RecyclerView>(R.id.rvCategories)
                rvCategories.layoutManager = LinearLayoutManager(requireContext())
                rvCategories.adapter = CategoryAdapter(categories) { category ->
                    val intent = Intent(requireContext(), DetailActivity::class.java)
                    intent.putExtra("CATEGORY", category.name)
                    intent.putExtra("CATEGORY_ID", category.id)
                    startActivity(intent)
                }
            }
        }
    }
}
