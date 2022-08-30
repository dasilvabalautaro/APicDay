package com.globalhiddenodds.apicday.ui.controls

import android.annotation.SuppressLint
import android.net.http.SslError
import android.webkit.SslErrorHandler
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun VideoPlayer(videoUrl: String) {
    val context = LocalContext.current
    val resources = context.resources
    val widthPx = resources.displayMetrics.widthPixels
    val heightPx = widthPx * 1.277f

    val dataUrl =
        "<html style='background-color:CornflowerBlue;'>" +
                "<body style='margin: 0; padding: 0;'>" +
                "<iframe width='100%' height='" +
                heightPx.toString() +
                "px' src='" + videoUrl +
                "' fullscreen/>" +
                "</body>" +
                "</html>"
    Box(
        modifier = Modifier
            .clip(RectangleShape)
    ) {
        AndroidView(
            modifier = Modifier
                .fillMaxSize(),
            factory = {
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    settings.cacheMode = WebSettings.LOAD_DEFAULT
                    settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
                    settings.loadWithOverviewMode = true
                    settings.useWideViewPort = true
                    settings.domStorageEnabled = true
                    webViewClient = object: WebViewClient() {
                        override fun onReceivedSslError(
                            view: WebView?,
                            handler: SslErrorHandler?,
                            error: SslError?
                        ) {
                            //super.onReceivedSslError(view, handler, error)
                            if (error.toString() == "SSLError") {
                                handler?.cancel()
                            }
                        }
                    }

                }
            },
            update = {
                it.clearCache(true)
                it.clearHistory()
                it.clearMatches()
                try {
                    it.loadData(dataUrl, "text/html", "utf-8")
                } catch (ex: Exception){
                    println(ex.message)
                }
            }
        )
    }
}
