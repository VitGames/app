package io.techmeskills.an02onl_plannerapp.screen.main

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import io.techmeskills.an02onl_plannerapp.alarmservice.AlarmReceiver
import io.techmeskills.an02onl_plannerapp.database.Note
import io.techmeskills.an02onl_plannerapp.database.NotesDao
import io.techmeskills.an02onl_plannerapp.database.User
import io.techmeskills.an02onl_plannerapp.database.UserDao
import io.techmeskills.an02onl_plannerapp.support.CoroutineViewModel
import io.techmeskills.an02onl_plannerapp.support.NotificationRepository
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
    private val notificationRepository: NotificationRepository,
) :
    CoroutineViewModel() {

    val currentUserNotesFlow: Flow<List<Note>> =
        sharedPref.userNameFlow().flatMapLatest { userName ->
            userDao.getUserContentFlow(userName!!)
                .map { it?.notes ?: emptyList() }
        }

    fun addNewNote(note: Note) {
        launch {
            val name = sharedPref.userName()
            val id = notesDao.saveNote(
                note.copy(userName = name!!)
            )
            if (note.alarmEnabled) {
                notificationRepository.setNotification(note.copy(id = id))
            }
        }
    }

    fun updateNote(note: Note) {
        launch {
            notesDao.getNoteById(note.id)?.let { oldNote ->
                notificationRepository.unsetNotification(oldNote)
            }
            notesDao.updateNote(note)
            if (note.alarmEnabled) {
                notificationRepository.setNotification(note)
            }
        }
    }

    suspend fun getCurrentUserNotes(): List<Note> {
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
            val userName = sharedPref.userName()
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(sharedPref.context, "Пользователь: $userName удален",
                    Toast.LENGTH_SHORT).show()
            }
            userDao.deleteUser(User(sharedPref.userName()!!))
        }
    }

    suspend fun deleteNoteById(noteId: Long) {
        withContext(Dispatchers.IO) {
            notesDao.getNoteById(noteId)?.let {
                notificationRepository.unsetNotification(it)
                notesDao.deleteNote(it)
            }
        }
    }

     suspend fun updateTextNote(noteId: Long, noteText: String) {
        withContext(Dispatchers.IO) {
            val note = notesDao.getNoteById(noteId)
            if(note != null){
                note.text = noteText
                notesDao.updateNote(note)}
        }
    }
}