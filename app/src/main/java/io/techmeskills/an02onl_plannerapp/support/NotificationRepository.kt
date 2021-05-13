package io.techmeskills.an02onl_plannerapp.support

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import io.techmeskills.an02onl_plannerapp.alarmservice.AlarmReceiver
import io.techmeskills.an02onl_plannerapp.database.Note
import java.text.SimpleDateFormat
import java.util.*

class NotificationRepository(
    private val context: Context,
    private val alarmManager: AlarmManager,
) {
    private val dateFormatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    fun setNotification(note: Note) {
        val alarmTimeAtUTC = dateFormatter.parse(note.date!!)

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            alarmTimeAtUTC!!.time,
            makeIntent(note)
        )
    }

    fun unsetNotification(note: Note) {
        alarmManager.cancel(makeIntent(note))
    }

    private fun makeIntent(note: Note): PendingIntent {
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.action = AlarmReceiver.ACTION
        intent.putExtra(AlarmReceiver.NOTIFICATION_KEY_NOTE_ID, note.id)
        intent.putExtra(AlarmReceiver.NOTIFICATION_KEY_NOTE_OWNER, note.userName)
        intent.putExtra(AlarmReceiver.NOTIFICATION_KEY_NOTE_TEXT, note.text)
        return PendingIntent.getBroadcast(context, 0, intent, 0)
    }
}