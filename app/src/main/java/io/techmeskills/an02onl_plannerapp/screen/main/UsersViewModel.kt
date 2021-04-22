package io.techmeskills.an02onl_plannerapp.screen.main


import android.os.Handler
import android.os.Looper
import android.widget.Toast
import io.techmeskills.an02onl_plannerapp.database.User
import io.techmeskills.an02onl_plannerapp.database.UserDao
import io.techmeskills.an02onl_plannerapp.support.CoroutineViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class UsersViewModel(private val userDao: UserDao, private val sharedPref: SharedPref) :
    CoroutineViewModel() {

    var isExists: Boolean = false

    private fun addNewUser(user: User) {
        launch {
            if (!checkUserExists(user.name)) {
                userDao.newUser(user)
            }
        }
    }

    private suspend fun getUserIdDao(userName: String): Long {
        return withContext(Dispatchers.IO) {
            userDao.getUserId(userName)
        }
    }

    private fun putUserIdToPref(userName: String) {
        launch {
            val userId: Long = getUserIdDao(userName)
            sharedPref.putUserId(userId)
        }
    }

    private fun checkUserExists(userName: String): Boolean {
        isExists = userDao.getUsersCount(userName) > 0
        return isExists
    }

    fun login(userName: String) {
        launch {
            if (checkUserExists(userName)) {
                //если есть, добавляем в prefs
                putUserIdToPref(userName)
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(sharedPref.context, "Ник $userName есть в системе", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                addNewUser(User(name = userName))
                putUserIdToPref(userName)
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(sharedPref.context,
                        "Новыи пользователь: $userName",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}