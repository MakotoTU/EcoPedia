package com.makoto.ecopedia

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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
        setupRecyclerView(view)
    }

    private fun setupRecyclerView(view: View) {
        val categories = listOf(
            Category(1, "Plastik", R.drawable.plastik),
            Category(2, "Kertas", R.drawable.kertas),
            Category(3, "Kaca", R.drawable.kaca),
            Category(4, "Organik", R.drawable.organik),
            Category(5, "B3", R.drawable.b3),
            Category(6, "Logam", R.drawable.logam)
        )

        val rvCategories = view.findViewById<RecyclerView>(R.id.rvCategories)
        rvCategories.layoutManager = LinearLayoutManager(requireContext())
        rvCategories.adapter = CategoryAdapter(categories) { category ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("CATEGORY", category.name)
            startActivity(intent)
        }
    }
}
