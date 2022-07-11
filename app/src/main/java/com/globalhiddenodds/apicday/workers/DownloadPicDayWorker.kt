package com.globalhiddenodds.apicday.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.globalhiddenodds.apicday.R
import com.globalhiddenodds.apicday.datasource.network.GetPicDay
import com.globalhiddenodds.apicday.datasource.network.data.PicDayCloud
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DownloadPicDayWorker @Inject constructor(
    @ApplicationContext val context: Context,
    workerParams: WorkerParameters
): CoroutineWorker(context, workerParams) {
    private var isSuccess = false

    override suspend fun doWork(): Result {
        makeStatusNotification(context.getString(R.string.lbl_down_pic_day),
        applicationContext)

        return withContext(Dispatchers.IO) {
            return@withContext try {
                val date = inputData.getString(KEY_PARAM_DATE)
                if(!date.isNullOrEmpty()){
                    picDayCloud = GetPicDay
                        .retrofitPicDayService.searchPicDay(date)!!
                    isSuccess = true
                    val outputData: Data = workDataOf(KEY_IS_SUCCESS to isSuccess)
                    Result.success(outputData)
                } else {
                     Result.failure()
                }

            }catch (throwable: Throwable) {
                Result.failure()
            }
        }
    }

    companion object {
        lateinit var picDayCloud: PicDayCloud
    }

}