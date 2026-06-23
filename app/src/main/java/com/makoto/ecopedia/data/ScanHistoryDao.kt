package com.makoto.ecopedia.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ScanHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScan(history: ScanHistoryEntity)

    @Query("SELECT * FROM scan_history ORDER BY scannedAt DESC")
    fun getAllHistory(): Flow<List<ScanHistoryEntity>>

    @Query("DELETE FROM scan_history WHERE id = :id")
    suspend fun deleteHistoryById(id: Int)

    @Query("DELETE FROM scan_history")
    suspend fun clearAllHistory()
}
