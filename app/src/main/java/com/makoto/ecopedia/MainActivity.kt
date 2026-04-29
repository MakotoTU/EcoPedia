package com.makoto.ecopedia

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        setupCategoryClicks()
        setupNavigation()
        setupThemeToggle()
        setupEdgeToEdge()
    }

    private fun setupNavigation() {
        val navHome = findViewById<View>(R.id.navHome)
        val navTips = findViewById<View>(R.id.navTips)
        
        // Set Home as selected by default
        navHome.isSelected = true

        navTips.setOnClickListener {
            val intent = Intent(this, TipsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupThemeToggle() {
        val btnToggle = findViewById<ImageButton>(R.id.btnThemeToggle)
        
        // Update icon based on current mode
        val isDarkMode = resources.configuration.uiMode and 
                android.content.res.Configuration.UI_MODE_NIGHT_MASK == 
                android.content.res.Configuration.UI_MODE_NIGHT_YES
        
        btnToggle.setImageResource(if (isDarkMode) R.drawable.moon_darkmode else R.drawable.sun_lightmode)

        btnToggle.setOnClickListener {
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
    }

    private fun setupCategoryClicks() {
        val categories = mapOf(
            R.id.cardPlastik to "Plastik",
            R.id.cardKertas to "Kertas",
            R.id.cardKaca to "Kaca",
            R.id.cardOrganik to "Organik",
            R.id.cardB3 to "B3",
            R.id.cardLogam to "Logam"
        )

        for ((id, name) in categories) {
            findViewById<View>(id).setOnClickListener {
                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra("CATEGORY", name)
                startActivity(intent)
            }
        }
    }

    private fun setupEdgeToEdge() {
        val topBar = findViewById<View>(R.id.topBarContent)
        val bottomNav = findViewById<View>(R.id.bottomNavContent)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainScrollView)) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            
            topBar.setPadding(
                topBar.paddingLeft,
                systemBars.top,
                topBar.paddingRight,
                topBar.paddingBottom
            )

            bottomNav.setPadding(
                bottomNav.paddingLeft,
                bottomNav.paddingTop,
                bottomNav.paddingRight,
                systemBars.bottom
            )

            insets
        }
    }
}