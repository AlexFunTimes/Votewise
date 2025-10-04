package com.votewise.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "subscriptions")
data class Subscription(
    @PrimaryKey val id: String,
    val userId: String,
    val productId: String,
    val subscriptionType: SubscriptionType,
    val status: SubscriptionStatus,
    val startDate: Date,
    val endDate: Date?,
    val autoRenew: Boolean = true,
    val price: Double,
    val currency: String = "USD",
    val purchaseToken: String?,
    val orderId: String?,
    val createdAt: Date = Date(),
    val lastUpdated: Date = Date()
)

enum class SubscriptionType {
    MONTHLY,
    YEARLY,
    LIFETIME
}

enum class SubscriptionStatus {
    ACTIVE,
    EXPIRED,
    CANCELLED,
    PENDING,
    PAUSED
}

@Entity(tableName = "in_app_purchases")
data class InAppPurchase(
    @PrimaryKey val id: String,
    val userId: String,
    val productId: String,
    val purchaseType: PurchaseType,
    val amount: Double,
    val currency: String = "USD",
    val purchaseToken: String,
    val orderId: String,
    val purchaseDate: Date,
    val status: PurchaseStatus,
    val consumed: Boolean = false
)

enum class PurchaseType {
    SUBSCRIPTION,
    ONE_TIME,
    CONSUMABLE
}

enum class PurchaseStatus {
    PENDING,
    COMPLETED,
    FAILED,
    REFUNDED,
    CANCELLED
}

data class PremiumFeatures(
    val unlimitedMatches: Boolean = false,
    val advancedReports: Boolean = false,
    val aiInsights: Boolean = false,
    val adFree: Boolean = false,
    val prioritySupport: Boolean = false,
    val exportData: Boolean = false,
    val customAlerts: Boolean = false
)


