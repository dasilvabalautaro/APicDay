package com.globalhiddenodds.apicday.domain

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.work.*
import com.globalhiddenodds.apicday.workers.DownloadPicDayWorker
import com.globalhiddenodds.apicday.workers.KEY_PARAM_DATE
import com.globalhiddenodds.apicday.workers.TAG_DOWN
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DownloadPicOfDayUseCase @Inject constructor(
    @ApplicationContext val context: Context
) {
    private val workManager = WorkManager.getInstance(context)

    val workInfo: LiveData<List<WorkInfo>> = workManager
        .getWorkInfosByTagLiveData(TAG_DOWN)

    suspend fun downPicOfDay(date: String) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()

        val workRequest = OneTimeWorkRequest.Builder(DownloadPicDayWorker::class.java)
            .setInputData(workDataOf(KEY_PARAM_DATE to date))
            .setConstraints(constraints)
            .addTag(TAG_DOWN)
            .build()
        workManager.enqueue(workRequest).await()

    }

    internal fun cancelWork() {
        //workManager.cancelUniqueWork(DOWNLOAD_PIC_WORK_NAME)
        workManager.cancelAllWork()
    }
}