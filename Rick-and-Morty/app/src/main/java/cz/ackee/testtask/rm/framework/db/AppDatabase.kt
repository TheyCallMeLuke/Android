package cz.ackee.testtask.rm.framework.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cz.ackee.testtask.rm.framework.db.character.CharacterDao
import cz.ackee.testtask.rm.framework.db.character.CharacterDbEntity
import org.koin.dsl.module

@Database(entities = [CharacterDbEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun characterDao(): CharacterDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "CharacterDatabase"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance

                instance
            }
        }
    }
}