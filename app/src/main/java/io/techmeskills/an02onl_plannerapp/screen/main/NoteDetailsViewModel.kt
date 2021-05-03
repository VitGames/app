package io.techmeskills.an02onl_plannerapp.screen.main

import io.techmeskills.an02onl_plannerapp.database.Note
import io.techmeskills.an02onl_plannerapp.database.NotesDao
import io.techmeskills.an02onl_plannerapp.database.User
import io.techmeskills.an02onl_plannerapp.database.UserDao
import io.techmeskills.an02onl_plannerapp.support.CoroutineViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoteDetailsViewModel(
    private val notesDao: NotesDao,
    private val sharedPref: SharedPref,
    private val userDao: UserDao,
) :
    CoroutineViewModel() {

    val currentUserNotesFlow: Flow<List<Note>> =
        sharedPref.userNameFlow().flatMapLatest { userName ->
            userDao.getUserContentFlow(userName!!)
                .map { it?.notes ?: emptyList() }
        }

    fun addNewNote(note: Note) {
        launch {
            notesDao.saveNote(Note(
                text = note.text,
                date = note.date,
                userName = sharedPref.userName()!!
            ))
        }
    }

    fun updateNote(note: Note) {
        launch {
            notesDao.updateNote(Note(
                id = note.id,
                text = note.text,
                date = note.date,
                userName = sharedPref.userName()!!
            ))
        }
    }

    suspend fun getCurrentUserNotes(): List<Note> {
//        return notesDao.getAllNotesByUserId(sharedPref.userId())
        return userDao.getUserContent(sharedPref.userName()!!)?.notes ?: emptyList()
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

    suspend fun clearDataBase() {
        withContext(Dispatchers.IO) {
            notesDao.clearTable()
        }
    }

    suspend fun deleteUser() {
        withContext(Dispatchers.IO) {
            userDao.deleteUser(User(sharedPref.userName()!!))
        }
    }
}