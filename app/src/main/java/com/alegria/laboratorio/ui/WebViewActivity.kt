package com.alegria.laboratorio.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alegria.laboratorio.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle: Bundle? = intent.extras
        val url = bundle?.get("url")
        binding.webView.loadUrl("https://docs.google.com/viewer?url=${url.toString()}")
    }
}