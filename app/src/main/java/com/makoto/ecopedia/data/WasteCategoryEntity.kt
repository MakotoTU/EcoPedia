package com.makoto.ecopedia.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "waste_categories")
data class WasteCategoryEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val icon: String,           // drawable resource name (e.g. "plastik")
    val description: String,
    val characteristics: String,
    val impact: String,
    val recyclingTips: String
)
