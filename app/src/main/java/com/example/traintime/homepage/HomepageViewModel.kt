package com.example.traintime.homepage

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.traintime.database.TimetableRequest
import com.example.traintime.database.getDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomepageViewModel(application: Application) : AndroidViewModel(application) {
    private val trainTimeDao = getDatabase(application).trainTimeDao
    val timetableRequests = trainTimeDao.getTimetableRequests()

    fun insertTimetableRequest(request: TimetableRequest) {
        viewModelScope.launch(Dispatchers.Default) {
            trainTimeDao.insertTimetableRequest(request)
        }
    }

    fun saveTimetableRequests(requests: List<TimetableRequest>) {
        viewModelScope.launch(Dispatchers.Default) {
            requests.forEachIndexed {
                index, timetableRequest ->
                timetableRequest.listOrder = index
            }
            trainTimeDao.deleteTimetableRequests()
            trainTimeDao.insertTimetableRequests(requests)
        }
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomepageViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HomepageViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}