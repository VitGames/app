package io.techmeskills.an02onl_plannerapp.alarmservice

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import io.techmeskills.an02onl_plannerapp.R
import io.techmeskills.an02onl_plannerapp.screen.main.NoteDetailsViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


@KoinApiExtension
class NotificationActionService : Service(), KoinComponent {

    private val noteDetailsViewModel: NoteDetailsViewModel by inject()

    private var noteId: Long = -1

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            noteId =
                it.getLongExtra(AlarmReceiver.NOTIFICATION_KEY_NOTE_ID, -1)
            when (it.action) {
                AlarmReceiver.ACTION_DELETE -> {
                    GlobalScope.launch {
                        noteDetailsViewModel.deleteNoteById(noteId)
                    }
                }
                AlarmReceiver.ACTION_EDIT_NOTE -> {
                    GlobalScope.launch {
                        noteId = it.getLongExtra(AlarmReceiver.NOTIFICATION_KEY_NOTE_ID, -1)
                        var replyText: CharSequence? = null
                        val results: Bundle = RemoteInput.getResultsFromIntent(intent)
                        replyText = results.getCharSequence(AlarmReceiver.NOTIFICATION_KEY_NOTE_TEXT_EDIT)
                        noteDetailsViewModel.updateTextNote(noteId, replyText.toString())

                        val repliedNotification = NotificationCompat.Builder(
                            baseContext, AlarmReceiver.NOTIFICATION_CHANNEL)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentText("Заметка изменена!")
                            .build()
                        val mNotificationManager =
                            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        mNotificationManager.notify(0, repliedNotification)
                    }
                }
                else -> Unit
            }
            stopSelf()
        }
        return START_NOT_STICKY
    }
}