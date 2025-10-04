package com.votewise.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.votewise.data.database.dao.QuizDao
import com.votewise.data.database.dao.UserDao
import com.votewise.data.model.CandidateMatch
import com.votewise.data.model.QuizQuestion
import com.votewise.data.model.QuizResponse
import com.votewise.data.model.QuizSession
import com.votewise.data.model.User

@Database(entities = [User::class, QuizQuestion::class, QuizResponse::class, CandidateMatch::class, QuizSession::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun quizDao(): QuizDao
}
