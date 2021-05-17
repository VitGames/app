package io.techmeskills.an02onl_plannerapp.screen.main


import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import io.techmeskills.an02onl_plannerapp.database.User
import io.techmeskills.an02onl_plannerapp.database.UserDao
import io.techmeskills.an02onl_plannerapp.support.CoroutineViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class UsersViewModel(
    private val userDao: UserDao,
    private val sharedPref: SharedPref,
    context: Context,
) :
    CoroutineViewModel() {

    var isExists: Boolean = false

    fun getCurrentUserFlow(): Flow<User> = sharedPref.userNameFlow().map { User(it!!) }
    private fun addNewUser(user: User) {
        launch {
            if (!checkUserExists(user.name)) {
                userDao.newUser(user)
            }
        }
    }

    private fun putUserNameToPref(userName: String) {
        sharedPref.putUserName(userName)
    }

    private fun checkUserExists(userName: String): Boolean {
        isExists = userDao.getUsersCount(userName) > 0
        return isExists
    }

    fun login(userName: String) {
        launch {
            if (checkUserExists(userName)) {
                //если есть, добавляем в prefs
                putUserNameToPref(userName)
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(sharedPref.context,
                        "$userName , добро пожаловать",
                        Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                addNewUser(User(name = userName))
                putUserNameToPref(userName)
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(sharedPref.context,
                        "Новыи пользователь: $userName",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    @SuppressLint("HardwareIds")
    val phoneId: String =
        Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
}