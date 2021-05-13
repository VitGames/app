package io.techmeskills.an02onl_plannerapp.screen.main

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map


class SharedPref(val context: Context) {
    private val pref: SharedPreferences = context.getSharedPreferences("PREFERENCE",
        Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = pref.edit()

    fun putUserId(userId: Long) {
        editor.putLong("userId", userId)
        editor.commit()
    }
    fun getUserName(): String? {
        return pref.getString("userName","default")
    }

    fun putUserName(userName: String) {
        editor.putString("userName", userName)
        editor.commit()
    }

    fun deleteUserName() {
        editor.putString("userName", "")
        editor.commit()
    }

    private fun getUserIdFlow(): Flow<Long> = flow {
        emit(pref.getLong("userId", 0))
    }

    private fun userIdFlow(): Flow<Long> = getUserIdFlow()


    suspend fun userId(): Long = userIdFlow().first()

    fun userNameFlow(): Flow<String?> = flow {
        emit(pref.getString("userName", "default"))
    }

    suspend fun userName(): String? = userNameFlow().first()
}



