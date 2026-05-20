package com.makoto.ecopedia

import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        setupToolbar()
        setupNavigation()
        setupEdgeToEdge()
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
        toolbar.overflowIcon?.setTint(getColor(R.color.primary_green))
        menu.findItem(R.id.action_toggle_theme)?.icon?.setTint(getColor(R.color.primary_green))

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
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun toggleTheme() {
        val isDarkMode = resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
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
}