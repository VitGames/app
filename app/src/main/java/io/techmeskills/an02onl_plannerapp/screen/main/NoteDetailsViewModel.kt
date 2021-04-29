package io.techmeskills.an02onl_plannerapp.screen.main

import io.techmeskills.an02onl_plannerapp.database.Note
import io.techmeskills.an02onl_plannerapp.database.NotesDao
import io.techmeskills.an02onl_plannerapp.support.CoroutineViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoteDetailsViewModel(private val notesDao: NotesDao, private val sharedPref: SharedPref) :
    CoroutineViewModel() {

    val currentUserNotesFlow: Flow<List<Note>> =
        sharedPref.userIdFlow().flatMapLatest { userId -> //получаем из сеттингов текущий айди юзера
            notesDao.getAllNotesFlowByUserId(userId) //получаем заметки по айди юзера
        }

    fun addNewNote(note: Note) {
        launch {
            notesDao.saveNote(Note(
                text = note.text,
                date = note.date,
                userId = sharedPref.userId()
            ))
        }
    }

    fun updateNote(note: Note) {
        launch {
            notesDao.updateNote(Note(
                id = note.id,
                text = note.text,
                date = note.date,
                userId = sharedPref.getUserId()
            ))
        }
    }

    suspend fun getCurrentUserNotes(): List<Note> {
        return notesDao.getAllNotesByUserId(sharedPref.userId())
    }

    suspend fun setAllNotesSyncWithCloud() {
        withContext(Dispatchers.IO) {
            notesDao.setAllNotesSyncWithCloud()
        }
    }
    suspend fun saveNotes(notes: List<Note>) {
        withContext(Dispatchers.IO) {
            notesDao.saveNotes(notes)
        }
    }
}