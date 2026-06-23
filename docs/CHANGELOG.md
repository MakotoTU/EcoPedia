# 📋 EcoPedia - Changelog

---

## 22:30 | 23 Juni 2026 | Selasa | Fase 3: Offline-First and Manual Input |

### 1. Local-First Caching Database
**File:** `app/src/main/java/com/makoto/ecopedia/data/local/LocalProductEntity.kt`
- Menambahkan `LocalProductEntity` di Room Database untuk menyimpan data scan produk beserta jenis kemasannya secara lokal. 
- **Alasan:** Mempercepat scan ulang dan memastikan data produk tetap bisa diakses meskipun pengguna sedang offline.

### 2. Manual Entry Input
**File:** `app/src/main/java/com/makoto/ecopedia/ScanResultBottomSheet.kt`
- Menambahkan UI text input nama produk di `ScanResultBottomSheet` untuk memberi nama produk yang tidak dikenali oleh API atau saat kondisi offline.
- **Alasan:** Memberikan fleksibilitas kepada pengguna untuk tetap mencatat item yang tidak terdaftar di database Open Food Facts.

### 3. API Response Fallback
**File:** `app/src/main/java/com/makoto/ecopedia/repository/ScanRepository.kt`
- Menyimpan data dari OpenFoodFacts ke cache lokal. Jika API gagal (404/Offline), aplikasi akan menampilkan form Manual Entry yang akan menyimpan barcode, nama kustom, dan ID sampah ke database lokal.

---

## 21:40 | 23 Juni 2026 | Senin | Refactor Fitur Scan (Perbaikan Kritis) |

### 1. Fix User-Agent pada RetrofitClient
**File:** `app/src/main/java/com/makoto/ecopedia/data/api/RetrofitClient.kt`
- Ditambahkan `Interceptor` pada `OkHttpClient` yang menyisipkan header `User-Agent: EcoPedia/1.0 (Android; Kotlin)` di setiap HTTP request.
- **Alasan:** API Open Food Facts menolak request tanpa `User-Agent` yang valid dengan respons HTTP 403 Forbidden. Tanpa fix ini, fitur scan barcode selalu gagal mengambil data produk dari server.

### 2. Fix Thread Leak pada ScanFragment
**File:** `app/src/main/java/com/makoto/ecopedia/ScanFragment.kt`
- `cameraExecutor.shutdown()` dipindahkan dari `onDestroy()` ke `onDestroyView()`.
- **Alasan:** Pada navigasi tab di BottomNavigationView, Android hanya menghancurkan *view* fragment (memanggil `onDestroyView`), bukan fragment itu sendiri (tidak memanggil `onDestroy`). Akibatnya, `ExecutorService` yang dibuat di `onViewCreated` tidak pernah di-shutdown, menyebabkan thread baru terus menumpuk setiap kali user pindah tab dan kembali ke tab Scan. Ini adalah *thread leak* yang bisa menyebabkan ANR atau OOM pada penggunaan lama.

### 3. Fix Navigasi Backstack pada ScanResultBottomSheet
**File:** `app/src/main/java/com/makoto/ecopedia/ScanResultBottomSheet.kt`
- Dihapus `addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)` pada Intent di `showErrorState()`.
- `appContext.startActivity(intent)` diganti menjadi `requireActivity().startActivity(intent)`.
- Intent context diganti dari `applicationContext` menjadi `requireActivity()`.
- **Alasan:** `FLAG_ACTIVITY_NEW_TASK` memaksa `DetailActivity` dibuka di task terpisah, sehingga tombol Back tidak mengembalikan user ke `MainActivity` (tab Scan). Dengan menggunakan Activity context biasa tanpa flag, `DetailActivity` masuk ke task yang sama dan navigasi Back berfungsi normal.

---

## 21:49 | 23 Juni 2026 | Senin | Fase 1: Fix CRITICAL dari Review Menyeluruh |

### 1. Fix Plugin Kotlin Tidak Diterapkan (C6)
**File:** `build.gradle.kts` (root) dan `app/build.gradle.kts`
- Ditambahkan `alias(libs.plugins.kotlin.android)` di kedua file Gradle.
- Ditambahkan `kotlinOptions { jvmTarget = "11" }` di `app/build.gradle.kts`.
- **Alasan:** Tanpa plugin `kotlin-android`, semua file `.kt` tidak bisa di-compile, KSP/Room codegen gagal. Proyek tidak bisa di-build sama sekali.

### 2. Fix `fallbackToDestructiveMigration()` Deprecated (C5)
**File:** `app/src/main/java/com/makoto/ecopedia/data/EcoPediaDatabase.kt`
- Diganti dari `.fallbackToDestructiveMigration()` menjadi `.fallbackToDestructiveMigration(dropAllTables = true)`.
- Fix race condition pada singleton (DCL pattern): ditambahkan re-check `INSTANCE` di dalam blok `synchronized`.
- **Alasan:** Room 2.7.1 menghapus overload tanpa parameter. Tanpa fix ini, proyek gagal compile.

### 3. Fix Tipe `packaging` pada Response Model (C3)
**File:** `app/src/main/java/com/makoto/ecopedia/data/api/OpenFoodFactsResponse.kt`
- Tipe `packaging` diubah dari `PackagingData?` (object) menjadi `String?`.
- Class `PackagingData` dihapus.
- Akses `product.packaging?.text` di `ScanResultBottomSheet.kt` diubah menjadi `product.packaging`.
- **Alasan:** API Open Food Facts mengembalikan `packaging` sebagai String biasa (misal `"en:plastic,en:cardboard"`), bukan JSON object. Gson throws `JsonSyntaxException` saat mencoba deserialize string menjadi object.

### 4. Fix Crash `dismiss()` sebelum `viewLifecycleOwner` (C1)
**File:** `app/src/main/java/com/makoto/ecopedia/ScanResultBottomSheet.kt`
- Activity reference ditangkap sebelum `dismiss()`.
- `viewLifecycleOwner.lifecycleScope.launch` diganti dengan `CoroutineScope(Dispatchers.Main).launch` yang independent dari lifecycle fragment.
- Semua `requireActivity()` di dalam coroutine diganti dengan `hostActivity` yang sudah ditangkap sebelumnya.
- **Alasan:** Setelah `dismiss()`, lifecycle view fragment sudah DESTROYED. Mengakses `viewLifecycleOwner` setelah dismiss pasti crash `IllegalStateException`.

### 5. Fix Crash `requireActivity()` di Callback Kamera (C2)
**File:** `app/src/main/java/com/makoto/ecopedia/ScanFragment.kt`
- `requireActivity().runOnUiThread` diganti menjadi `activity?.runOnUiThread`.
- Ditambahkan guard `if (!isAdded) return@runOnUiThread`.
- **Alasan:** Callback `BarcodeAnalyzer` berjalan di thread kamera. Jika user pindah tab sebelum barcode selesai diproses, `requireActivity()` throws crash karena fragment sudah detach.

### 6. Fix Warna `black`/`white` Terbalik di Dark Mode (C4)
**File:** `app/src/main/res/values/colors.xml` dan `values-night/colors.xml`
- `black` dan `white` dikembalikan ke nilai semantik aslinya (hitam = hitam, putih = putih) di kedua mode.
- Ditambahkan semantic color names: `surface`, `on_surface`, `surface_variant`, `divider`, `text_hint`.
- `accent_green` dibedakan dari `primary_green` di dark mode (`#66D9A5` vs `#42B883`).
- Seluruh layout yang menggunakan `@color/white` sebagai background/surface dimigrasi ke `@color/surface`:
  - `activity_main.xml` (toolbar, bottom nav)
  - `activity_detail.xml` (top bar, hero card)
  - `fragment_tips.xml` (3 tip cards)
  - `item_category.xml` (category card)
  - `layout_scan_result_bottom_sheet.xml` (button background)
- **Alasan:** `black=#FFFFFFFF` dan `white=#FF121212` di night mode adalah penamaan terbalik yang sangat membingungkan dan rawan bug untuk development ke depan.

---

## 22:11 | 23 Juni 2026 | Selasa | Fase 2: Fix HIGH & MEDIUM dari Review Menyeluruh |

### 1. Fix Coroutine Leak dan File Leak (H1, H2)
**File:** `app/src/main/java/com/makoto/ecopedia/ScanResultBottomSheet.kt`
- Pemanggilan API dan pemrosesan stream gambar dipindahkan ke blok `withContext(Dispatchers.IO)` yang terikat dengan coroutine induk.
- Stream download gambar menggunakan block `.use {}` agar resource otomatis ditutup.
- **Alasan:** Mencegah thread yang terus berjalan (memory leak) dan file descriptor yang tidak ditutup.

### 2. Fix Crash Akses Context (H3, H4)
**File:** `app/src/main/java/com/makoto/ecopedia/ScanFragment.kt`
- `requireContext()` diganti dengan pengecekan aman `context?.let` di dalam callback permission dan camera provider.
- **Alasan:** Mencegah `IllegalStateException` jika user pindah tab dengan cepat saat inisialisasi masih berjalan.

### 3. Handle HTTP Error pada API (H6)
**File:** `OpenFoodFactsApi.kt` dan `ScanResultBottomSheet.kt`
- Return type `getProduct` diubah menjadi `retrofit2.Response<OpenFoodFactsResponse>`.
- Ditambahkan penanganan khusus untuk HTTP 404 (Tidak ditemukan) dan 429 (Terlalu Banyak Request).
- **Alasan:** Agar tidak semua error server masuk ke block `catch (Exception)` dan dianggap sebagai "Masalah Koneksi".

### 4. Fix Room Schema Export (H7)
**File:** `EcoPediaDatabase.kt` dan `app/build.gradle.kts`
- Mengubah `exportSchema = false` menjadi `true`.
- Menambahkan ksp `room.schemaLocation` di gradle.
- **Alasan:** Room mewajibkan ekspor skema untuk memverifikasi konsistensi database saat melakukan migrasi versi di masa depan.

### 5. Fix Tipe Data Nullable `ecoScore` (M12)
**File:** `app/src/main/java/com/makoto/ecopedia/data/ScanHistoryEntity.kt`
- Mengubah tipe `ecoScore` menjadi `String?`.
- **Alasan:** Karena data skor ekologi dari API bisa jadi tidak ada (null).

### 6. Atomic Database Seeding (M14)
**File:** `WasteDao.kt` dan `DatabaseSeeder.kt`
- Membuat fungsi `seedAll()` dengan anotasi `@Transaction` di dalam DAO untuk menggabungkan insert kategori dan contoh sampah.
- **Alasan:** Jika terjadi kegagalan (crash) saat proses seeding awal, mencegah database tersisa dalam kondisi setengah terisi.

---
