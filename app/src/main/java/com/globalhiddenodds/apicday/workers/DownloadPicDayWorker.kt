package com.globalhiddenodds.apicday.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.globalhiddenodds.apicday.R
import com.globalhiddenodds.apicday.datasource.network.GetPicDay
import com.globalhiddenodds.apicday.datasource.network.data.BodyCloud
import com.globalhiddenodds.apicday.datasource.network.data.PicDayCloud
import com.globalhiddenodds.apicday.repository.PicDayDao
import com.globalhiddenodds.apicday.utils.Utils
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class DownloadPicDayWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val picDayDao: PicDayDao
) : CoroutineWorker(context, workerParams) {
    private var isSuccess = true

    override suspend fun doWork(): Result {
        makeStatusNotification(
            applicationContext.getString(R.string.lbl_down_pic_day),
            applicationContext
        )

        return withContext(Dispatchers.IO) {
            return@withContext try {
                val date: String? = inputData.getString(KEY_PARAM_DATE)

                if (!date.isNullOrEmpty()) {
                    val body = BODY_ANSWER + date
                    val bodyCloud = BodyCloud(body)
                    val responseCloud = GetPicDay
                        .retrofitPicDayService.searchPicDay(bodyCloud)
                    if (responseCloud != null &&
                        responseCloud.success &&
                        responseCloud.data.url.isNotEmpty()
                    ) {
                        picDayCloud.date = responseCloud.data.date
                        picDayCloud.explanation = responseCloud.data.explanation
                        picDayCloud.media_type = responseCloud.data.media_type
                        picDayCloud.title = responseCloud.data.title
                        picDayCloud.url = responseCloud.data.url
                        picDayCloud.hdurl = responseCloud.data.hdurl.ifEmpty { "null" }
                        picDayCloud.copyright = responseCloud.data.copyright.ifEmpty { "null" }
                        picDayCloud.service_version =
                            responseCloud.data.service_version.ifEmpty { "null" }
                        if (picDayCloud.media_type == "image") {
                            val base64 = Utils.urlToBase64(applicationContext, picDayCloud.url)
                            if (base64 != null) {
                                picDayCloud.base64 = base64
                            } else {
                                isSuccess = false
                            }
                        }
                        if (isSuccess) {
                            picDayDao.insert(picDayCloud.toPicDay())
                            picDayCloud = PicDayCloud()
                            val outputData: Data = workDataOf(KEY_IS_SUCCESS to isSuccess)
                            Result.success(outputData)
                        } else {
                            Result.failure()
                        }

                    } else {
                        Result.failure()
                    }

                } else {
                    Result.failure()
                }

            } catch (throwable: Throwable) {
                println(throwable.message)
                Result.failure()
            }
        }
    }

    companion object {
        var picDayCloud: PicDayCloud = PicDayCloud()

    }

}