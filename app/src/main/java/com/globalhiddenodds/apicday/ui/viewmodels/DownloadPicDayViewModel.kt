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
    val outputWorkInfo: LiveData<List<WorkInfo>> = downloadPicOfDayUseCase.workInfo

    fun downPicDay(date: String){
        viewModelScope. launch {
            downloadPicOfDayUseCase.downPicOfDay(date)
        }
    }
}