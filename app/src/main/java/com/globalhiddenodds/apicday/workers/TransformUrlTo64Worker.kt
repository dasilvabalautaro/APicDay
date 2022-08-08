package com.globalhiddenodds.apicday.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.globalhiddenodds.apicday.R
import com.globalhiddenodds.apicday.utils.Utils
import com.globalhiddenodds.apicday.workers.DownloadPicDayWorker.Companion.picDayCloud
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TransformUrlTo64Worker @Inject constructor(
    @ApplicationContext val context: Context,
    workerParams: WorkerParameters
): CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        makeStatusNotification(context.getString(R.string.lbl_transform_to_64),
        applicationContext)

        return withContext(Dispatchers.IO) {
            return@withContext try {
                val isSuccess = inputData.getBoolean(KEY_IS_SUCCESS, false)
                if (isSuccess && picDayCloud.url.isNotEmpty()){
                    if (picDayCloud.media_type == "image"){
                        val url = picDayCloud.url
                        val base64 = Utils.urlToBase64(applicationContext, url)
                        if (base64 != null){
                            picDayCloud.base64 = base64
                            val outputData: Data = workDataOf(KEY_IS_SUCCESS to isSuccess)
                            Result.success(outputData)
                        }
                        else {
                            Result.failure()
                        }
                    }
                    else {
                        val outputData: Data = workDataOf(KEY_IS_SUCCESS to isSuccess)
                        Result.success(outputData)
                    }

                }
                else {
                    Result.failure()
                }

            }catch (throwable: Throwable) {
                Result.failure()
            }
        }
    }

}