package com.globalhiddenodds.apicday.domain

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.work.*
import com.globalhiddenodds.apicday.workers.*
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DownloadPicOfDayUseCase @Inject constructor(
    @ApplicationContext val context: Context
) {
    private val workManager = WorkManager.getInstance(context)
    val workInfo: LiveData<List<WorkInfo>> = workManager
        .getWorkInfosByTagLiveData(TAG_TRANSFORM_IMAGE)

    fun downPicOfDay(date: String){
        val data: Data = Data.Builder()
            .putString(KEY_PARAM_DATE, date)
            .build()
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()
        val transformUrlTo64 = OneTimeWorkRequest
            .Builder(TransformUrlTo64Worker::class.java)
            .setConstraints(constraints)
            .addTag(TAG_TRANSFORM_IMAGE)
            .build()

        workManager.beginUniqueWork(
            DOWNLOAD_PIC_WORK_NAME, ExistingWorkPolicy.KEEP,
            OneTimeWorkRequest.Builder(DownloadPicDayWorker::class.java)
                .setInputData(data)
                .setConstraints(constraints)
                .addTag(TAG_DOWN)
                .build()).then(transformUrlTo64).enqueue()
    }

    internal fun cancelWork(){
        workManager.cancelUniqueWork(DOWNLOAD_PIC_WORK_NAME)
    }
}