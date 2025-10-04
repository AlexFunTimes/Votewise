package com.votewise.data.database.dao

import androidx.room.*
import com.votewise.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SubscriptionDao {
    // Subscriptions
    @Query("SELECT * FROM subscriptions WHERE userId = :userId")
    fun getUserSubscriptions(userId: String): Flow<List<Subscription>>

    @Query("SELECT * FROM subscriptions WHERE userId = :userId AND status = :status")
    fun getActiveSubscriptions(userId: String, status: SubscriptionStatus): Flow<List<Subscription>>

    @Query("SELECT * FROM subscriptions WHERE userId = :userId AND status = 'ACTIVE' ORDER BY startDate DESC LIMIT 1")
    suspend fun getCurrentSubscription(userId: String): Subscription?

    @Query("SELECT * FROM subscriptions WHERE id = :subscriptionId")
    suspend fun getSubscriptionById(subscriptionId: String): Subscription?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubscription(subscription: Subscription)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubscriptions(subscriptions: List<Subscription>)

    @Update
    suspend fun updateSubscription(subscription: Subscription)

    @Delete
    suspend fun deleteSubscription(subscription: Subscription)

    @Query("UPDATE subscriptions SET status = :status WHERE id = :subscriptionId")
    suspend fun updateSubscriptionStatus(subscriptionId: String, status: SubscriptionStatus)

    @Query("UPDATE subscriptions SET autoRenew = :autoRenew WHERE id = :subscriptionId")
    suspend fun updateAutoRenew(subscriptionId: String, autoRenew: Boolean)

    // In-App Purchases
    @Query("SELECT * FROM in_app_purchases WHERE userId = :userId")
    fun getUserPurchases(userId: String): Flow<List<InAppPurchase>>

    @Query("SELECT * FROM in_app_purchases WHERE userId = :userId AND purchaseType = :type")
    fun getUserPurchasesByType(userId: String, type: PurchaseType): Flow<List<InAppPurchase>>

    @Query("SELECT * FROM in_app_purchases WHERE userId = :userId AND status = :status")
    fun getPurchasesByStatus(userId: String, status: PurchaseStatus): Flow<List<InAppPurchase>>

    @Query("SELECT * FROM in_app_purchases WHERE purchaseToken = :token")
    suspend fun getPurchaseByToken(token: String): InAppPurchase?

    @Query("SELECT * FROM in_app_purchases WHERE orderId = :orderId")
    suspend fun getPurchaseByOrderId(orderId: String): InAppPurchase?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPurchase(purchase: InAppPurchase)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPurchases(purchases: List<InAppPurchase>)

    @Update
    suspend fun updatePurchase(purchase: InAppPurchase)

    @Delete
    suspend fun deletePurchase(purchase: InAppPurchase)

    @Query("UPDATE in_app_purchases SET status = :status WHERE id = :purchaseId")
    suspend fun updatePurchaseStatus(purchaseId: String, status: PurchaseStatus)

    @Query("UPDATE in_app_purchases SET consumed = :consumed WHERE id = :purchaseId")
    suspend fun updatePurchaseConsumed(purchaseId: String, consumed: Boolean)

    // Complex queries
    @Query("""
        SELECT 
            COUNT(*) as totalPurchases,
            SUM(amount) as totalSpent,
            AVG(amount) as averagePurchase
        FROM in_app_purchases 
        WHERE userId = :userId AND status = 'COMPLETED'
    """)
    suspend fun getPurchaseSummary(userId: String): PurchaseSummary?

    @Query("""
        SELECT 
            productId,
            COUNT(*) as purchaseCount,
            SUM(amount) as totalAmount
        FROM in_app_purchases 
        WHERE userId = :userId AND status = 'COMPLETED'
        GROUP BY productId
        ORDER BY totalAmount DESC
    """)
    suspend fun getTopProducts(userId: String): List<ProductSummary>
}

data class PurchaseSummary(
    val totalPurchases: Int,
    val totalSpent: Double,
    val averagePurchase: Double
)

data class ProductSummary(
    val productId: String,
    val purchaseCount: Int,
    val totalAmount: Double
)


