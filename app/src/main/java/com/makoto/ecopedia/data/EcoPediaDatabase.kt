package com.makoto.ecopedia.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [WasteCategoryEntity::class, WasteExampleEntity::class, ScanHistoryEntity::class],
    version = 3,
    exportSchema = false
)
abstract class EcoPediaDatabase : RoomDatabase() {
    abstract fun wasteDao(): WasteDao
    abstract fun scanHistoryDao(): ScanHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: EcoPediaDatabase? = null

        fun getInstance(context: Context): EcoPediaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EcoPediaDatabase::class.java,
                    "ecopedia_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
