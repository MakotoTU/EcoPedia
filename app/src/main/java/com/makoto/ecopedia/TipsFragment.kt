package com.makoto.ecopedia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class TipsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tips, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup click listeners for tip cards
        view.findViewById<View>(R.id.tipPotBotol)?.setOnClickListener {
            showTipDetail(
                category = "DIY & Kerajinan",
                title = "Cara Membuat Pot dari Botol Bekas",
                content = "Botol plastik bekas air mineral atau minuman ringan sangat sulit terurai secara alami, memakan waktu hingga ratusan tahun. Daripada menumpuk di tempat sampah, botol-botol ini bisa disulap menjadi barang berguna seperti pot tanaman.\n\nCara membuat:\n1. Siapkan botol plastik kosong, gunting/cutter, paku (untuk melubangi), cat (opsional), dan media tanam.\n2. Potong botol plastik menjadi dua bagian. Anda bisa menggunakan bagian bawah untuk pot standar atau bagian atas yang dibalik untuk pot model gantung.\n3. Beri lubang kecil di bagian bawah potongan botol menggunakan paku yang dipanaskan agar air siraman tanaman tidak menggenang dan membuat akar busuk.\n4. (Opsional) Warnai bagian luar botol dengan cat air atau cat akrilik agar terlihat lebih estetik.\n5. Masukkan tanah dan tanaman, lalu siram dengan air secukupnya.",
                imageResId = R.drawable.plastik
            )
        }

        view.findViewById<View>(R.id.tipEcobrick)?.setOnClickListener {
            showTipDetail(
                category = "Inovasi Daur Ulang",
                title = "Mengenal Ecobrick: Solusi Sampah Plastik",
                content = "Ecobrick adalah bata ramah lingkungan yang dibuat dengan cara memasukkan plastik-plastik bekas ke dalam botol plastik bekas hingga padat dan keras. Metode ini tidak menghancurkan plastik, melainkan 'memenjarakannya' agar tidak mencemari lingkungan.\n\nCara membuat:\n1. Kumpulkan sampah plastik lembut seperti bungkus makanan, kantong kresek, dan sedotan. Pastikan semuanya dalam keadaan bersih dan kering.\n2. Siapkan botol plastik utuh berukuran 600ml atau 1.5L.\n3. Masukkan sampah plastik ke dalam botol sedikit demi sedikit.\n4. Gunakan tongkat kayu atau bambu untuk menekan sampah plastik tersebut hingga benar-benar padat dan tidak ada rongga udara.\n5. Botol harus diisi sampai benar-benar keras (tidak penyok saat ditekan). Rata-rata botol 600ml membutuhkan sekitar 200-250 gram plastik.\n6. Kumpulkan botol-botol ini dan rangkai menjadi kursi, meja, atau bahkan dinding bangunan sederhana.",
                imageResId = R.drawable.plastik
            )
        }

        view.findViewById<View>(R.id.tipUmurSampah)?.setOnClickListener {
            showTipDetail(
                category = "Edukasi Lingkungan",
                title = "Berapa Lama Sampah Terurai di Alam?",
                content = "Banyak dari kita membuang sampah sembarangan tanpa menyadari berapa lama benda tersebut akan bertahan di bumi. Memahami umur sampah adalah langkah pertama untuk lebih bijak mengelola barang bekas.\n\nWaktu urai berbagai jenis sampah:\n- Kertas/Karton: 2 hingga 6 minggu. Mudah terurai, tapi sangat baik jika didaur ulang.\n- Kulit Buah/Sayur: 1 bulan. Paling baik diolah menjadi kompos.\n- Kantong Plastik (Kresek): 10 hingga 20 tahun. Sering pecah menjadi mikroplastik yang termakan hewan laut.\n- Kaleng Aluminium: 80 hingga 200 tahun. Dapat didaur ulang berkali-kali tanpa mengurangi kualitasnya.\n- Popok Sekali Pakai: 250 hingga 500 tahun. Sangat merusak lingkungan jika dibuang ke laut atau sungai.\n- Botol Plastik: 450 tahun. Bahan PET sangat kuat dan persentase daur ulangnya masih rendah.\n- Kaca: 1 Juta Tahun (atau tidak akan terurai sama sekali). Namun, kaca adalah material yang 100% dapat didaur ulang tanpa batas.",
                imageResId = R.drawable.kaca
            )
        }
    }

    private fun showTipDetail(category: String, title: String, content: String, imageResId: Int) {
        val bottomSheet = TipDetailBottomSheet.newInstance(category, title, content, imageResId)
        bottomSheet.show(childFragmentManager, "TipDetailBottomSheet")
    }
}
