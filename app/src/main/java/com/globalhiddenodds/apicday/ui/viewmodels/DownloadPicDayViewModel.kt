package com.globalhiddenodds.apicday.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globalhiddenodds.apicday.datasource.network.GetPicDay
import com.globalhiddenodds.apicday.datasource.network.data.BodyCloud
import com.globalhiddenodds.apicday.datasource.network.data.PicDayCloud
import com.globalhiddenodds.apicday.datasource.network.data.toPicDay
import com.globalhiddenodds.apicday.repository.PicDayDao
import com.globalhiddenodds.apicday.workers.BODY_ANSWER
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DownloadPicDayViewModel @Inject constructor(
    private val picDayDao: PicDayDao
) : ViewModel() {
    private val taskResultMutableLive = MutableLiveData<Boolean>()
    val outputWorkInfo: LiveData<Boolean> = taskResultMutableLive
    var messageError = ""

    fun downPicDay(date: String) {
        viewModelScope.launch {
            val result = withContext(
                viewModelScope
                    .coroutineContext + Dispatchers.IO
            ) {
                try {
                    val picDayCloud: PicDayCloud
                    val body = BODY_ANSWER + date
                    val bodyCloud = BodyCloud(body)
                    val responseCloud = GetPicDay
                        .retrofitPicDayService.searchPicDay(bodyCloud)

                    if (responseCloud != null && responseCloud.success) {
                        picDayCloud = responseCloud.data
                        picDayDao.insert(picDayCloud.toPicDay())
                        return@withContext true

                    } else {
                        messageError = "Null FALSE EMPTY"
                        return@withContext false
                    }

                } catch (throwable: Throwable) {
                    messageError = throwable.message.toString().ifEmpty { "ERROR THROW" }
                    return@withContext false
                }
            }
            taskResultMutableLive.value = result
        }
    }
}
