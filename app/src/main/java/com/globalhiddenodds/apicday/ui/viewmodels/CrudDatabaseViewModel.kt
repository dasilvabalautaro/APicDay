package com.globalhiddenodds.apicday.ui.viewmodels

import androidx.lifecycle.*
import com.globalhiddenodds.apicday.domain.CrudDatabaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CrudDatabaseViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val crudDatabaseUseCase: CrudDatabaseUseCase
): ViewModel() {
    private val taskResultMutableLive = MutableLiveData<String>()
    val taskResult: LiveData<String> = taskResultMutableLive

    fun insert(){
        viewModelScope.launch {
            val result = crudDatabaseUseCase.insertPicDay()
            taskResultMutableLive.value = "Insert Pic of Day: $result"
        }
    }
}