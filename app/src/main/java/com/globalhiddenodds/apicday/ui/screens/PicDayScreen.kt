package com.globalhiddenodds.apicday.ui.screens

import android.app.DatePickerDialog
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.globalhiddenodds.apicday.R
import com.globalhiddenodds.apicday.ui.activities.MainActivity
import com.globalhiddenodds.apicday.ui.activities.MainActivity.Companion.dateSearch
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
    val dateSearch = dateSearch
    SearchPicDay(
        viewModel = viewModel, date = dateSearch
    )
}

@Composable
fun SearchPicDay(
    viewModel: CrudDatabaseViewModel, date: String
) {
    val context = LocalContext.current
    viewModel.setSearchDate(date)
    val list by viewModel.lisPicDay.observeAsState()
    list?.let {
        if (it.isNotEmpty()) {
            Utils.notify(context, "Image downloaded")
            DrawScreen(picDays = it)
        } else {
            Utils.notify(context, "Image for download")
            val viewModelDown: DownloadPicDayViewModel = hiltViewModel()
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
        ImagePost(base64 = picDay.base64)
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
    val year: Int
    val month: Int
    val day: Int
    var monthStr: String
    var dayStr: String

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    year = calendar.get(Calendar.YEAR)
    month = calendar.get(Calendar.MONTH)
    day = calendar.get(Calendar.DAY_OF_MONTH)
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
            Utils.notify(context, "Click PicOfDay $dateSearch")
        }, year, month, day
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp, vertical = 5.dp),
        horizontalArrangement = Arrangement.End
    ) {
        IconButton(onClick = {
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

    //

    val dataTranslate = "${picDay.title} * ${picDay.explanation}"
    viewModel.sourceText.postValue(dataTranslate)

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