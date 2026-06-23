package com.makoto.ecopedia.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "waste_examples",
    foreignKeys = [
        ForeignKey(
            entity = WasteCategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["categoryId"])]
)
data class WasteExampleEntity(
    @PrimaryKey val id: Int,
    val categoryId: Int,
    val name: String,
    val description: String,
    val decompositionTime: String
)
