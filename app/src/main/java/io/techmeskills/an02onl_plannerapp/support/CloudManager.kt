package io.techmeskills.an02onl_plannerapp.support

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import io.techmeskills.an02onl_plannerapp.cloud.ApiInterface
import io.techmeskills.an02onl_plannerapp.cloud.CloudNote
import io.techmeskills.an02onl_plannerapp.cloud.CloudUser
import io.techmeskills.an02onl_plannerapp.cloud.ExportNotesRequestBody
import io.techmeskills.an02onl_plannerapp.database.Note
import io.techmeskills.an02onl_plannerapp.screen.main.NoteDetailsViewModel
import io.techmeskills.an02onl_plannerapp.screen.main.UsersViewModel
import kotlinx.coroutines.flow.first
import java.lang.Exception
import kotlin.coroutines.coroutineContext

class CloudManager(
    private val apiInterface: ApiInterface,
    private val userRepository: UsersViewModel,
    private val notesRepository: NoteDetailsViewModel,
    private val context: Context,
) {
    suspend fun exportNotes(): Boolean {
            val currentList: List<Note> = notesRepository.getCurrentUserNotes()
            if (currentList.isEmpty()) {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(context,
                        "Invalid export: notes does not exist",
                        Toast.LENGTH_SHORT)
                        .show()
                }
                return false
            } else {
                val user = userRepository.getCurrentUserFlow().first()
                val notes = notesRepository.getCurrentUserNotes()
                val cloudUser = CloudUser(userName = user.name)
                val cloudNotes =
                    notes.map { CloudNote(id = it.id, title = it.text, date = it.date, alarmEnabled = it.alarmEnabled) }
                val exportRequestBody =
                    ExportNotesRequestBody(cloudUser, userRepository.phoneId, cloudNotes)
                val exportResult = apiInterface.exportNotes(exportRequestBody).isSuccessful
                if (exportResult) {
                    notesRepository.setAllNotesSyncWithCloud()
                }
                return exportResult
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(context, "Export success", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    suspend fun importNotes(): Boolean {
        val user = userRepository.getCurrentUserFlow().first()
        val response = apiInterface.importNotes(user.name, userRepository.phoneId)
        val cloudNotes = response.body() ?: emptyList()
        val notes = cloudNotes.map { cloudNote ->
            Note(
                text = cloudNote.title,
                date = cloudNote.date,
                userName = user.name,
                fromCloud = true,
                alarmEnabled = cloudNote.alarmEnabled
            )
        }
        val currentList: List<Note> = notesRepository.getCurrentUserNotes()
        val cloudList: List<Note> = notes
        val result = (cloudList + currentList).distinctBy { it.text }
        notesRepository.clearDataBase()
        notesRepository.saveNotes(result)
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, "Import success", Toast.LENGTH_SHORT)
                .show()
        }

        return response.isSuccessful
    }
}