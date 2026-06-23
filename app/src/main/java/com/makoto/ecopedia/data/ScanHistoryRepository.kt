package com.makoto.ecopedia.data

import kotlinx.coroutines.flow.Flow

class ScanHistoryRepository(private val scanHistoryDao: ScanHistoryDao) {

    val allHistory: Flow<List<ScanHistoryEntity>> = scanHistoryDao.getAllHistory()

    suspend fun insert(history: ScanHistoryEntity) {
        scanHistoryDao.insertScan(history)
    }

    suspend fun deleteById(id: Int) {
        scanHistoryDao.deleteHistoryById(id)
    }

    suspend fun clearAll() {
        scanHistoryDao.clearAllHistory()
    }
}
