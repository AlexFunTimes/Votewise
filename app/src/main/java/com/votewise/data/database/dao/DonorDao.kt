package com.votewise.data.database.dao

import androidx.room.*
import com.votewise.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DonorDao {
    // Donors
    @Query("SELECT * FROM donors WHERE candidateId = :candidateId ORDER BY amount DESC")
    fun getDonorsForCandidate(candidateId: String): Flow<List<Donor>>

    @Query("SELECT * FROM donors WHERE candidateId = :candidateId AND donorType = :donorType ORDER BY amount DESC")
    fun getDonorsByType(candidateId: String, donorType: DonorType): Flow<List<Donor>>

    @Query("SELECT * FROM donors WHERE candidateId = :candidateId AND industry = :industry ORDER BY amount DESC")
    fun getDonorsByIndustry(candidateId: String, industry: String): Flow<List<Donor>>

    @Query("SELECT * FROM donors WHERE candidateId = :candidateId AND isIndividual = 1 ORDER BY amount DESC")
    fun getIndividualDonors(candidateId: String): Flow<List<Donor>>

    @Query("SELECT * FROM donors WHERE candidateId = :candidateId AND isPAC = 1 ORDER BY amount DESC")
    fun getPACDonors(candidateId: String): Flow<List<Donor>>

    @Query("SELECT * FROM donors WHERE candidateId = :candidateId AND amount >= :minAmount ORDER BY amount DESC")
    fun getDonorsAboveAmount(candidateId: String, minAmount: Double): Flow<List<Donor>>

    @Query("SELECT * FROM donors WHERE candidateId = :candidateId AND electionCycle = :cycle ORDER BY amount DESC")
    fun getDonorsForElectionCycle(candidateId: String, cycle: String): Flow<List<Donor>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDonor(donor: Donor)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDonors(donors: List<Donor>)

    @Update
    suspend fun updateDonor(donor: Donor)

    @Delete
    suspend fun deleteDonor(donor: Donor)

    // Donor Aggregates
    @Query("SELECT * FROM donor_aggregates WHERE candidateId = :candidateId")
    fun getDonorAggregates(candidateId: String): Flow<List<DonorAggregate>>

    @Query("SELECT * FROM donor_aggregates WHERE candidateId = :candidateId AND donorType = :donorType")
    suspend fun getAggregateByType(candidateId: String, donorType: DonorType): DonorAggregate?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDonorAggregate(aggregate: DonorAggregate)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDonorAggregates(aggregates: List<DonorAggregate>)

    // Industry Donations
    @Query("SELECT * FROM industry_donations WHERE candidateId = :candidateId ORDER BY totalAmount DESC")
    fun getIndustryDonations(candidateId: String): Flow<List<IndustryDonation>>

    @Query("SELECT * FROM industry_donations WHERE candidateId = :candidateId AND electionCycle = :cycle ORDER BY totalAmount DESC")
    fun getIndustryDonationsForCycle(candidateId: String, cycle: String): Flow<List<IndustryDonation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIndustryDonation(donation: IndustryDonation)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIndustryDonations(donations: List<IndustryDonation>)

    // Complex queries for visualizations
    @Query("""
        SELECT 
            SUM(amount) as totalRaised,
            COUNT(*) as donationCount,
            AVG(amount) as averageDonation,
            SUM(CASE WHEN amount < 200 THEN amount ELSE 0 END) as smallDonorTotal,
            COUNT(CASE WHEN amount < 200 THEN 1 END) as smallDonorCount
        FROM donors 
        WHERE candidateId = :candidateId AND electionCycle = :cycle
    """)
    suspend fun getDonationSummary(candidateId: String, cycle: String): DonationSummary?

    @Query("""
        SELECT 
            donorType,
            SUM(amount) as totalAmount,
            COUNT(*) as donationCount
        FROM donors 
        WHERE candidateId = :candidateId AND electionCycle = :cycle
        GROUP BY donorType
        ORDER BY totalAmount DESC
    """)
    suspend fun getDonationsByType(candidateId: String, cycle: String): List<DonationTypeSummary>

    @Query("""
        SELECT 
            industry,
            SUM(amount) as totalAmount,
            COUNT(*) as donationCount
        FROM donors 
        WHERE candidateId = :candidateId AND electionCycle = :cycle AND industry IS NOT NULL
        GROUP BY industry
        ORDER BY totalAmount DESC
        LIMIT :limit
    """)
    suspend fun getTopIndustries(candidateId: String, cycle: String, limit: Int = 10): List<IndustrySummary>

    @Query("DELETE FROM donors WHERE donationDate < :cutoffDate")
    suspend fun deleteOldDonations(cutoffDate: java.util.Date)
}

data class DonationSummary(
    val totalRaised: Double,
    val donationCount: Int,
    val averageDonation: Double,
    val smallDonorTotal: Double,
    val smallDonorCount: Int
)

data class DonationTypeSummary(
    val donorType: DonorType,
    val totalAmount: Double,
    val donationCount: Int
)

data class IndustrySummary(
    val industry: String,
    val totalAmount: Double,
    val donationCount: Int
)


