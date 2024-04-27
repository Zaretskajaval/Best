package com.example.top_top

import android.os.Bundle
import android.view.MenuItem
import android.webkit.GeolocationPermissions
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import androidx.appcompat.app.AppCompatActivity


class MapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarMap)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        val webView = findViewById<android.webkit.WebView>(R.id.webView)
        // Получаем настройки WebView
        val webSettings: WebSettings = webView.settings

        // Включаем поддержку JavaScript
        webSettings.javaScriptEnabled = true

        // Формируем URL с указанными координатами
        val lat = "59.839129057706494"
        val lng = "30.40579095474662"
        val url = "https://www.google.com/maps?q=$lat,$lng&z=14"

        webView.loadUrl(url)//Ниже код для док-в
          //   val patOnMap = intent.getStringExtra("PAT_ON_MAP")
       // if (!patOnMap.isNullOrEmpty()) {
        //    val url = if (patOnMap.startsWith("http://") || patOnMap.startsWith("https://")) {
        //        patOnMap
        //    } else {
         //       "http://$patOnMap"
         //   }
         //   webView.loadUrl(url)
       // } else {
            //!!!!!!!!!!1 Если значение patOnMap отсутствует или пустое, загружаем стандартную веб-страницу-изменить в документации
         //   val defaultMapUrl = "https://www.google.com/maps?q=59.8359652,30.3793638&z=14"
          //    webView.loadUrl(defaultMapUrl)
      //  }


    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}