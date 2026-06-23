package com.makoto.ecopedia

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import coil.load
import com.makoto.ecopedia.data.EcoPediaDatabase
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail)

        setupEdgeToEdge()

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }

        loadData()
    }

    private fun loadData() {
        val categoryId = intent.getIntExtra("CATEGORY_ID", -1)
        val categoryName = intent.getStringExtra("CATEGORY") ?: "Plastik"

        val db = EcoPediaDatabase.getInstance(this)
        val dao = db.wasteDao()

        lifecycleScope.launch {
            val category = if (categoryId != -1) {
                dao.getCategoryById(categoryId)
            } else {
                dao.getCategoryByName(categoryName)
            }

            val examples = category?.let { dao.getExamplesByCategoryId(it.id) } ?: emptyList()

            if (category != null) {
                setupUI(category.name, category.icon, category.description,
                    category.characteristics, category.impact, category.recyclingTips, examples)
            } else {
                setupFallback(categoryName)
            }
        }
    }

    private fun setupUI(
        name: String,
        icon: String,
        description: String,
        characteristics: String,
        impact: String,
        recyclingTips: String,
        examples: List<com.makoto.ecopedia.data.WasteExampleEntity>
    ) {
        val tvTitleHeader = findViewById<TextView>(R.id.tvTitleHeader)
        val tvHeroTitle = findViewById<TextView>(R.id.tvHeroTitle)
        val tvDescription = findViewById<TextView>(R.id.tvDescription)
        val tvCharacteristics = findViewById<TextView>(R.id.tvCharacteristics)
        val tvImpact = findViewById<TextView>(R.id.tvImpact)
        val tvRecyclingTips = findViewById<TextView>(R.id.tvRecyclingTips)
        val tvExamples = findViewById<TextView>(R.id.tvExamples)
        val ivHeroImage = findViewById<ImageView>(R.id.ivHeroImage)

        tvTitleHeader.text = name
        tvHeroTitle.text = "Sampah $name"

        val imageRes = resources.getIdentifier(icon, "drawable", packageName)
        ivHeroImage.load(if (imageRes != 0) imageRes else R.drawable.leaf)

        tvDescription.text = description
        tvCharacteristics.text = characteristics
        tvImpact.text = impact
        tvRecyclingTips.text = recyclingTips

        // Build examples text
        if (examples.isNotEmpty()) {
            val examplesText = examples.joinToString("\n\n") { example ->
                "\u2022 ${example.name}\n  ${example.description}\n  \u23F3 Waktu terurai: ${example.decompositionTime}"
            }
            tvExamples.text = examplesText
        } else {
            tvExamples.text = "Belum ada data contoh."
        }
    }

    private fun setupFallback(categoryName: String) {
        findViewById<TextView>(R.id.tvTitleHeader).text = categoryName
        findViewById<TextView>(R.id.tvHeroTitle).text = "Sampah $categoryName"
        findViewById<TextView>(R.id.tvDescription).text = "Informasi detail mengenai sampah $categoryName akan segera hadir."
    }

    private fun setupEdgeToEdge() {
        val topBar = findViewById<View>(R.id.topBarContent)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.detailScrollView)) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            topBar.setPadding(
                topBar.paddingLeft,
                systemBars.top,
                topBar.paddingRight,
                topBar.paddingBottom
            )
            insets
        }
    }
}