package io.techmeskills.an02onl_plannerapp.screen.main

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.*
import androidx.room.Delete
import androidx.viewbinding.ViewBinding
import by.kirich1409.viewbindingdelegate.viewBinding
import io.techmeskills.an02onl_plannerapp.R
import io.techmeskills.an02onl_plannerapp.database.Note
import io.techmeskills.an02onl_plannerapp.database.NotesDao
import io.techmeskills.an02onl_plannerapp.databinding.FragmentMainBinding
import io.techmeskills.an02onl_plannerapp.support.CloudManager
import io.techmeskills.an02onl_plannerapp.support.CoroutineViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainViewModel(
    private val notesDao: NotesDao,
    private val noteDetailsViewModel: NoteDetailsViewModel,
    private val cloudManager: CloudManager,
    private val pref: SharedPref,
    private val context: Context,
) : CoroutineViewModel() {

    val liveData = noteDetailsViewModel.currentUserNotesFlow.flowOn(Dispatchers.IO).asLiveData()

    val progressLiveData = MutableLiveData<Boolean>()

    val internetConnectionLiveData = MutableLiveData<Boolean>()


    fun checkInternetConnection(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return if (networkInfo != null && networkInfo.isConnected) {
            internetConnectionLiveData.postValue(true)
            true
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG)
                .show()
            internetConnectionLiveData.postValue(false)
            false
        }
    }

    fun getItemById(position: Int): Note {
        val list: List<Note>? = liveData.value
        return list!![position]
    }


    fun removeItemByDao(note: Note) {
        launch {
            notesDao.deleteNote(note)
        }
    }

    fun exportNotes() = launch {
        val result = cloudManager.exportNotes()
        progressLiveData.postValue(result)
    }

    fun importNotes() = launch {
        val result = cloudManager.importNotes()
        if (result) {
            progressLiveData.postValue(result)
        } else {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(context, "Invalid import: notes already exist", Toast.LENGTH_SHORT).show()
                progressLiveData.postValue(result)
            }
        }
    }


    fun logout() {
        pref.putUserId(-1)
    }

}