package com.example.top_top

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.VideoView

class LoadActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load2)
        val videoViewLoad: VideoView = findViewById(R.id.videoViewLoad)
        val uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.loadgif)
        videoViewLoad.setVideoURI(uri)
       videoViewLoad.setOnPreparedListener { mp ->
           mp.isLooping = true
       }
        videoViewLoad.start()

    }

}