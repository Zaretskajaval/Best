package com.example.top_top

import android.graphics.Typeface
import android.os.Bundle
import android.view.MenuItem
import android.webkit.GeolocationPermissions
import android.webkit.WebChromeClient
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class VeterinaryClinicsMapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_veterinary_clinics_map)
        val toolbar = findViewById<Toolbar>(R.id.toolbarMap)
        setSupportActionBar(toolbar)


        supportActionBar?.apply {
            title = "Ветеринарные клиники"
            setDisplayHomeAsUpEnabled(true) // Показываем кнопку "Назад"
            val titleTextView = toolbar.getChildAt(0) as? TextView
            titleTextView?.let {
                it.textSize = 20f // Размер шрифта в сп
                it.setTypeface(Typeface.DEFAULT_BOLD) // Жирный стиль шрифта
                it.setTextColor(ContextCompat.getColor(this@VeterinaryClinicsMapActivity, R.color.black))
            }
        }
        val url = "https://www.google.com/maps/search/ветеринарные+клиники/"
        val webViewVetClinics = findViewById<android.webkit.WebView>(R.id.webViewVetClinics)
        webViewVetClinics.loadUrl(url)
        webViewVetClinics.webChromeClient = object : WebChromeClient() {
            override fun onGeolocationPermissionsShowPrompt(
                origin: String?,
                callback: GeolocationPermissions.Callback?
            ) {
                callback?.invoke(origin, true, false)
            }

            override fun onGeolocationPermissionsHidePrompt() {
                // Скрыть запрос на разрешение геолокации
            }
        }

        // Включить поддержку JavaScript, чтобы можно было просматривать гугл карту
        val webSettings = webViewVetClinics.settings
        webSettings.javaScriptEnabled = true
        webViewVetClinics.loadUrl(url)
        webSettings.setGeolocationEnabled(true) // Включение поддержки геолокации

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}