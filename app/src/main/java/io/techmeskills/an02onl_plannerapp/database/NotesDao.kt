package io.techmeskills.an02onl_plannerapp.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
abstract class NotesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun saveNote(note: Note): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun saveNotes(notes: List<Note>): List<Long>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun updateNote(note: Note)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun updateNotes(notes: List<Note>)

    @Delete
    abstract fun deleteNote(note: Note)

    @Delete
    abstract fun deleteNotes(notes: List<Note>)

    @Query("DELETE FROM notes")
    abstract fun clearTable()

    @Query("SELECT * FROM notes")
    abstract fun getAllNotes(): List<Note>

//    @Query("SELECT * FROM notes WHERE userId == :userId ORDER BY id DESC")
//    abstract fun getAllNotesFlowByUserId(userId: Long): Flow<List<Note>>
//
//    @Query("SELECT * FROM notes WHERE userId == :userId ORDER BY id DESC")
//    abstract fun getAllNotesByUserId(userId: Long): List<Note>

    @Query("SELECT * FROM notes WHERE id == :noteId LIMIT 1")
    abstract fun getNoteById(noteId: Long): Note?

    @Query("UPDATE notes SET fromCloud = 1")
    abstract fun setAllNotesSyncWithCloud()
}