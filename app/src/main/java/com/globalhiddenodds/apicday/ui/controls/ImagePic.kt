package com.globalhiddenodds.apicday.ui.controls

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.globalhiddenodds.apicday.R
import com.skydoves.landscapist.coil.CoilImage
import kotlin.math.roundToInt

@Composable
fun ImagePost(url: String) {
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
            placeHolder = ImageVector.vectorResource(id = R.drawable.loading_img),
            error = ImageVector.vectorResource(id = R.drawable.ic_broken_image),
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                .align(Alignment.Center)
                .graphicsLayer(
                    scaleX = maxOf(1f, minOf(3f, scale)),
                    scaleY = maxOf(1f, minOf(3f, scale))
                )
        )
    }
}

@Composable
fun ImageBackground(
    option: Int,
    url: String
) {
    var model: Any? = null
    when (option) {
        1 -> model = url.toUri().buildUpon().scheme("https").build()
        2 -> model = (R.drawable.background)
        3 -> model = (R.drawable.background)
    }
    CoilImage(
        imageModel = model,
        modifier = Modifier.fillMaxWidth(),
        contentDescription = "background image",
        contentScale = ContentScale.Crop
    )

}
