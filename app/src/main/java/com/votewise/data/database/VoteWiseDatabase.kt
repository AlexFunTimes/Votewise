package com.votewise.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.votewise.data.model.Candidate

@Database(entities = [Candidate::class], version = 1, exportSchema = false)
abstract class VoteWiseDatabase : RoomDatabase() {
    abstract fun candidateDao(): CandidateDao

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
