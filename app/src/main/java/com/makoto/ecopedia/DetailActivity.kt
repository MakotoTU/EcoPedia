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

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail)

        val category = intent.getStringExtra("CATEGORY") ?: "Plastik"
        
        setupUI(category)
        setupEdgeToEdge()

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }
    }

    private fun setupUI(category: String) {
        val tvTitleHeader = findViewById<TextView>(R.id.tvTitleHeader)
        val tvHeroTitle = findViewById<TextView>(R.id.tvHeroTitle)
        val tvDescription = findViewById<TextView>(R.id.tvDescription)
        val ivHeroImage = findViewById<ImageView>(R.id.ivHeroImage)

        tvTitleHeader.text = category
        tvHeroTitle.text = "Sampah $category"

        // Map category to real image from Desktop assets
        val imageRes = when (category) {
            "Plastik" -> R.drawable.plastik
            "Kertas" -> R.drawable.kertas
            "Kaca" -> R.drawable.kaca
            "Organik" -> R.drawable.organik
            "B3" -> R.drawable.b3
            "Logam" -> R.drawable.logam
            else -> R.drawable.leaf
        }
        ivHeroImage.setImageResource(imageRes)

        // Data based on Desktop Design (Latest Source of Truth)
        when (category) {
            "Plastik" -> {
                tvDescription.text = "Sampah plastik adalah semua barang bekas atau material yang diproduksi dari bahan kimia tak terbarukan. Jenis sampah ini sangat sulit terurai secara alami dan dapat mencemari lingkungan jika tidak dikelola dengan benar."
            }
            "Kertas" -> {
                tvDescription.text = "Sampah kertas adalah limbah yang berasal dari berbagai jenis kertas yang sudah tidak digunakan lagi, seperti koran, majalah, kardus, dan kertas bekas kantor. Jenis limbah ini sangat umum dihasilkan dari aktivitas sehari-hari, tetapi jika tidak dikelola dengan baik, dapat menimbulkan masalah lingkungan."
            }
            "Kaca" -> {
                tvDescription.text = "Sampah kaca adalah limbah anorganik yang berasal dari produk berbahan kaca seperti botol, toples, atau pecahan gelas. Kaca merupakan material unik yang dapat didaur ulang 100% berulang kali tanpa penurunan kualitas, namun butuh waktu jutaan tahun untuk terurai di alam jika dibuang sembarangan."
            }
            "Organik" -> {
                tvDescription.text = "Sampah organik adalah limbah yang berasal dari sisa makhluk hidup (tanaman atau hewan) dan mudah terurai secara alami oleh mikroorganisme. Meskipun bisa membusuk sendiri, sampah ini sebaiknya diolah menjadi kompos agar tidak menumpuk di TPA dan menghasilkan gas metana yang berbahaya."
            }
            "B3" -> {
                tvDescription.text = "Sampah B3 (Bahan Berbahaya dan Beracun) adalah limbah yang mengandung zat kimia berbahaya yang dapat merusak lingkungan dan kesehatan manusia secara serius. Jenis sampah ini SANGAT DILARANG untuk dibuang sembarangan atau dicampur dengan sampah rumah tangga biasa karena sifatnya yang mudah meledak, terbakar, reaktif, atau korosif."
            }
            "Logam" -> {
                tvDescription.text = "Sampah logam adalah limbah anorganik yang terbuat dari bahan tambang seperti besi, baja, atau aluminium. Material ini sangat bernilai karena dapat didaur ulang dan dilebur kembali berkali-kali menjadi produk baru tanpa mengurangi kualitas aslinya secara signifikan."
            }
            else -> {
                tvDescription.text = "Informasi detail mengenai sampah $category akan segera hadir."
            }
        }
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