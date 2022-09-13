package com.globalhiddenodds.apicday.ui

import android.annotation.SuppressLint
import android.net.http.SslError
import android.webkit.SslErrorHandler
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import com.globalhiddenodds.apicday.R
import com.skydoves.landscapist.coil.CoilImage
import dev.patrickgold.compose.tooltip.tooltip
import kotlin.math.roundToInt

@Composable
fun MediaImage(url: String, type: String, onSendZoomImage: () -> Unit){
    when(type){
        "image" -> ImageCentral(url = url, onSendZoomImage)
        "video" -> VideoPlayer(videoUrl = url)
        else -> {}
    }
}
@Composable
fun ImageCentral(url: String, onSendZoomImage: () -> Unit) {
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    var size by remember { mutableStateOf(IntSize.Zero) }

    val imageUrl = url.toUri().buildUpon().scheme("https").build()
    Box(modifier = Modifier
        .padding(5.dp)
        .clip(RectangleShape)
        .onSizeChanged { size = it }
        .animateContentSize(
            animationSpec = tween(
                durationMillis = 3000,
                delayMillis = 300,
                easing = FastOutSlowInEasing
            )
        )
        .run {
            this.pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale = (scale * zoom).coerceIn(1f, 3f)
                    val maxX = (size.width * (scale - 1)) / 2
                    val minX = -maxX
                    offsetX = maxOf(minX, minOf(maxX, offsetX + pan.x))
                    val maxY = (size.height * (scale - 1)) / 2
                    val minY = -maxY
                    offsetY = maxOf(minY, minOf(maxY, offsetY + pan.y))
                }
            }
        }
    ) {
        CoilImage(
            imageModel = imageUrl,
            placeHolder = ImageVector.vectorResource(id = R.drawable.ic_loading),
            error = ImageVector.vectorResource(id = R.drawable.ic_broken),
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                .align(Alignment.Center)
                .graphicsLayer(
                    scaleX = maxOf(1f, minOf(3f, scale)),
                    scaleY = maxOf(1f, minOf(3f, scale))
                ).clickable(onClick = onSendZoomImage)
                .tooltip(stringResource(R.string.tip_high_resolution)),
            contentScale = ContentScale.Fit
        )
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun VideoPlayer(videoUrl: String) {
    val context = LocalContext.current
    val resources = context.resources
    val widthPx = resources.displayMetrics.widthPixels
    val heightPx = widthPx * 1.277f

    val dataUrl =
        "<html><body style='margin: 0; padding: 0;'>" +
                "<iframe width='100%' height='" + heightPx.toString() +
                "px' src='" + videoUrl + "' fullscreen/></body></html>"
    Box(
        modifier = Modifier
            .clip(RectangleShape)
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 3000,
                    delayMillis = 300,
                    easing = FastOutSlowInEasing
                )
            )
    ) {
        AndroidView(
            modifier = Modifier
                .fillMaxSize(),
            factory = {
                WebView(it).apply {
                    settings.javaScriptEnabled = true
                    settings.cacheMode = WebSettings.LOAD_DEFAULT
                    settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
                    settings.loadWithOverviewMode = true
                    settings.useWideViewPort = true
                    settings.domStorageEnabled = true
                    webViewClient = object : WebViewClient() {
                        override fun onReceivedSslError(
                            view: WebView?,
                            handler: SslErrorHandler?,
                            error: SslError?
                        ) {
                            super.onReceivedSslError(view, handler, error)
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
                } catch (ex: Exception) {
                    println(ex.message)
                }
            }
        )
    }
}
