package com.makoto.ecopedia.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WasteDao {
    @Query("SELECT * FROM waste_categories ORDER BY id")
    suspend fun getAllCategories(): List<WasteCategoryEntity>

    @Query("SELECT * FROM waste_categories WHERE id = :id")
    suspend fun getCategoryById(id: Int): WasteCategoryEntity?

    @Query("SELECT * FROM waste_categories WHERE name = :name")
    suspend fun getCategoryByName(name: String): WasteCategoryEntity?

    @Query("SELECT * FROM waste_examples WHERE categoryId = :categoryId ORDER BY id")
    suspend fun getExamplesByCategoryId(categoryId: Int): List<WasteExampleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<WasteCategoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExamples(examples: List<WasteExampleEntity>)

    @Query("SELECT COUNT(*) FROM waste_categories")
    suspend fun getCategoryCount(): Int
}
