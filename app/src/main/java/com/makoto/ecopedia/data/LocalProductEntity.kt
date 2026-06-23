package com.makoto.ecopedia.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "local_products")
data class LocalProductEntity(
    @PrimaryKey val barcode: String,
    val productName: String,
    val categoryId: Int,
    val ecoScore: String?,
    val imageUrl: String?
)
