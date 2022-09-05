package com.example.genericapplication

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.genericapplication.services.WebViewService
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URLDecoder


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.init()
    }

    private fun init() {
        this.allowUrlsReading()
        this.loadWebView()
    }

    private fun allowUrlsReading() {
        StrictMode.setThreadPolicy(ThreadPolicy.Builder().permitAll().build())
    }

    private fun loadWebView() {
        this.loadWebViewSettings()
        this.webView!!.loadDataWithBaseURL(null, WebViewService.read("mywebpage.html"),
            "text/html", "UTF-8", null)
        this.loadWebViewClient()
    }

    private fun loadWebViewSettings() {
        this.webView.settings.javaScriptEnabled = true
        this.webView.settings.domStorageEnabled = true
        this.webView.settings.builtInZoomControls = true
        this.webView.settings.allowFileAccess = true
        this.webView.settings.allowContentAccess = true
    }

    private fun loadWebViewClient() {
        this.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                if(!WebViewService.isAppUrl(url))
                    return true

                val webViewIdentifier: String =
                    WebViewService.catchWebViewIdentifier(url) ?: return true

                val intent = Intent(this@MainActivity, MainActivity2::class.java)
                intent.putExtra("webViewIdentifier", webViewIdentifier)
                val data = WebViewService.catchData(url)
                for((k, v) in data)
                    intent.putExtra(k, URLDecoder.decode(v, "UTF-8"))

                startActivityForResult(intent, 0)
                return true
            }
        }
    }
}