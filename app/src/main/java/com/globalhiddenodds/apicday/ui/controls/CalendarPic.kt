package com.globalhiddenodds.apicday.ui.controls

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.globalhiddenodds.apicday.ui.activities.MainActivity
import com.globalhiddenodds.apicday.utils.Utils
import java.util.*

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
