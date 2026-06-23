package com.makoto.ecopedia

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import coil.load
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.card.MaterialCardView
import androidx.lifecycle.lifecycleScope
import androidx.fragment.app.viewModels
import com.makoto.ecopedia.data.EcoPediaDatabase
import com.makoto.ecopedia.data.api.RetrofitClient
import com.makoto.ecopedia.data.api.OpenFoodFactsResponse
import com.makoto.ecopedia.data.ScanHistoryEntity
import com.makoto.ecopedia.data.ScanHistoryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class ScanResultBottomSheet : BottomSheetDialogFragment() {

    private lateinit var layoutLoading: LinearLayout
    private lateinit var layoutSuccess: View
    private lateinit var layoutError: LinearLayout

    // Success views
    private lateinit var imgProduct: ImageView
    private lateinit var tvProductName: TextView
    private lateinit var tvBarcode: TextView
    private lateinit var cardEcoScore: MaterialCardView
    private lateinit var tvEcoScore: TextView
    private lateinit var tvPackagingMaterial: TextView
    private lateinit var imgCategoryIcon: ImageView
    private lateinit var tvCategoryName: TextView
    private lateinit var tvCategoryDescription: TextView
    private lateinit var tvRecyclingTips: TextView
    private lateinit var btnViewFullCategory: Button

    // Error/Fallback views
    private lateinit var spinnerManualCategory: Spinner
    private lateinit var btnSubmitManual: Button

    private var barcode: String = ""

    companion object {
        private const val ARG_BARCODE = "arg_barcode"

        fun newInstance(barcode: String): ScanResultBottomSheet {
            val fragment = ScanResultBottomSheet()
            val args = Bundle().apply {
                putString(ARG_BARCODE, barcode)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        barcode = arguments?.getString(ARG_BARCODE) ?: ""
    }

    override fun onDismiss(dialog: android.content.DialogInterface) {
        super.onDismiss(dialog)
        val parentFrag = parentFragmentManager.fragments.find { it is ScanFragment } as? ScanFragment
        parentFrag?.resumeScanning()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_scan_result_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        layoutLoading = view.findViewById(R.id.layoutLoading)
        layoutSuccess = view.findViewById(R.id.layoutSuccess)
        layoutError = view.findViewById(R.id.layoutError)

        imgProduct = view.findViewById(R.id.imgProduct)
        tvProductName = view.findViewById(R.id.tvProductName)
        tvBarcode = view.findViewById(R.id.tvBarcode)
        cardEcoScore = view.findViewById(R.id.cardEcoScore)
        tvEcoScore = view.findViewById(R.id.tvEcoScore)
        tvPackagingMaterial = view.findViewById(R.id.tvPackagingMaterial)
        imgCategoryIcon = view.findViewById(R.id.imgCategoryIcon)
        tvCategoryName = view.findViewById(R.id.tvCategoryName)
        tvCategoryDescription = view.findViewById(R.id.tvCategoryDescription)
        tvRecyclingTips = view.findViewById(R.id.tvRecyclingTips)
        btnViewFullCategory = view.findViewById(R.id.btnViewFullCategory)

        spinnerManualCategory = view.findViewById(R.id.spinnerManualCategory)
        btnSubmitManual = view.findViewById(R.id.btnSubmitManual)

        setupManualSelectionSpinner()
        fetchProductData()
    }

    private fun setupManualSelectionSpinner() {
        val categories = arrayOf(
            "Plastik",
            "Kertas",
            "Kaca",
            "Organik",
            "B3 (Bahan Berbahaya)",
            "Logam"
        )
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            categories
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerManualCategory.adapter = adapter
    }

    private fun fetchProductData() {
        if (barcode.isEmpty()) {
            showErrorState("Barcode Kosong", "Kode barcode tidak valid atau kosong.")
            return
        }

        layoutLoading.visibility = View.VISIBLE
        layoutSuccess.visibility = View.GONE
        layoutError.visibility = View.GONE

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // Network call
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.instance.getProduct(barcode)
                }
                
                if (response.status == 1 && response.product != null) {
                    bindProductData(response)
                } else {
                    showErrorState("Produk Tidak Ditemukan", "Barcode $barcode tidak terdaftar di database Open Food Facts.")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                showErrorState("Masalah Koneksi", "Gagal menghubungi server. Periksa koneksi internet Anda.")
            }
        }
    }

    private fun bindProductData(response: OpenFoodFactsResponse) {
        val product = response.product ?: return
        
        layoutLoading.visibility = View.GONE
        layoutSuccess.visibility = View.VISIBLE
        layoutError.visibility = View.GONE

        // Bind Basic Product info
        tvProductName.text = product.productName ?: "Produk Tanpa Nama"
        tvBarcode.text = "Barcode: $barcode"
        imgProduct.load(product.imageUrl) {
            placeholder(R.drawable.leaf)
            error(R.drawable.leaf)
        }

        // Bind Eco-Score
        val grade = product.ecoscoreGrade?.lowercase() ?: "unknown"
        if (grade != "unknown") {
            cardEcoScore.visibility = View.VISIBLE
            tvEcoScore.text = "Eco-Score: ${grade.uppercase()}"
            val colorHex = when (grade) {
                "a", "b" -> "#4CAF50" // Hijau
                "c", "d" -> "#FF9800" // Jingga
                "e" -> "#F44336" // Merah
                else -> "#9E9E9E" // Abu-abu
            }
            cardEcoScore.setCardBackgroundColor(Color.parseColor(colorHex))
        } else {
            cardEcoScore.visibility = View.GONE
        }

        // Bind Packaging text
        val packagingText = product.packaging?.text ?: "Tidak tersedia"
        tvPackagingMaterial.text = "Kemasan: $packagingText"

        // Map to Room WasteCategory
        val mappedCategoryId = mapPackagingToCategoryId(product.packagingMaterialsTags, packagingText)
        
        loadWasteCategoryDetails(mappedCategoryId)
        
        // Save scan history
        saveScanHistory(
            productName = product.productName ?: "Produk Tanpa Nama",
            ecoScore = product.ecoscoreGrade?.uppercase() ?: "UNKNOWN",
            imageUrl = product.imageUrl,
            categoryId = mappedCategoryId
        )
    }

    private fun mapPackagingToCategoryId(materials: List<String>?, text: String?): Int {
        val searchString = (materials?.joinToString(" ") ?: "") + " " + (text ?: "")
        val lower = searchString.lowercase()

        return when {
            lower.contains("plastic") || lower.contains("pet") || lower.contains("pe-") || 
            lower.contains("pp-") || lower.contains("pvc") || lower.contains("ldpe") || 
            lower.contains("hdpe") || lower.contains("polystyrene") || lower.contains("plastik") ||
            lower.contains("kresek") || lower.contains("styrofoam") || lower.contains("sachet") -> 1 // Plastik

            lower.contains("paper") || lower.contains("cardboard") || lower.contains("carton") || 
            lower.contains("kertas") || lower.contains("kardus") || lower.contains("karton") || 
            lower.contains("buku") || lower.contains("box") -> 2 // Kertas

            lower.contains("glass") || lower.contains("kaca") || lower.contains("botol kaca") || 
            lower.contains("gelas") || lower.contains("cermin") -> 3 // Kaca

            lower.contains("metal") || lower.contains("aluminum") || lower.contains("aluminium") || 
            lower.contains("steel") || lower.contains("tin") || lower.contains("iron") || 
            lower.contains("kaleng") || lower.contains("logam") || lower.contains("seng") ||
            lower.contains("tembaga") || lower.contains("kawat") || lower.contains("paku") -> 6 // Logam

            lower.contains("organic") || lower.contains("food") || lower.contains("daun") || 
            lower.contains("buah") || lower.contains("sisa") || lower.contains("organik") || 
            lower.contains("sayur") -> 4 // Organik

            lower.contains("battery") || lower.contains("baterai") || lower.contains("chemical") || 
            lower.contains("toxic") || lower.contains("b3") || lower.contains("pestisida") || 
            lower.contains("racun") || lower.contains("lampu neon") || lower.contains("obat") -> 5 // B3

            else -> -1 // Tidak dikenali
        }
    }

    private fun saveScanHistory(productName: String, ecoScore: String, imageUrl: String?, categoryId: Int) {
        val finalCategoryId = if (categoryId == -1) null else categoryId
        
        // Ambil context dan path sebelum masuk background coroutine agar aman meski fragment keburu ditutup
        val appContext = requireContext().applicationContext
        val filesDirPath = requireContext().filesDir.absolutePath
        
        CoroutineScope(Dispatchers.IO).launch {
            var localImagePath: String? = null

            // Download image to local storage as per design doc
            if (!imageUrl.isNullOrEmpty()) {
                try {
                    val url = URL(imageUrl)
                    val connection = url.openConnection()
                    connection.connectTimeout = 5000
                    connection.readTimeout = 5000
                    val inputStream = connection.getInputStream()
                    
                    val filename = "scan_${System.currentTimeMillis()}.jpg"
                    val file = File(filesDirPath, filename)
                    FileOutputStream(file).use { output ->
                        inputStream.copyTo(output)
                    }
                    localImagePath = file.absolutePath
                } catch (e: Exception) {
                    e.printStackTrace()
                    // Fallback to URL if download fails
                    localImagePath = imageUrl
                }
            }

            val entity = ScanHistoryEntity(
                barcode = barcode,
                productName = productName,
                categoryId = finalCategoryId,
                ecoScore = ecoScore,
                imagePath = localImagePath,
                scannedAt = System.currentTimeMillis()
            )
            
            val db = EcoPediaDatabase.getInstance(appContext)
            db.scanHistoryDao().insertScan(entity)
        }
    }

    private fun loadWasteCategoryDetails(categoryId: Int) {
        if (categoryId == -1) {
            // Fallback: Show manual selection spinner even if product is found but materials are unknown
            showErrorState(
                "Bahan Kemasan Tidak Diketahui",
                "Kami berhasil menemukan produk, tetapi tipe kemasannya belum dianalisis. Pilih jenis kemasan manual:"
            )
            return
        }

        val appContext = context?.applicationContext ?: return

        viewLifecycleOwner.lifecycleScope.launch {
            val db = EcoPediaDatabase.getInstance(appContext)
            val category = db.wasteDao().getCategoryById(categoryId)

            if (category != null) {
                tvCategoryName.text = "Kategori: ${category.name}"
                tvCategoryDescription.text = category.description
                tvRecyclingTips.text = category.recyclingTips

                val resId = appContext.resources.getIdentifier(
                    category.icon, "drawable", appContext.packageName
                )
                if (resId != 0) {
                    imgCategoryIcon.setImageResource(resId)
                } else {
                    imgCategoryIcon.setImageResource(R.drawable.leaf)
                }

                btnViewFullCategory.setOnClickListener {
                    dismiss()
                    val intent = Intent(context, DetailActivity::class.java).apply {
                        putExtra("CATEGORY_ID", category.id)
                        putExtra("CATEGORY", category.name)
                    }
                    startActivity(intent)
                }
            } else {
                showErrorState("Gagal Memuat Kategori", "Kategori sampah tidak ditemukan di database lokal.")
            }
        }
    }

    private fun showErrorState(title: String, description: String) {
        layoutLoading.visibility = View.GONE
        layoutSuccess.visibility = View.GONE
        layoutError.visibility = View.VISIBLE

        val tvErrorTitle: TextView = view?.findViewById(R.id.tvErrorTitle) ?: return
        val tvErrorDescription: TextView = view?.findViewById(R.id.tvErrorDescription) ?: return
        
        tvErrorTitle.text = title
        tvErrorDescription.text = description

        btnSubmitManual.setOnClickListener {
            // Spinner position mapping: 0 -> Plastik (1), 1 -> Kertas (2), 2 -> Kaca (3), 3 -> Organik (4), 4 -> B3 (5), 5 -> Logam (6)
            val selectedPosition = spinnerManualCategory.selectedItemPosition
            val manualCategoryId = when (selectedPosition) {
                0 -> 1 // Plastik
                1 -> 2 // Kertas
                2 -> 3 // Kaca
                3 -> 4 // Organik
                4 -> 5 // B3
                5 -> 6 // Logam
                else -> 1
            }
            
            // Open DetailActivity directly for manual selection
            val appContext = context?.applicationContext ?: return@setOnClickListener
            dismiss()
            viewLifecycleOwner.lifecycleScope.launch {
                val db = EcoPediaDatabase.getInstance(appContext)
                val category = db.wasteDao().getCategoryById(manualCategoryId)
                if (category != null) {
                    val intent = Intent(appContext, DetailActivity::class.java).apply {
                        putExtra("CATEGORY_ID", category.id)
                        putExtra("CATEGORY", category.name)
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    appContext.startActivity(intent)
                }
            }
        }
    }
}
