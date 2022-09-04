package com.example.genericapplication

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import com.example.genericapplication.factories.DotenvFactory
import com.example.genericapplication.services.VideoProviderService
import com.example.genericapplication.services.WebViewService
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ext.ima.ImaAdsLoader
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_main.textView
import kotlinx.android.synthetic.main.activity_main.webView
import kotlinx.android.synthetic.main.activity_main2.*

class MainActivity2 : AppCompatActivity() {
    private var player: ExoPlayer? = null
    private var adsLoader: ImaAdsLoader? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        this.init()
    }

    private fun init() {
        // Create an AdsLoader.
        this.adsLoader = ImaAdsLoader.Builder(this).build()
        this.textView.text = intent.getStringExtra("title")
        this.loadWebView()
    }

    private fun loadWebView() {
        this.loadWebViewSettings()
        this.webView!!.loadDataWithBaseURL(null, WebViewService.read(intent.getStringExtra("webViewIdentifier")), "text/html", "UTF-8", null)
        this.webView.webViewClient = object : WebViewClient() {}
    }

    private fun loadWebViewSettings() {
        this.webView.settings.javaScriptEnabled = true
        this.webView.settings.domStorageEnabled = true
        this.webView.settings.builtInZoomControls = true
        this.webView.settings.allowFileAccess = true
        this.webView.settings.allowContentAccess = true
    }

    private fun releasePlayer() {
        adsLoader!!.setPlayer(null)
        playerView!!.player = null
        player!!.release()
        player = null
    }

    override fun onStart() {
        super.onStart()
        if(Util.SDK_INT > 23) {
            initializePlayer()
            if(playerView != null)
                playerView!!.onResume()
        }
    }

    override fun onResume() {
        super.onResume()
        if(Util.SDK_INT <= 23 || player == null) {
            initializePlayer()
            if(playerView != null)
                playerView!!.onResume()
        }
    }

    override fun onPause() {
        super.onPause()
        if(Util.SDK_INT <= 23) {
            if(playerView != null)
                playerView!!.onPause()
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if(Util.SDK_INT > 23) {
            if(playerView != null)
                playerView!!.onPause()
            releasePlayer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        adsLoader!!.release()
    }

    private fun initializePlayer() {
        val videoUrl: String? = VideoProviderService.buildUrl(intent.getStringExtra("defaultVideoIdentifier"))
        val contentUri = Uri.parse(videoUrl)
        val adTagUri = Uri.parse(DotenvFactory.getInstance()["AD_TAG_URL"])
        // Create a SimpleExoPlayer and set it as the player for content and ads.
        player = ExoPlayer.Builder(this)
            .setMediaSourceFactory(buildMediaSource())
            .build()
        playerView!!.player = player
        adsLoader!!.setPlayer(player)

        val mediaItem = MediaItem.Builder()
            .setUri(contentUri)
            .setAdsConfiguration(
                MediaItem.AdsConfiguration.Builder(adTagUri)
                    .setAdsId(adTagUri)
                    .build()
            )
            .build()

        // Prepare the content and ad to be played with the SimpleExoPlayer.
        player!!.setMediaItem(mediaItem)
        player!!.prepare()

        // Set PlayWhenReady. If true, content and ads will autoplay.
        player!!.setPlayWhenReady(false)
    }

    //creating mediaSource
    private fun buildMediaSource(): MediaSourceFactory {
        return DefaultMediaSourceFactory(this)
            .setAdsLoaderProvider { unusedAdTagUri: MediaItem.AdsConfiguration? -> adsLoader }
            .setAdViewProvider(playerView)
    }
}