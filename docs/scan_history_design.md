# Rancangan Fitur Riwayat Scan (Scan History)

## 1. Ringkasan Pemahaman
- **Apa yang dibangun:** Fitur Riwayat Scan (Scan History) untuk menyimpan hasil pindaian barcode/gambar secara lokal.
- **Tujuan:** Memudahkan pengguna melihat kembali informasi produk, kategori sampah, dan *eco-score* tanpa harus memindai ulang barang yang sama.
- **Pengguna:** Pengguna EcoPedia yang aktif menggunakan fitur *scanner*.
- **Batasan Utama:** Data disimpan murni secara lokal menggunakan Room Database. Terdapat kontrol pengelolaan memori melalui antarmuka "Hapus Semua" dan "Hapus per Item".
- **Bukan Tujuan:** Tidak ada pencadangan (*backup*) data ke server *cloud*. Tidak ada penghapusan otomatis berdasarkan batas kuota riwayat.

## 2. Asumsi
- **Performa:** Pengambilan data riwayat dari database akan dieksekusi secara *asynchronous* (menggunakan Kotlin Coroutines / Flow) agar antarmuka (*scrolling*) tetap responsif.
- **Privasi:** Data 100% tersimpan di perangkat lokal sehingga aman dan privat.
- **Skema Penyimpanan Gambar:** Karena dilarang menyimpan BLOB, gambar akan disimpan di penyimpanan internal Android (`getFilesDir()`) dan Room hanya akan menyimpan `String` *file path*-nya.

## 3. Decision Log (Log Keputusan)
| Keputusan | Alternatif yang Dipertimbangkan | Alasan Pemilihan |
|---|---|---|
| **Penyimpanan Data** | Room Database | Sudah menjadi standar Android dan strukturnya jauh lebih bersih, aman, serta terintegrasi dengan Coroutines dibandingkan `SQLiteOpenHelper` manual. |
| **Cakupan Data Riwayat** | Info Dasar (hanya barcode & nama) | **Dipilih: Info Lengkap** (Barcode, Nama Produk, Kategori, Eco-Score, Tanggal, Path Gambar) agar pengguna mendapatkan konteks penuh saat membuka riwayat lama. |
| **Pengelolaan Skala Penyimpanan** | Batas otomatis 100 item atau Simpan Tanpa Batas | **Dipilih: Simpan Semua dengan Fitur Hapus Manual** (Hapus Semua & Hapus Satuan). Memberikan kontrol penuh ke pengguna tanpa membuang riwayat mereka secara paksa. |

## 4. Rancangan Arsitektur (Draft - Menunggu Validasi)
*Bagian ini dapat dievaluasi lebih lanjut setelah sesi dilanjutkan.*

**Entity Baru: `ScanHistoryEntity`**
```kotlin
@Entity(
    tableName = "scan_history",
    foreignKeys = [
        ForeignKey(
            entity = WasteCategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.SET_NULL // Jika kategori dihapus, riwayat tetap ada tapi kategori null
        )
    ],
    indices = [Index(value = ["categoryId"])]
)
data class ScanHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val barcode: String,
    val productName: String,
    val categoryId: Int?,
    val ecoScore: String,
    val imagePath: String?,    // Path gambar di local storage
    val scannedAt: Long        // Timestamp saat di-scan
)
```

**Fungsi DAO Tambahan (`ScanHistoryDao`)**
- `suspend fun insertScan(history: ScanHistoryEntity)`
- `fun getAllHistory(): Flow<List<ScanHistoryEntity>>`
- `suspend fun deleteHistoryById(id: Int)`
- `suspend fun clearAllHistory()`

---
*Status: Understanding Lock Confirmed. Sesi dijeda. Validasi desain dan implementasi akan dilanjutkan pada sesi berikutnya.*
