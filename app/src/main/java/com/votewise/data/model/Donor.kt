package com.votewise.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "donors")
data class Donor(
    @PrimaryKey val id: String,
    val name: String,
    val amount: Double,
    val candidateId: String,
    val donorType: DonorType,
    val industry: String?,
    val state: String?,
    val city: String?,
    val zipCode: String?,
    val employer: String?,
    val occupation: String?,
    val isPAC: Boolean = false,
    val pacName: String?,
    val fecId: String?,
    val donationDate: Date,
    val electionCycle: String,
    val isIndividual: Boolean = true
)

enum class DonorType {
    INDIVIDUAL,
    PAC,
    PARTY_COMMITTEE,
    CANDIDATE_COMMITTEE,
    SUPER_PAC,
    OTHER
}

@Entity(tableName = "donor_aggregates")
data class DonorAggregate(
    @PrimaryKey val id: String,
    val candidateId: String,
    val donorType: DonorType,
    val totalAmount: Double,
    val donationCount: Int,
    val electionCycle: String,
    val lastUpdated: Date = Date()
)

@Entity(tableName = "industry_donations")
data class IndustryDonation(
    @PrimaryKey val id: String,
    val candidateId: String,
    val industry: String,
    val totalAmount: Double,
    val donationCount: Int,
    val electionCycle: String,
    val lastUpdated: Date = Date()
)

data class DonorVisualization(
    val candidateId: String,
    val totalRaised: Double,
    val individualDonations: Double,
    val pacDonations: Double,
    val partyDonations: Double,
    val topIndustries: List<IndustryDonation>,
    val topDonors: List<Donor>,
    val averageDonation: Double,
    val smallDonorPercentage: Double // donations under $200
)
