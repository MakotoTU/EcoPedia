package com.makoto.ecopedia

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.makoto.ecopedia.data.ScanHistoryEntity
import com.makoto.ecopedia.data.ScanHistoryRepository
import kotlinx.coroutines.launch

class ScanHistoryViewModel(private val repository: ScanHistoryRepository) : ViewModel() {

    val allHistory = repository.allHistory

    fun insert(history: ScanHistoryEntity) = viewModelScope.launch {
        repository.insert(history)
    }

    fun deleteById(id: Int) = viewModelScope.launch {
        repository.deleteById(id)
    }

    fun clearAll() = viewModelScope.launch {
        repository.clearAll()
    }
}

class ScanHistoryViewModelFactory(private val repository: ScanHistoryRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScanHistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ScanHistoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
