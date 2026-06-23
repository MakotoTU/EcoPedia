package com.makoto.ecopedia.data

object DatabaseSeeder {

    suspend fun seedDatabase(dao: WasteDao) {
        if (dao.getCategoryCount() > 0) return

        val categories = listOf(
            WasteCategoryEntity(
                id = 1,
                name = "Plastik",
                icon = "plastik",
                description = "Sampah plastik adalah semua barang bekas atau material yang diproduksi dari bahan kimia tak terbarukan (polimer sintetis). Plastik dibuat dari minyak bumi melalui proses polimerisasi dan memiliki sifat yang sangat tahan lama sehingga sulit terurai secara alami. Jenis plastik yang umum ditemukan antara lain PET (Polyethylene Terephthalate), HDPE (High-Density Polyethylene), PVC, LDPE, PP, dan PS (Styrofoam).",
                characteristics = "• Sangat ringan namun kuat dan tahan lama\n• Tidak mudah terurai secara alami (membutuhkan 100-1000 tahun)\n• Tahan terhadap air dan bahan kimia\n• Mudah dibentuk menjadi berbagai produk\n• Beberapa jenis melepaskan zat berbahaya saat dipanaskan\n• Dapat terurai menjadi mikroplastik yang mencemari tanah dan air",
                impact = "• Mencemari lautan — lebih dari 8 juta ton plastik masuk ke laut setiap tahun\n• Mengancam kehidupan satwa liar — hewan laut sering salah makan plastik\n• Mikroplastik masuk ke rantai makanan manusia\n• Pembakaran plastik menghasilkan dioksin dan furan yang bersifat karsinogenik\n• Menumpuk di TPA karena tidak terurai\n• Menyumbat saluran air dan menyebabkan banjir",
                recyclingTips = "• Pisahkan berdasarkan kode resin (segitiga angka 1-7)\n• Bersihkan dari sisa makanan sebelum didaur ulang\n• Kurangi penggunaan plastik sekali pakai (bawa tumbler & tas belanja sendiri)\n• Plastik PET (kode 1) dan HDPE (kode 2) paling mudah didaur ulang\n• Serahkan ke bank sampah atau pengepul untuk didaur ulang\n• Hindari membakar plastik karena menghasilkan gas beracun"
            ),
            WasteCategoryEntity(
                id = 2,
                name = "Kertas",
                icon = "kertas",
                description = "Sampah kertas adalah limbah yang berasal dari berbagai jenis kertas yang sudah tidak digunakan lagi. Kertas diproduksi dari serat selulosa yang diperoleh dari kayu melalui proses pulping. Meskipun bersifat biodegradable, produksi kertas baru membutuhkan penebangan pohon dalam jumlah besar, sehingga daur ulang kertas sangat penting untuk menjaga kelestarian hutan.",
                characteristics = "• Mudah terurai secara alami (2-6 minggu untuk kertas tipis)\n• Mudah menyerap air dan kelembapan\n• Dapat didaur ulang hingga 5-7 kali sebelum seratnya terlalu pendek\n• Ringan dan mudah terbang tertiup angin\n• Mudah terbakar\n• Kertas berlapis plastik atau lilin lebih sulit didaur ulang",
                impact = "• Produksi kertas baru menyebabkan deforestasi (penebangan hutan)\n• Proses produksi menggunakan banyak air dan energi\n• Kertas yang membusuk di TPA menghasilkan gas metana\n• Tinta pada kertas bisa mencemari tanah dan air\n• Pemutihan kertas dengan klorin menghasilkan dioksin\n• Indonesia kehilangan 684.000 hektar hutan per tahun, sebagian untuk industri pulp",
                recyclingTips = "• Pisahkan kertas bersih dari kertas kotor/berminyak\n• Kardus dilipat rata agar menghemat ruang\n• Kertas yang sudah didaur ulang bisa menjadi kertas baru, tisu, atau kemasan telur\n• Manfaatkan kertas bekas untuk kerajinan atau catatan\n• Gunakan kertas bolak-balik sebelum membuang\n• Hindari mencampur dengan kertas berlapis plastik (bungkus nasi, cup kopi)"
            ),
            WasteCategoryEntity(
                id = 3,
                name = "Kaca",
                icon = "kaca",
                description = "Sampah kaca adalah limbah anorganik yang berasal dari produk berbahan dasar silika (pasir) yang dilelehkan pada suhu sangat tinggi (sekitar 1700°C). Kaca merupakan material unik karena dapat didaur ulang 100% secara tak terbatas tanpa penurunan kualitas. Namun, jika dibuang sembarangan, kaca membutuhkan waktu jutaan tahun untuk terurai di alam.",
                characteristics = "• Sangat tahan lama dan tidak terurai secara alami (butuh 1-2 juta tahun)\n• Dapat didaur ulang 100% tanpa kehilangan kualitas\n• Transparan, tahan panas, dan tahan bahan kimia\n• Mudah pecah dan pecahannya sangat tajam — berbahaya\n• Inert (tidak bereaksi dengan isi di dalamnya)\n• Berat dibanding material kemasan lainnya",
                impact = "• Tidak terurai di alam dan menumpuk di TPA\n• Pecahan kaca berbahaya bagi manusia dan hewan\n• Produksi kaca baru membutuhkan energi sangat tinggi\n• Penambangan pasir silika merusak ekosistem sungai dan pantai\n• Namun dampaknya lebih rendah dibanding plastik jika didaur ulang dengan benar\n• Kaca daur ulang menghemat 30% energi dibanding produksi baru",
                recyclingTips = "• Pisahkan berdasarkan warna (bening, hijau, cokelat)\n• Bersihkan dari sisa makanan/minuman\n• Bungkus pecahan kaca dengan koran sebelum dibuang agar tidak melukai\n• Jangan campur kaca dengan keramik, cermin, atau kaca pyrex (titik leleh berbeda)\n• Botol kaca bisa di-reuse langsung sebagai wadah\n• Serahkan ke bank sampah atau fasilitas daur ulang kaca"
            ),
            WasteCategoryEntity(
                id = 4,
                name = "Organik",
                icon = "organik",
                description = "Sampah organik adalah limbah yang berasal dari sisa makhluk hidup (tumbuhan dan hewan) yang mudah terurai secara alami oleh mikroorganisme pengurai. Sampah organik merupakan jenis sampah terbesar di Indonesia, mencapai sekitar 60% dari total sampah yang dihasilkan. Meskipun bisa membusuk sendiri, pengelolaan yang tepat sangat penting untuk menghindari pencemaran.",
                characteristics = "• Mudah terurai secara alami (hari hingga beberapa bulan)\n• Berbau tidak sedap saat membusuk\n• Dapat diolah menjadi kompos atau biogas\n• Mengundang lalat, tikus, dan hewan pengganggu lainnya\n• Menghasilkan air lindi (leachate) yang bisa mencemari tanah\n• Menjadi sumber nutrisi jika diolah dengan benar",
                impact = "• Menghasilkan gas metana (CH₄) di TPA — 25x lebih kuat dari CO₂ sebagai gas rumah kaca\n• Air lindi mencemari tanah dan air tanah\n• Menimbulkan bau dan menjadi sarang penyakit\n• Sampah makanan menyia-nyiakan semua sumber daya yang digunakan untuk memproduksinya\n• Secara global, 1/3 makanan yang diproduksi terbuang sia-sia\n• Emisi dari food waste setara dengan negara penghasil emisi terbesar ketiga di dunia",
                recyclingTips = "• Buat kompos dari sisa sayur, buah, dan daun kering\n• Gunakan metode Takakura atau biopori untuk komposting rumahan\n• Sisa makanan matang bisa dijadikan pakan ternak (setelah diolah)\n• Pisahkan sampah organik basah dan kering\n• Kulit buah bisa dijadikan eco-enzyme (cairan pembersih alami)\n• Rencanakan belanja dan masak sesuai kebutuhan untuk mengurangi food waste"
            ),
            WasteCategoryEntity(
                id = 5,
                name = "B3",
                icon = "b3",
                description = "Sampah B3 (Bahan Berbahaya dan Beracun) adalah limbah yang mengandung zat kimia berbahaya yang dapat merusak lingkungan dan kesehatan manusia secara serius. Menurut PP No. 101 Tahun 2014, limbah B3 memiliki sifat mudah meledak, mudah menyala, reaktif, infeksius, korosif, dan/atau beracun. Jenis sampah ini SANGAT DILARANG untuk dibuang sembarangan atau dicampur dengan sampah rumah tangga biasa.",
                characteristics = "• Mengandung zat kimia berbahaya dan beracun\n• Dapat bersifat mudah terbakar, meledak, korosif, atau reaktif\n• Beberapa bersifat karsinogenik (menyebabkan kanker)\n• Dapat mencemari tanah dan air dalam jangka waktu sangat lama\n• Memerlukan penanganan khusus oleh pihak berwenang\n• Tidak boleh dicampur dengan sampah domestik biasa",
                impact = "• Mencemari tanah dan air tanah secara permanen\n• Menyebabkan keracunan akut hingga kematian pada manusia dan hewan\n• Merkuri dan timbal menyebabkan kerusakan saraf dan otak\n• Limbah elektronik melepaskan logam berat ke lingkungan\n• Baterai yang bocor melepaskan asam dan logam berat\n• Obat kadaluarsa yang dibuang sembarangan bisa masuk ke sumber air",
                recyclingTips = "• JANGAN buang bersama sampah rumah tangga biasa\n• Kumpulkan baterai bekas dan serahkan ke drop box yang disediakan\n• Obat kadaluarsa bisa dikembalikan ke apotek\n• Limbah elektronik serahkan ke pengepul e-waste resmi\n• Cat dan pelarut simpan dalam wadah tertutup rapat\n• Hubungi Dinas Lingkungan Hidup setempat untuk pembuangan limbah B3"
            ),
            WasteCategoryEntity(
                id = 6,
                name = "Logam",
                icon = "logam",
                description = "Sampah logam adalah limbah anorganik yang terbuat dari bahan tambang seperti besi, baja, aluminium, tembaga, dan seng. Material ini sangat bernilai karena dapat didaur ulang dan dilebur kembali berkali-kali menjadi produk baru tanpa mengurangi kualitas aslinya secara signifikan. Daur ulang logam jauh lebih hemat energi dibandingkan produksi dari bijih logam mentah.",
                characteristics = "• Sangat tahan lama dan kuat\n• Dapat didaur ulang berkali-kali tanpa penurunan kualitas signifikan\n• Bersifat konduktor panas dan listrik\n• Beberapa jenis mudah berkarat (besi, baja) jika terkena air\n• Memiliki nilai jual tinggi sebagai barang bekas\n• Berat dan memakan tempat di TPA",
                impact = "• Penambangan logam merusak ekosistem dan habitat alami\n• Proses smelting (peleburan) menghasilkan emisi CO₂ dan polusi udara\n• Logam yang berkarat bisa mencemari tanah\n• Kaleng bekas bisa menjadi sarang nyamuk jika menampung air\n• Namun, daur ulang aluminium menghemat 95% energi dibanding produksi baru\n• Daur ulang baja menghemat 60% energi dibanding produksi dari bijih besi",
                recyclingTips = "• Pisahkan logam besi (magnetik) dan non-besi (aluminium, tembaga)\n• Remas kaleng aluminium agar menghemat ruang\n• Bersihkan dari sisa makanan sebelum didaur ulang\n• Jual ke pengepul barang bekas — logam memiliki nilai jual tinggi\n• Kaleng bisa di-upcycle menjadi pot tanaman atau kerajinan\n• Jangan buang logam besar sembarangan — serahkan ke tempat pengelolaan"
            )
        )

        val examples = listOf(
            // Plastik examples
            WasteExampleEntity(1, 1, "Botol PET", "Botol minuman plastik transparan dengan kode resin #1, paling umum didaur ulang", "450 tahun"),
            WasteExampleEntity(2, 1, "Kantong Kresek", "Kantong belanja plastik tipis yang sering digunakan di pasar dan minimarket", "10-20 tahun"),
            WasteExampleEntity(3, 1, "Sedotan Plastik", "Sedotan sekali pakai yang sulit didaur ulang karena ukurannya kecil", "200 tahun"),
            WasteExampleEntity(4, 1, "Styrofoam", "Wadah makanan dari busa polistirena, tidak bisa didaur ulang di kebanyakan fasilitas", "500+ tahun"),
            WasteExampleEntity(5, 1, "Tutup Botol", "Tutup botol dari PP atau HDPE, sering terpisah dari botolnya saat didaur ulang", "450 tahun"),

            // Kertas examples
            WasteExampleEntity(6, 2, "Koran Bekas", "Kertas koran yang sudah dibaca, mudah didaur ulang menjadi kertas baru", "2-6 minggu"),
            WasteExampleEntity(7, 2, "Kardus", "Kotak kemasan dari karton bergelombang, sangat berharga untuk didaur ulang", "2 bulan"),
            WasteExampleEntity(8, 2, "Kertas HVS", "Kertas putih polos untuk printer/fotokopi, kualitas daur ulang terbaik", "2-6 minggu"),
            WasteExampleEntity(9, 2, "Buku Bekas", "Buku tulis atau cetak yang tidak dipakai lagi", "2-6 minggu"),
            WasteExampleEntity(10, 2, "Tisu Bekas", "Tisu yang sudah dipakai, TIDAK bisa didaur ulang karena serat terlalu pendek dan terkontaminasi", "2-4 minggu"),

            // Kaca examples
            WasteExampleEntity(11, 3, "Botol Kaca", "Botol minuman atau saus dari kaca, bisa didaur ulang 100%", "1-2 juta tahun"),
            WasteExampleEntity(12, 3, "Cermin", "Kaca dengan lapisan perak, TIDAK bisa didaur ulang bersama kaca biasa", "1-2 juta tahun"),
            WasteExampleEntity(13, 3, "Gelas Pecah", "Pecahan gelas atau piring kaca, harus dibungkus aman sebelum dibuang", "1-2 juta tahun"),
            WasteExampleEntity(14, 3, "Toples Kaca", "Wadah selai, saus, atau bumbu dari kaca, bisa di-reuse atau didaur ulang", "1-2 juta tahun"),

            // Organik examples
            WasteExampleEntity(15, 4, "Sisa Makanan", "Nasi basi, lauk sisa, sayur basi — bisa dijadikan kompos", "1-6 bulan"),
            WasteExampleEntity(16, 4, "Daun Kering", "Daun yang gugur dari pohon, bahan kompos yang sangat baik (brown material)", "3-12 bulan"),
            WasteExampleEntity(17, 4, "Kulit Buah", "Kulit pisang, jeruk, apel — kaya nutrisi untuk kompos", "2-5 minggu"),
            WasteExampleEntity(18, 4, "Ampas Kopi", "Sisa bubuk kopi setelah diseduh, bisa langsung jadi pupuk tanaman", "2-3 bulan"),
            WasteExampleEntity(19, 4, "Cangkang Telur", "Kulit telur yang sudah pecah, sumber kalsium untuk kompos", "3 tahun"),

            // B3 examples
            WasteExampleEntity(20, 5, "Baterai Bekas", "Baterai AA/AAA/lithium mengandung merkuri, kadmium, dan timbal", "100+ tahun"),
            WasteExampleEntity(21, 5, "Cat & Pelarut", "Sisa cat dan thinner mengandung VOC dan logam berat", "Tidak terurai"),
            WasteExampleEntity(22, 5, "Obat Kadaluarsa", "Obat yang sudah melewati tanggal kadaluarsa, berbahaya jika masuk ke air", "Bervariasi"),
            WasteExampleEntity(23, 5, "Lampu Neon/CFL", "Lampu hemat energi mengandung merkuri yang sangat beracun", "Tidak terurai"),
            WasteExampleEntity(24, 5, "Pestisida", "Sisa racun hama dan herbisida, sangat berbahaya bagi ekosistem", "Tidak terurai"),

            // Logam examples
            WasteExampleEntity(25, 6, "Kaleng Aluminium", "Kaleng minuman ringan, paling bernilai dan mudah didaur ulang", "80-200 tahun"),
            WasteExampleEntity(26, 6, "Paku & Kawat", "Paku, kawat, dan sekrup dari besi/baja", "50-100 tahun"),
            WasteExampleEntity(27, 6, "Besi Tua", "Potongan besi, pipa, atau rangka besi yang tidak terpakai", "50-100 tahun"),
            WasteExampleEntity(28, 6, "Tutup Kaleng", "Tutup kaleng makanan dari baja atau aluminium", "80-200 tahun"),
            WasteExampleEntity(29, 6, "Kaleng Makanan", "Kaleng sarden, kornet, susu dari tin plate (baja berlapis timah)", "50-100 tahun")
        )

        dao.insertCategories(categories)
        dao.insertExamples(examples)
    }
}
