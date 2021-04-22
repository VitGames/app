package io.techmeskills.an02onl_plannerapp.screen.main

import io.techmeskills.an02onl_plannerapp.database.Note
import io.techmeskills.an02onl_plannerapp.database.NotesDao
import io.techmeskills.an02onl_plannerapp.support.CoroutineViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class NoteDetailsViewModel(private val notesDao: NotesDao, private val sharedPref: SharedPref) :
    CoroutineViewModel() {

    val currentUserNotesFlow: Flow<List<Note>> = sharedPref.userIdFlow().flatMapLatest {
            userId -> //получаем из сеттингов текущий айди юзера
            notesDao.getAllNotesFlowByUserId(userId) //получаем заметки по айди юзера
        }


    fun addNewNote(note: Note) {
        launch {
            // notesDao.saveNote(note)
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
                text = note.text,
                date = note.date,
                userId = sharedPref.getUserId()
            ))
        }
    }
    fun logout(){
        sharedPref.putUserId(-1)
    }

}