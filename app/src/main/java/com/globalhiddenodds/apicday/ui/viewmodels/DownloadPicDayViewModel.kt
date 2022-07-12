package com.globalhiddenodds.apicday.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import com.globalhiddenodds.apicday.domain.DownloadPicOfDayUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DownloadPicDayViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val downloadPicOfDayUseCase: DownloadPicOfDayUseCase
): ViewModel() {
    private val viewStatus = "VIEW_STATUS_DOWN"
    val outputWorkInfo: LiveData<List<WorkInfo>> = downloadPicOfDayUseCase.workInfo
    val status: LiveData<Boolean> by lazy { handle.getLiveData(viewStatus) }

    init {
        handle[viewStatus] = false
    }
    fun downPicDay(date: String){
        viewModelScope. launch {
            downloadPicOfDayUseCase.downPicOfDay(date)
            handle[viewStatus] = true
        }
    }
}