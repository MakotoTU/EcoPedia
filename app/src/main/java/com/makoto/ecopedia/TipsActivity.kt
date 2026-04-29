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

class TipsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tips)

        setupNavigation()
        setupThemeToggle()
        setupEdgeToEdge()
    }

    private fun setupNavigation() {
        val navHome = findViewById<View>(R.id.navHome)
        val navTips = findViewById<View>(R.id.navTips)
        
        // Set Tips as selected
        navTips.isSelected = true

        navHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
            finish()
        }
    }

    private fun setupThemeToggle() {
        // We'll reuse the same toggle logic if we add the button to activity_tips.xml
        // For now, it respects the global setting from MainActivity
    }

    private fun setupEdgeToEdge() {
        val topBar = findViewById<View>(R.id.topBarContent)
        val bottomNav = findViewById<View>(R.id.bottomNavContent)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tipsScrollView)) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            topBar.setPadding(topBar.paddingLeft, systemBars.top, topBar.paddingRight, topBar.paddingBottom)
            bottomNav.setPadding(bottomNav.paddingLeft, bottomNav.paddingTop, bottomNav.paddingRight, systemBars.bottom)
            insets
        }
    }
}