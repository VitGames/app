package io.techmeskills.an02onl_plannerapp.screen.main

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.viewbinding.ViewBinding
import by.kirich1409.viewbindingdelegate.viewBinding
import io.techmeskills.an02onl_plannerapp.R
import io.techmeskills.an02onl_plannerapp.database.Note
import io.techmeskills.an02onl_plannerapp.database.NotesDao
import io.techmeskills.an02onl_plannerapp.databinding.FragmentMainBinding
import io.techmeskills.an02onl_plannerapp.support.CoroutineViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainViewModel(private val notesDao: NotesDao) : CoroutineViewModel() {

    val liveData = MutableLiveData(
        listOf(
            Note(0, "Пример заметки")
        )
    )

    fun addNewNote(textNote: String, date: String?) {
        launch {
            val note = Note(
                text = textNote,
                date = date)
            val list = liveData.value!!.toMutableList()
            list.add(0, note)
            liveData.postValue(list)
            invalidateList()
        }
    }

    fun getItemById(position: Int): Note {
        val list: List<Note>? = liveData.value
        return list!![position]
    }

    fun removeItemById(position: Int) {
        val list = liveData.value!!.toMutableList()
        list.removeAt(position)
        liveData.postValue(list)
    }

    fun removeItemByDao(note: Note) {
        launch {
            notesDao.deleteNote(note)
            invalidateList()
        }
    }

    fun editNote(note: Note, textNote: String, date: String?) {

    }

    fun invalidateList() {
        launch {
            val notes = notesDao.getAllNotes()
            liveData.postValue(notes)
        }
    }
}