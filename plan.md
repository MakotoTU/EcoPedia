# EcoPedia — Plan & Rangkuman Sesi

## Rangkuman Sesi (17 Juni 2026)

### Apa yang Baru Saja Dikerjakan

**1. Perbaikan Bug Kritis:**
- **Splash Screen Blank:** Membersihkan redundansi atribut `android:` di `themes.xml` agar kompatibel dengan library `core-splashscreen` terbaru, serta memberikan panduan tentang *Android Launcher Caching Bug*.
- **Ganti Tema Lemot (1.5s delay):** Memperbaiki delay UI di `MainActivity.kt` saat ganti tema dengan menambahkan pengecekan `savedInstanceState == null` sehingga jeda *splash screen* hanya berjalan saat *Cold Boot*.
- **Crash Fragment Detached (`ScanResultBottomSheet`):** Memperbaiki *crash* (`IllegalStateException: Can't access ViewModels from detached fragment`) saat *bottom sheet* ditutup paksa ketika gambar sedang didownload. Solusi: Membuang inisialisasi `scanHistoryViewModel` yang terikat pada *lifecycle fragment* dan memindahkannya ke `CoroutineScope(Dispatchers.IO).launch` independen menggunakan `applicationContext`.

**2. Penambahan Fitur Baru:**
- **Notifikasi Harian (Eco-Tips):** Atas permintaan dosen, fitur notifikasi pengingat harian telah diimplementasikan menggunakan `WorkManager`. Notifikasi akan berjalan di latar belakang setiap 24 jam sekali membagikan tips ramah lingkungan secara acak.
- **Tombol Test Notifikasi (Demo):** Menambahkan tombol *testing* di menu pojok kanan atas aplikasi untuk memicu pengiriman notifikasi secara langsung tanpa menunggu 24 jam.

**3. Basis Data (Persiapan Riwayat Scan):**
- Menambahkan `ScanHistoryEntity` dan `ScanHistoryDao` ke dalam Room Database.
- Mengedukasi cara melihat fisik database SQLite (`ecopedia_database`) via fitur *App Inspection (Database Inspector)* dan *Device File Explorer* di Android Studio.

---

## Rangkuman Sesi (20 Mei 2026)

**Migrasi Navigasi: Multi-Activity → Single Activity + Navigation Component**

Seluruh navigasi EcoPedia berhasil dimigrasikan dari arsitektur multi-Activity (dengan custom bottom nav CardView) ke Single Activity + Fragment + Navigation Component.

#### Arsitektur Sekarang
```
SplashActivity → MainActivity (Host)
                    ├── NavHostFragment
                    │     ├── HomeFragment (RecyclerView kategori)
                    │     ├── ScanFragment (placeholder "Coming Soon")
                    │     └── TipsFragment (tips lingkungan)
                    ├── Material BottomNavigationView (auto-sync dengan NavController)
                    └── Options Menu (⋮)
                          ├── Toggle Dark/Light Mode
                          └── About (AlertDialog)
```

#### File Baru yang Dibuat
- `res/navigation/nav_graph.xml` — Navigation graph
- `res/menu/bottom_nav_menu.xml` — Menu bottom nav
- `res/menu/main_menu.xml` — Options menu (theme + about)
- `HomeFragment.kt` + `fragment_home.xml`
- `ScanFragment.kt` + `fragment_scan.xml`
- `TipsFragment.kt` + `fragment_tips.xml`

#### File yang Dimodifikasi
- `gradle/libs.versions.toml` — Tambah navigation deps
- `app/build.gradle.kts` — Tambah navigation implementation
- `activity_main.xml` — Full rewrite (MaterialToolbar + NavHostFragment + BottomNavigationView)
- `MainActivity.kt` — Full rewrite (NavController + Options Menu + Theme Toggle + About Dialog)
- `values/themes.xml` — Tambah ToolbarTitle style
- `AndroidManifest.xml` — Hapus TipsActivity

#### File yang Dihapus
- `TipsActivity.kt`
- `activity_tips.xml`

#### Bug yang Diperbaiki
1. **Kategori terpotong (3 dari 6 hilang)**: Root cause → `ScrollView` biasa gagal mengukur tinggi RecyclerView di dalam Fragment. Fix → Ganti ke `NestedScrollView` dengan `fillViewport="true"`.
2. **Layout meluber**: `match_parent` width di ConstraintLayout → Ganti ke `0dp` + constraint start/end.
3. **Double padding**: Padding manual di BottomNavigationView → Hapus, biarkan Material 3 handle sendiri.

#### Status Build
- ✅ `./gradlew assembleDebug` — BUILD SUCCESSFUL (0 errors)

---

### Keputusan yang Sudah Dibuat
| Topik | Keputusan |
|---|---|
| Arsitektur navigasi | Single Activity + Fragment + Navigation Component |
| Fitur Pindai (Scan) | Placeholder dulu, nanti implementasi pakai Gemini API |
| About | AlertDialog (sudah diimplementasi) |
| Options Menu | Toggle light/dark mode + About |
| Konten ensiklopedia | Disimpan lokal (Room Database), bukan API eksternal |
| API eksternal | Open Food Facts → untuk fitur barcode scan kemasan (nanti) |
| API untuk Scan gambar | Gemini API (nanti, saat fitur Pindai dikerjakan) |

---

## Plan Selanjutnya: Room Database untuk Ensiklopedia

### Tujuan
Menyimpan data ensiklopedia sampah secara lokal menggunakan Room Database agar:
- Konten bisa diakses offline
- Loading lebih cepat (tanpa network request)
- Data statis (jenis sampah, deskripsi, contoh) tidak perlu API eksternal

### Struktur Data yang Dibutuhkan

#### Entity: `WasteCategory`
```kotlin
@Entity(tableName = "waste_categories")
data class WasteCategory(
    @PrimaryKey val id: Int,
    val name: String,           // "Plastik", "Kertas", "Kaca", dll
    val icon: Int,              // Resource ID drawable
    val description: String,    // Deskripsi lengkap kategori
    val characteristics: String,// Ciri-ciri sampah kategori ini
    val impact: String,         // Dampak lingkungan
    val recyclingTips: String   // Cara daur ulang / pengelolaan
)
```

#### Entity: `WasteExample`
```kotlin
@Entity(tableName = "waste_examples")
data class WasteExample(
    @PrimaryKey val id: Int,
    val categoryId: Int,        // FK ke WasteCategory
    val name: String,           // "Botol plastik PET", "Baterai bekas", dll
    val description: String,    // Penjelasan singkat
    val decompositionTime: String // "450 tahun", "1-5 bulan", dll
)
```

### Komponen yang Perlu Dibuat

#### 1. Database Layer
- [x] `WasteCategoryEntity.kt` — Entity Room
- [x] `WasteExampleEntity.kt` — Entity Room
- [x] `WasteDao.kt` — Data Access Object (query)
- [x] `EcoPediaDatabase.kt` — Room Database class
- [x] `DatabaseSeeder.kt` — Prepopulate data awal (6 kategori + contoh per kategori)

#### 2. Dependencies
- [x] Tambah Room dependencies ke `libs.versions.toml` dan `build.gradle.kts`
- [x] Tambah KSP (Kotlin Symbol Processing) untuk Room annotation processor

#### 3. UI Updates
- [x] Update `HomeFragment.kt` — Ambil data kategori dari Room, bukan hardcoded
- [x] Update `DetailActivity.kt` — Tampilkan deskripsi, ciri-ciri, dampak, dan contoh dari database
- [x] Update `activity_detail.xml` — Tambah section untuk deskripsi lengkap dan daftar contoh sampah
- [x] Update `Category.kt` — Sesuaikan data class dengan entity baru atau buat mapper

#### 4. Data Seeding (Konten Ensiklopedia)
Isi minimal untuk 6 kategori:

| Kategori | Contoh Sampah |
|---|---|
| Plastik | Botol PET, kantong kresek, sedotan, styrofoam, tutup botol |
| Kertas | Koran, kardus, kertas HVS, buku bekas, tissue |
| Kaca | Botol kaca, cermin, gelas pecah, lampu pijar |
| Organik | Sisa makanan, daun kering, kulit buah, ampas kopi |
| B3 (Bahan Berbahaya & Beracun) | Baterai, cat, pestisida, obat kadaluarsa, lampu neon |
| Logam | Kaleng aluminium, paku, kawat, besi tua, tutup kaleng |

### Urutan Pengerjaan
1. Setup Room dependencies
2. Buat Entity + DAO + Database class
3. Buat DatabaseSeeder dengan data ensiklopedia
4. Update HomeFragment untuk baca dari database
5. Update DetailActivity untuk tampilkan konten lengkap
6. Verifikasi build + test di device

---

## Plan: Open Food Facts API (Barcode Scan Kemasan) (Status: Saved as Plan / Dinonaktifkan Sementara)

### Tujuan
Mengintegrasikan Open Food Facts API agar pengguna bisa scan barcode kemasan produk dan mendapatkan informasi terkait kemasan/dampak lingkungannya.

### Tentang API
- **Base URL**: `https://world.openfoodfacts.org/api/v2/product/{barcode}.json`
- **Gratis & Open Source** — tidak perlu API key
- **Data yang relevan**: nama produk, packaging material, eco-score, kategori produk, gambar produk

### Komponen yang Perlu Dibuat

#### 1. Dependencies
- [x] Tambah Retrofit + Gson/Moshi ke `libs.versions.toml` dan `build.gradle.kts`
- [x] Tambah Google Play Services Code Scanner (sebagai ganti CameraX) ke `libs.versions.toml` dan `build.gradle.kts`
- [x] Tambah Internet permission di `AndroidManifest.xml`

#### 2. Network Layer
- [x] `OpenFoodFactsApi.kt` — Retrofit interface (GET product by barcode)
- [x] `OpenFoodFactsResponse.kt` — Data class untuk response API
- [x] `RetrofitClient.kt` — Singleton Retrofit instance

#### 3. UI Updates
- [x] Update `ScanFragment` — Mengaktifkan pemicu Google Code Scanner (*Dinonaktifkan kembali ke placeholder*)
- [x] `ScanResultBottomSheet.kt` (menggantikan ScanResultActivity) — Tampilkan hasil scan (nama produk, packaging, eco-score, tips daur ulang, link ensiklopedia)
- [x] Layout untuk hasil scan (`layout_scan_result_bottom_sheet.xml`)

#### 4. Fitur
- [x] Scan barcode kemasan via kamera (Google Code Scanner sandboxed)
- [x] Fetch data produk dari Open Food Facts API
- [x] Tampilkan info produk: nama, gambar (via Coil), jenis kemasan, eco-score badge
- [x] Klasifikasi jenis kemasan ke kategori sampah (mapping ke WasteCategory dari Room DB)
- [x] Handling error: produk tidak ditemukan, tidak ada internet, serta manual selection spinner fallback

> [!WARNING]
> **Status Saat Ini: Dinonaktifkan Sementara (Saved as Plan)**
> Seluruh kode implementasi di atas telah selesai dibuat dan berhasil di-compile, namun karena kendala inisialisasi GMS Code Scanner yang memicu crash pada perangkat fisik pengujian, UI utama `ScanFragment` dikembalikan ke tampilan "Segera Hadir". Kode tetap disimpan utuh di dalam proyek untuk pengembangan lebih lanjut.

### Urutan Pengerjaan
1. Setup Retrofit + dependencies — **SELESAI**
2. Buat network layer (API interface + response model) — **SELESAI**
3. Implementasi barcode scanner di ScanFragment — **SELESAI (Dinonaktifkan)**
4. Buat UI hasil scan (Bottom Sheet) — **SELESAI**
5. Hubungkan hasil scan dengan kategori sampah dari Room DB — **SELESAI**
6. Testing & error handling — **SELESAI (Bypass to coming soon due to GMS Crash)**

---

## Plan (Masa Depan): Gemini API untuk Scan Gambar

> **Status**: Ditunda — akan dikerjakan nanti setelah fitur barcode scan selesai.

Gemini API akan digunakan untuk identifikasi jenis sampah dari foto/gambar yang diambil pengguna, tanpa perlu barcode.

---

## Catatan Penting
- Baca `antigravity.md` di root project sebelum mulai kerja — berisi aturan khusus (selalu tanya sebelum eksekusi, cek skills dulu)
- DetailActivity dan SplashActivity belum disentuh di sesi ini
- `nav_text_color_selector.xml` dan color resources lama masih utuh, belum perlu diubah
- Dark mode theming untuk fragment baru belum di-test secara visual
