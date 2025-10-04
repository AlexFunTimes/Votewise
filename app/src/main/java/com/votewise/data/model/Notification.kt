package com.votewise.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "notifications")
data class Notification(
    @PrimaryKey val id: String,
    val userId: String,
    val title: String,
    val message: String,
    val type: NotificationType,
    val category: NotificationCategory,
    val candidateId: String?,
    val electionId: String?,
    val url: String?,
    val isRead: Boolean = false,
    val isActionable: Boolean = false,
    val actionText: String?,
    val actionUrl: String?,
    val createdAt: Date = Date(),
    val scheduledFor: Date? = null,
    val expiresAt: Date? = null
)

enum class NotificationType {
    CANDIDATE_UPDATE,
    NEW_ELECTION,
    POLICY_CHANGE,
    DONOR_UPDATE,
    QUIZ_REMINDER,
    REGISTRATION_DEADLINE,
    VOTING_REMINDER,
    WEEKLY_DIGEST,
    SYSTEM_UPDATE
}

enum class NotificationCategory {
    URGENT,
    IMPORTANT,
    INFORMATIONAL,
    PROMOTIONAL
}

@Entity(tableName = "notification_preferences")
data class NotificationPreference(
    @PrimaryKey val id: String,
    val userId: String,
    val type: NotificationType,
    val enabled: Boolean = true,
    val frequency: NotificationFrequency = NotificationFrequency.IMMEDIATE,
    val quietHoursStart: String? = null, // HH:mm format
    val quietHoursEnd: String? = null
)

enum class NotificationFrequency {
    IMMEDIATE,
    DAILY,
    WEEKLY,
    NEVER
}

data class NotificationSettings(
    val pushEnabled: Boolean = true,
    val emailEnabled: Boolean = false,
    val smsEnabled: Boolean = false,
    val quietHours: Pair<String, String>? = null,
    val preferences: Map<NotificationType, NotificationPreference> = emptyMap()
)


