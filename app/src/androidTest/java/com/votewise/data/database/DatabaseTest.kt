package com.votewise.data.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.votewise.data.model.Election
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    private lateinit var database: VoteWiseDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context, VoteWiseDatabase::class.java
        ).build()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetElection() = runBlocking {
        val election = Election(
            id = "1",
            name = "Test Election",
            type = "GENERAL",
            state = "CA",
            district = null,
            county = null,
            electionDate = "2024-11-05",
            registrationDeadline = "2024-10-15",
            earlyVotingStart = "2024-10-20",
            earlyVotingEnd = "2024-11-02",
            absenteeDeadline = "2024-11-01",
            isPrimary = false,
            isGeneral = true,
            isSpecial = false,
            status = "UPCOMING",
            description = "Test election",
            website = "https://test.com"
        )

        database.electionDao().insertElection(election)
        val retrieved = database.electionDao().getElectionById("1")
        assert(retrieved != null)
        assert(retrieved?.name == "Test Election")
    }
}
