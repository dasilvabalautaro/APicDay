package com.globalhiddenodds.apicday.ui.screens

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.DatePicker
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Translate
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.globalhiddenodds.apicday.R
import com.globalhiddenodds.apicday.ui.activities.MainActivity
import com.globalhiddenodds.apicday.ui.data.PicDayView
import com.globalhiddenodds.apicday.ui.viewmodels.CrudDatabaseViewModel
import com.globalhiddenodds.apicday.ui.viewmodels.DownloadPicDayViewModel
import com.globalhiddenodds.apicday.ui.viewmodels.TranslateViewModel
import com.globalhiddenodds.apicday.utils.Utils
import com.skydoves.landscapist.coil.CoilImage
import java.util.*
import kotlin.math.roundToInt


@Composable
fun PicDayBody(viewModel: CrudDatabaseViewModel = hiltViewModel()) {
    val dateSearch = MainActivity.dateSearch
    SearchPicDay(
        viewModel = viewModel, date = dateSearch
    )
}

@Composable
fun SearchPicDay(
    viewModel: CrudDatabaseViewModel, date: String
) {
    viewModel.setSearchDate(date)
    val list by viewModel.lisPicDay.observeAsState()
    list?.let {
        if (it.isNotEmpty()) {
            DrawScreen(picDays = it)
        } else {
            val viewModelDown: DownloadPicDayViewModel = hiltViewModel()
            MainActivity.availableOptionPicDay = false
            DownloadPickDay(viewModelDown)
        }
    }
}

@Composable
private fun DrawScreen(
    picDays: List<PicDayView>
) {
    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
        items(items = picDays) { pic ->
            Greet(pic = pic)
        }
    }
}

@Composable
private fun Greet(
    pic: PicDayView
) {
    Card(
        backgroundColor = MaterialTheme.colors.primary,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        CardContentPic(picDay = pic)
    }
}

@Composable
private fun CardContentPic(
    picDay: PicDayView
) {
    val translateLanguage = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
    ) {
        Title(name = picDay.title)
        SubTitle(name = picDay.date)
        if (picDay.media_type == "image") {
            ImagePost(base64 = picDay.base64)
        } else {
            VideoPlayer(videoUrl = picDay.url) //"https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
        }
        CopyRight(name = "Image Credit: NASA")
        BodyText(body = picDay.explanation)
        if (translateLanguage.value) {
            Translate(picDay = picDay)
        }
    }
    ShowCalendar()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp, vertical = 35.dp),
        horizontalArrangement = Arrangement.End
    ) {
        IconButton(onClick = {
            translateLanguage.value = !translateLanguage.value
        }) {
            Icon(
                imageVector = if (translateLanguage.value) Icons.Filled.Refresh else Icons.Filled.Translate,
                contentDescription = "Translate"
            )
        }
    }

}

@Composable
fun Title(name: String) {
    Text(
        text = name,
        style = MaterialTheme.typography.subtitle1,
        modifier = Modifier
            .padding(start = 15.dp, top = 5.dp, end = 60.dp, bottom = 5.dp)
            .wrapContentWidth(Alignment.Start)
    )
}

@Composable
fun CopyRight(name: String) {
    Text(
        text = name,
        style = MaterialTheme.typography.overline,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .wrapContentWidth(Alignment.End)
    )
}

@Composable
fun SubTitle(name: String) {
    Text(
        text = name,
        style = MaterialTheme.typography.subtitle2,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp, vertical = 10.dp)
            .wrapContentWidth(Alignment.CenterHorizontally)
    )
}

@Composable
fun BodyText(body: String) {
    Text(
        text = body,
        style = MaterialTheme.typography.body1,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp, vertical = 10.dp)
            .wrapContentWidth(Alignment.Start)
    )
}

@Composable
fun ImagePost(base64: String) {
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    var size by remember { mutableStateOf(IntSize.Zero) }

    val bitmap = Utils.decodeBase64(base64)

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
            imageModel = bitmap,
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

@Suppress("NAME_SHADOWING")
@Composable
fun ShowCalendar() {
    val context = LocalContext.current
    val cYear: Int
    val cMonth: Int
    val cDay: Int
    var monthStr: String
    var dayStr: String

    val calendar = Calendar.getInstance()
    val maxDate = calendar.timeInMillis
    cYear = calendar.get(Calendar.YEAR)
    cMonth = calendar.get(Calendar.MONTH)
    cDay = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.time = Date()
    val date = remember {
        mutableStateOf(value = "")
    }

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            dayStr = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
            val m = month + 1
            monthStr = if (m < 10) "0$m" else "$m"
            date.value = "$year-$monthStr-$dayStr"
            MainActivity.changeDate(date.value)
            Utils.notify(context, "Click PicOfDay ${MainActivity.dateSearch}")
        }, cYear, cMonth, cDay
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp, vertical = 5.dp),
        horizontalArrangement = Arrangement.End
    ) {
        IconButton(onClick = {
            datePickerDialog.datePicker.maxDate = maxDate
            datePickerDialog.show()
        }) {
            Icon(
                imageVector = Icons.Filled.CalendarMonth,
                contentDescription = "Date"
            )
        }
    }

}

@Composable
fun Translate(picDay: PicDayView) {
    val context = LocalContext.current
    val viewModel: TranslateViewModel = hiltViewModel()
    viewModel.sourceLang.value = TranslateViewModel.Language("en")
    viewModel.targetLang.value = TranslateViewModel.Language("es")

    //
    val availableLang = viewModel.availableLanguages
    if (availableLang.isEmpty()) {
        viewModel.downloadLanguage(viewModel.sourceLang.value!!)
        viewModel.downloadLanguage(viewModel.targetLang.value!!)
        val model by viewModel.availableModels.observeAsState()
        model?.let { list ->
            list.map { Utils.notify(context, it) }
        }
    }

    val dataTranslate = "${picDay.title} * ${picDay.explanation}"
    viewModel.sourceText.value = dataTranslate

    val translate by viewModel.translatedText.observeAsState()
    translate?.let {
        if (it.error != null) {
            it.error!!.localizedMessage?.let { it1 ->
                Utils.notify(context, it1)
            }
        } else if (it.result!!.isNotEmpty()) {
            val result = it.result!!
            val arrResult = result.split("*")
            picDay.title = arrResult[0]
            picDay.explanation = arrResult[1]
        } else {
            println("EMPTY TRANSLATE")
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun VideoPlayer(videoUrl: String) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current

    val widthDp = configuration.screenWidthDp.dp.value
    val widthPx = Utils.dpToPx(context, widthDp)
    val heightPx = widthPx * 1.277f

    val dataUrl =
        "<html style='display:flex; justify-content: center; background-color:CornflowerBlue;'>" +
                "<body style='margin: 0; padding: 0;'>" +
                "<iframe width='" + widthPx.toString() + "px' height='" +
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
                    settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
                    settings.loadWithOverviewMode = true
                    settings.useWideViewPort = true
                }
            },
            update = {
                it.loadData(dataUrl, "text/html", "utf-8")
            }
        )
    }
}

//    val exoPlayer = remember { getVideoPlayer(context, videoUrl)}
//    DisposableEffect(
//        AndroidView(
//            factory = {
//                PlayerView(context).apply {
//                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
//                    player = exoPlayer
//                    layoutParams = FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
//                }
//            })
//    ) {
//        onDispose { exoPlayer.release() }
//    }

//private fun getVideoPlayer(
//    context: Context,
//    videoUrl: String
//): ExoPlayer {
//    val currentItem = 0
//    val playbackPosition = 0L
//    val trackSelector = DefaultTrackSelector(context).apply {
//        setParameters(buildUponParameters().setMaxVideoSizeSd())
//    }
//
//    return ExoPlayer.Builder(context)
//        .setTrackSelector(trackSelector)
//        .build()
//        .also {
//            val customExtractorFactory = ExtractorsFactory {
//                arrayOf(
//                    FlvExtractor(),
//                    MatroskaExtractor(),
//                    Mp4Extractor(),
//                    FragmentedMp4Extractor(),
//                    AdtsExtractor(),
//                    Mp3Extractor()
//                )
//            }
//            val httpDatasourceFactory:
//                    HttpDataSource.Factory =
//                DefaultHttpDataSource.Factory()
//                    .setAllowCrossProtocolRedirects(true)
//            val mediaItem = MediaItem.Builder()
//                .setUri(videoUrl)
//                .setMimeType(MimeTypes.APPLICATION_SS)
//                .build()
//
//            val internetVideoSource = ProgressiveMediaSource
//                .Factory(httpDatasourceFactory, customExtractorFactory)
//                .createMediaSource(mediaItem)
//            it.addMediaSource(internetVideoSource)
//            //it.playWhenReady = true
//            it.seekTo(currentItem, playbackPosition)
//            it.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
//            it.repeatMode = Player.REPEAT_MODE_ONE
//            it.prepare()
//        }
//}

//private fun getYoutubeVideo(context: Context,
//                            playList: List<String>): YouTubePlayerView {
//    val playerView = YouTubePlayerView(context)
//    var player: YouTubePlayer? = null
//
//    val onPlaylistChangeListener = object : YouTubePlayer.PlaylistEventListener {
//        override fun onPlaylistEnded() {}
//        override fun onPrevious() {}
//        override fun onNext() {}
//    }
//    val youtubeApiInitializedListener = object : YouTubePlayer.OnInitializedListener {
//        override fun onInitializationSuccess(p0: YouTubePlayer.Provider?, p1: YouTubePlayer?, p2: Boolean) {
//            player = p1
//            player?.setPlaylistEventListener(onPlaylistChangeListener)
//            player?.loadVideos(playList)
//        }
//
//        override fun onInitializationFailure(
//            p0: YouTubePlayer.Provider?,
//            youTubeInitializationResult: YouTubeInitializationResult?) {
//            val errorMessage = "There was an error initializing the YoutubePlayer ($youTubeInitializationResult)"
//            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
//        }
//    }
//
//    playerView.initialize(context.getString(R.string.GOOGLE_API_KEY),
//        youtubeApiInitializedListener)
//    return playerView
//}
