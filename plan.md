# EcoPedia — Plan & Rangkuman Sesi

## Rangkuman Sesi (20 Mei 2026)

### Apa yang Sudah Dikerjakan

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
- [ ] `WasteCategoryEntity.kt` — Entity Room
- [ ] `WasteExampleEntity.kt` — Entity Room
- [ ] `WasteDao.kt` — Data Access Object (query)
- [ ] `EcoPediaDatabase.kt` — Room Database class
- [ ] `DatabaseSeeder.kt` — Prepopulate data awal (6 kategori + contoh per kategori)

#### 2. Dependencies
- [ ] Tambah Room dependencies ke `libs.versions.toml` dan `build.gradle.kts`
- [ ] Tambah KSP (Kotlin Symbol Processing) untuk Room annotation processor

#### 3. UI Updates
- [ ] Update `HomeFragment.kt` — Ambil data kategori dari Room, bukan hardcoded
- [ ] Update `DetailActivity.kt` — Tampilkan deskripsi, ciri-ciri, dampak, dan contoh dari database
- [ ] Update `activity_detail.xml` — Tambah section untuk deskripsi lengkap dan daftar contoh sampah
- [ ] Update `Category.kt` — Sesuaikan data class dengan entity baru atau buat mapper

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

## Catatan Penting
- Baca `antigravity.md` di root project sebelum mulai kerja — berisi aturan khusus (selalu tanya sebelum eksekusi, cek skills dulu)
- DetailActivity dan SplashActivity belum disentuh di sesi ini
- `nav_text_color_selector.xml` dan color resources lama masih utuh, belum perlu diubah
- Dark mode theming untuk fragment baru belum di-test secara visual
