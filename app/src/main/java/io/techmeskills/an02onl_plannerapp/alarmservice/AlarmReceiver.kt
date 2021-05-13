package io.techmeskills.an02onl_plannerapp.alarmservice

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import io.techmeskills.an02onl_plannerapp.R
import org.koin.core.component.KoinApiExtension


class AlarmReceiver() : BroadcastReceiver() {

    companion object {
        const val ACTION = "PLANNER_APP_NOTIFICATION"
        const val NOTIFICATION_CHANNEL = "PLANNER_APP_NOTIFICATION_CHANNEL"
        const val ACTION_DELETE = "PLANNER_APP_NOTIFICATION_DELETE"
        const val ACTION_EDIT_NOTE = "PLANNER_APP_NOTIFICATION_EDIT_NOTE"
        const val NOTIFICATION_KEY_NOTE_ID = "PLANNER_APP_NOTIFICATION_KEY_NOTE_ID"
        const val NOTIFICATION_KEY_NOTE_TEXT = "PLANNER_APP_NOTIFICATION_KEY_NOTE_TEXT"
        const val NOTIFICATION_KEY_NOTE_TEXT_EDIT = "PLANNER_APP_NOTIFICATION_KEY_NOTE_TEXT_EDIT"
        const val NOTIFICATION_KEY_NOTE_OWNER = "PLANNER_APP_NOTIFICATION_KEY_NOTE_OWNER"
    }

    @KoinApiExtension
    @SuppressLint("ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, arg1: Intent?) {
        showNotification(context!!, arg1!!)
    }

    @KoinApiExtension
    @RequiresApi(Build.VERSION_CODES.O)
    fun showNotification(context: Context, intent: Intent) {
        val contentIntent = PendingIntent.getActivity(
            context, 0,
            Intent(context, AlarmReceiver::class.java), 0
        )

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(notificationManager)
        }

        val noteId = intent.getLongExtra(NOTIFICATION_KEY_NOTE_ID, -1)
        val noteText = intent.getStringExtra(NOTIFICATION_KEY_NOTE_TEXT)
        val noteOwner = intent.getStringExtra(NOTIFICATION_KEY_NOTE_OWNER)

        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL)
            .setSmallIcon(R.drawable.ic_natification)
            .setColor(Color.BLUE)
            .setContentTitle("Привет, $noteOwner")
            .setContentText("$noteText")
            .setContentIntent(contentIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(editNote(context, noteId))
            .addAction(makeDeleteAction(context, noteId))
            .setDefaults(Notification.DEFAULT_SOUND)
            .setAutoCancel(true)
        notificationManager.notify(0, builder.build())
    }

    @KoinApiExtension
    private fun editNote(context: Context, noteId: Long): NotificationCompat.Action? {
        val editIntent =
            Intent(context.applicationContext, NotificationActionService::class.java)
        editIntent.action = ACTION_EDIT_NOTE
        editIntent.putExtra(NOTIFICATION_KEY_NOTE_ID, noteId)
        val editPendingIntent = PendingIntent.getService(
            context.applicationContext,
            1112,
            editIntent,
            PendingIntent.FLAG_UPDATE_CURRENT)
        val remoteInput: androidx.core.app.RemoteInput =
            androidx.core.app.RemoteInput.Builder(NOTIFICATION_KEY_NOTE_TEXT_EDIT)
                .setLabel("Изменить заметку")
                .build()
        return NotificationCompat.Action.Builder(
            R.drawable.ic_edit,
            "Изменить",
            editPendingIntent).addRemoteInput(remoteInput).build()
    }

    @KoinApiExtension
    private fun makeDeleteAction(context: Context, noteId: Long): NotificationCompat.Action? {
        val deleteIntent =
            Intent(context.applicationContext, NotificationActionService::class.java)
        deleteIntent.action = ACTION_DELETE
        deleteIntent.putExtra(NOTIFICATION_KEY_NOTE_ID, noteId)
        val deletePendingIntent = PendingIntent.getService(
            context.applicationContext,
            1111,
            deleteIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        return NotificationCompat.Action.Builder(
            R.drawable.ic_delete,
            "Удалить",
            deletePendingIntent
        ).build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(notificationManager: NotificationManager) {
        if (notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL) == null) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL,
                NOTIFICATION_CHANNEL,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
    }
}