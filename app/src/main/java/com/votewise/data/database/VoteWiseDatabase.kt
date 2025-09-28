package com.votewise.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.votewise.data.model.*
import com.votewise.data.database.dao.*
import com.votewise.data.database.converters.DateConverters
import com.votewise.data.database.converters.MapConverters

@Database(
    entities = [
        User::class,
        Candidate::class,
        CandidatePosition::class,
        VotingRecord::class,
        CandidateNews::class,
        Donor::class,
        DonorAggregate::class,
        IndustryDonation::class,
        QuizQuestion::class,
        QuizResponse::class,
        QuizSession::class,
        CandidateMatch::class,
        Election::class,
        ElectionCandidate::class,
        VoterRegistration::class,
        Notification::class,
        NotificationPreference::class,
        Subscription::class,
        InAppPurchase::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverters::class, MapConverters::class)
abstract class VoteWiseDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun candidateDao(): CandidateDao
    abstract fun quizDao(): QuizDao
    abstract fun donorDao(): DonorDao
    abstract fun electionDao(): ElectionDao
    abstract fun notificationDao(): NotificationDao
    abstract fun subscriptionDao(): SubscriptionDao

    companion object {
        @Volatile
        private var Instance: VoteWiseDatabase? = null

        fun getDatabase(context: Context): VoteWiseDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, VoteWiseDatabase::class.java, "votewise_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
