package io.techmeskills.an02onl_plannerapp.screen.main

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow


class SharedPref(val context: Context) {
    private val pref: SharedPreferences = context.getSharedPreferences("PREFERENCE",
        Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = pref.edit()

    fun putUserId(userId: Long) {
        editor.putLong("userId", userId)
        editor.commit()
    }

    fun getUserId(): Long {
        return pref.getLong("userId", 0)
    }

    private fun getUserIdFlow(): Flow<Long> = flow {
        emit(pref.getLong("userId", 0))
    }

     fun userIdFlow(): Flow<Long> = getUserIdFlow()

    suspend fun userId(): Long = userIdFlow().first()

}


