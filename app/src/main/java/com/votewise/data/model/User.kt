package com.votewise.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String,
    val email: String,
    val displayName: String?,
    val profileImageUrl: String?,
    val twitterId: String?,
    val twitterUsername: String?,
    val createdAt: Date = Date(),
    val lastLoginAt: Date = Date(),
    val zipCode: String?,
    val state: String?,
    val city: String?,
    val isPremium: Boolean = false,
    val isProfilePublic: Boolean = true,
    val isLocationShared: Boolean = true,
    val arePushNotificationsEnabled: Boolean = true,
    val areEmailNotificationsEnabled: Boolean = true
)
