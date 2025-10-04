package com.votewise.data.database.dao

import androidx.room.*
import com.votewise.data.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: String): User?

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM users WHERE twitterId = :twitterId")
    suspend fun getUserByTwitterId(twitterId: String): User?

    @Query("SELECT * FROM users WHERE zipCode = :zipCode")
    fun getUsersByZipCode(zipCode: String): Flow<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("UPDATE users SET lastLoginAt = :loginTime WHERE id = :userId")
    suspend fun updateLastLogin(userId: String, loginTime: java.util.Date)

    @Query("UPDATE users SET isPremium = :isPremium WHERE id = :userId")
    suspend fun updatePremiumStatus(userId: String, isPremium: Boolean)

    @Query("UPDATE users SET zipCode = :zipCode, state = :state, city = :city WHERE id = :userId")
    suspend fun updateUserLocation(userId: String, zipCode: String, state: String, city: String)
}
