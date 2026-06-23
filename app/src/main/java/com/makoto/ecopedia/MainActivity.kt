package com.makoto.ecopedia

import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import android.widget.Toast
import com.makoto.ecopedia.worker.EcoTipsWorker

class MainActivity : AppCompatActivity() {

    private var isReady = false

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            setupPeriodicNotification()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        
        if (savedInstanceState == null) {
            // Tahan splash screen selama 1.5 detik HANYA saat aplikasi pertama kali dibuka
            lifecycleScope.launch {
                delay(1500)
                isReady = true
            }
            splashScreen.setKeepOnScreenCondition { !isReady }
        } else {
            // Jika Activity di-recreate (misal karena ganti tema), langsung tampilkan
            isReady = true
        }

        val sharedPrefs = getSharedPreferences("ThemePrefs", MODE_PRIVATE)
        if (sharedPrefs.contains("isNightMode")) {
            val isNightMode = sharedPrefs.getBoolean("isNightMode", false)
            AppCompatDelegate.setDefaultNightMode(
                if (isNightMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
        
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        setupToolbar()
        setupNavigation()
        setupEdgeToEdge()
        checkNotificationPermission()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Semua tab adalah top-level destination (tidak ada tombol back)
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFragment, R.id.scanFragment, R.id.tipsFragment)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Hubungkan BottomNavigationView dengan NavController
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        // Update icon tema berdasarkan mode saat ini
        val isDarkMode = resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        menu.findItem(R.id.action_toggle_theme)?.setIcon(
            if (isDarkMode) R.drawable.moon_darkmode else R.drawable.sun_lightmode
        )

        // Tint icon menu agar sesuai tema
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.overflowIcon?.setTint(ContextCompat.getColor(this, R.color.primary_green))
        menu.findItem(R.id.action_toggle_theme)?.icon?.setTint(ContextCompat.getColor(this, R.color.primary_green))

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_toggle_theme -> {
                toggleTheme()
                true
            }
            R.id.action_about -> {
                showAboutDialog()
                true
            }
            R.id.action_test_notification -> {
                testNotificationNow()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun testNotificationNow() {
        val workRequest = OneTimeWorkRequestBuilder<EcoTipsWorker>().build()
        WorkManager.getInstance(this).enqueue(workRequest)
        Toast.makeText(this, "Mengirim notifikasi tes...", Toast.LENGTH_SHORT).show()
    }

    private fun toggleTheme() {
        val isDarkMode = resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        
        val newMode = if (isDarkMode) AppCompatDelegate.MODE_NIGHT_NO else AppCompatDelegate.MODE_NIGHT_YES
        AppCompatDelegate.setDefaultNightMode(newMode)
        
        val sharedPrefs = getSharedPreferences("ThemePrefs", MODE_PRIVATE)
        sharedPrefs.edit().putBoolean("isNightMode", !isDarkMode).apply()
    }

    private fun showAboutDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Tentang EcoPedia")
            .setMessage(
                "EcoPedia v${packageManager.getPackageInfo(packageName, 0).versionName}\n\n" +
                "Aplikasi edukasi untuk memahami berbagai jenis sampah dan tips hidup ramah lingkungan.\n\n" +
                "Dibuat dengan ❤\uFE0F untuk lingkungan yang lebih bersih."
            )
            .setIcon(R.drawable.leaf)
            .setPositiveButton("Tutup", null)
            .show()
    }

    private fun setupEdgeToEdge() {
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout)) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            toolbar.setPadding(
                toolbar.paddingLeft,
                systemBars.top,
                toolbar.paddingRight,
                toolbar.paddingBottom
            )

            insets
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    setupPeriodicNotification()
                }
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            setupPeriodicNotification()
        }
    }

    private fun setupPeriodicNotification() {
        val workRequest = PeriodicWorkRequestBuilder<EcoTipsWorker>(24, TimeUnit.HOURS)
            .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "EcoTipsDaily",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}