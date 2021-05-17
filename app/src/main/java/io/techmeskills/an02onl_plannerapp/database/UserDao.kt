package io.techmeskills.an02onl_plannerapp.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
abstract class UserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract fun newUser(user: User): Long

    @Query("SELECT COUNT(*) FROM users WHERE name == :userName")
    abstract fun getUsersCount(userName: String): Int

//    @Query("SELECT id FROM users WHERE name == :userName")
//    abstract fun getUserId(userName: String): Long

    @Query("SELECT COUNT(*) FROM users WHERE name == :userName")
    abstract fun getUsersCountFlow(userName: String): Flow<Int>

    @Query("SELECT name FROM users")
    abstract fun getAllUserNames(): Flow<List<String>>

//    @Query("SELECT * FROM users WHERE id == :userId")
//    abstract fun getById(userId: Long): Flow<User>

    @Transaction
    @Query("SELECT * from users WHERE name == :userName LIMIT 1")
    abstract fun getUserContent(userName: String): UserContent?

    @Transaction
    @Query("SELECT * from users WHERE name == :userName LIMIT 1")
    abstract fun getUserContentFlow(userName: String): Flow<UserContent?>
    @Delete
    abstract fun deleteUser(user: User)
}