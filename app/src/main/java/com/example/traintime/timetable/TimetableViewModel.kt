package com.example.traintime.timetable

import android.app.Application
import androidx.lifecycle.*
import com.example.traintime.database.getDatabase
import com.example.traintime.domain.TrainInfo
import com.example.traintime.repository.TrainInfoRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

enum class LoadingState {
    LOADING, SUCCESS, NETWORK_ERROR
}

class TimetableViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = TrainInfoRepository(getDatabase(application))
    private val _trainInfos = MutableLiveData<List<TrainInfo>>()
    val trainInfos: LiveData<List<TrainInfo>> = _trainInfos

    private val _loadingState = MutableLiveData<LoadingState>()
    val loadingState: LiveData<LoadingState> = _loadingState

    private suspend fun refresh(from: String, to: String) {
        try {
            repository.queryTimetable(from, to)
            _trainInfos.value = repository.getTrainInfos("${from}-${to}")
            _loadingState.value = LoadingState.SUCCESS
        } catch (e: HttpException) {
            _loadingState.value = LoadingState.NETWORK_ERROR
        } catch (e: IOException) {
            _loadingState.value = LoadingState.NETWORK_ERROR
        }
    }

    fun loadData(from: String, to: String, forceToRefresh: Boolean = false) {
        if (forceToRefresh || _trainInfos.value.isNullOrEmpty()) {
            _loadingState.value = LoadingState.LOADING

            viewModelScope.launch {
                // Load from database
                if (!forceToRefresh)
                    _trainInfos.value = repository.getTrainInfos("${from}-${to}")

                // ForceToRefresh or no data from database, load from internet
                if (forceToRefresh || _trainInfos.value.isNullOrEmpty()) {
                    refresh(from, to)
                } else {
                    _loadingState.value = LoadingState.SUCCESS
                }
            }
        }
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TimetableViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return TimetableViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}