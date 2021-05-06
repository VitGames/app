package io.techmeskills.an02onl_plannerapp.alarmservice

import android.annotation.SuppressLint
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import io.techmeskills.an02onl_plannerapp.R


open class AlarmReceiver() : BroadcastReceiver() {

    companion object {
        const val ID = "CHANEL_ID"
        const val chanelName = "Заметки"
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, p1: Intent?) {
        //** как выставить правильно время уведомления? */
        setAlarm(context!!,null,"null","null")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setAlarm(context: Context, timeInMillis: Long?, title: String, body: String) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelNotification: NotificationCompat.Builder =
            NotificationCompat.Builder(context, ID)
                .setChannelId(ID)
                .setContentTitle(title)
                .setTicker("Мои заметки")
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_natification)
        val channel = NotificationChannel(ID, chanelName, NotificationManager.IMPORTANCE_HIGH)
        channel.enableVibration(true)
        channel.description = "Уведомление о заметках";
        channel.enableLights(true);
        channel.lightColor = Color.BLUE;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(channel)
            manager.notify(1, channelNotification.build())
        }
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("Notification", "note");
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis!!, pendingIntent)
    }

    fun cancelAlarm(context: Context) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val sender = PendingIntent.getBroadcast(context, 0, intent, 0)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(sender)
    }
}