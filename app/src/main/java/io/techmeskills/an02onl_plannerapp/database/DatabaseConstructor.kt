package io.techmeskills.an02onl_plannerapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        Note::class,
        User::class
    ],
    version = 3,
    exportSchema = false
)

abstract class PlannerDatabase : RoomDatabase() {
    abstract fun notesDao(): NotesDao
    abstract fun userDao(): UserDao
}

object DatabaseConstructor {
    fun create(context: Context): PlannerDatabase =
        Room.databaseBuilder(
            context,
            PlannerDatabase::class.java,
            "io.techmeskills.an02onl_plannerapp.db"
        ).build()
}