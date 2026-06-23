package com.makoto.ecopedia.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "scan_history",
    foreignKeys = [
        ForeignKey(
            entity = WasteCategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.SET_NULL // Jika kategori dihapus, riwayat tetap ada tapi kategori null
        )
    ],
    indices = [Index(value = ["categoryId"])]
)
data class ScanHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val barcode: String,
    val productName: String,
    val categoryId: Int?,
    val ecoScore: String,
    val imagePath: String?,    // Path gambar di local storage
    val scannedAt: Long        // Timestamp saat di-scan
)
