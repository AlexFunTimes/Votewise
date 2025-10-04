package com.votewise.data.database.dao

import androidx.room.*
import com.votewise.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications WHERE userId = :userId ORDER BY createdAt DESC")
    fun getUserNotifications(userId: String): Flow<List<Notification>>

    @Query("SELECT * FROM notifications WHERE userId = :userId AND isRead = 0 ORDER BY createdAt DESC")
    fun getUnreadNotifications(userId: String): Flow<List<Notification>>

    @Query("SELECT * FROM notifications WHERE userId = :userId AND type = :type ORDER BY createdAt DESC")
    fun getNotificationsByType(userId: String, type: NotificationType): Flow<List<Notification>>

    @Query("SELECT * FROM notifications WHERE id = :notificationId")
    suspend fun getNotificationById(notificationId: String): Notification?

    @Query("SELECT COUNT(*) FROM notifications WHERE userId = :userId AND isRead = 0")
    suspend fun getUnreadCount(userId: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: Notification)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(notifications: List<Notification>)

    @Update
    suspend fun updateNotification(notification: Notification)

    @Delete
    suspend fun deleteNotification(notification: Notification)

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :notificationId")
    suspend fun markAsRead(notificationId: String)

    @Query("UPDATE notifications SET isRead = 1 WHERE userId = :userId")
    suspend fun markAllAsRead(userId: String)

    @Query("DELETE FROM notifications WHERE userId = :userId AND createdAt < :cutoffDate")
    suspend fun deleteOldNotifications(userId: String, cutoffDate: java.util.Date)

    // Notification Preferences
    @Query("SELECT * FROM notification_preferences WHERE userId = :userId")
    fun getUserNotificationPreferences(userId: String): Flow<List<NotificationPreference>>

    @Query("SELECT * FROM notification_preferences WHERE userId = :userId AND type = :type")
    suspend fun getPreferenceForType(userId: String, type: NotificationType): NotificationPreference?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotificationPreference(preference: NotificationPreference)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotificationPreferences(preferences: List<NotificationPreference>)

    @Update
    suspend fun updateNotificationPreference(preference: NotificationPreference)

    @Delete
    suspend fun deleteNotificationPreference(preference: NotificationPreference)
}


