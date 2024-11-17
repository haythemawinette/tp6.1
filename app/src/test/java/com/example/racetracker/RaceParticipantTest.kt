package com.example.racetracker

import com.example.racetracker.ui.RaceParticipant
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Test

class RaceParticipantTest {

    @Test
    fun raceParticipant_NormalProgress_Success() = runTest {
        val raceParticipant = RaceParticipant(
            name = "Test",
            maxProgress = 100,
            progressDelayMillis = 500L,
            progressIncrement = 10
        )

        launch { raceParticipant.run() }

        advanceTimeBy(1500L)
        runCurrent()
        assertEquals(30, raceParticipant.currentProgress)
    }

    @Test
    fun raceParticipant_ErrorDuringRun_StopsProgress() = runTest {
        val raceParticipant = RaceParticipant(
            name = "Test",
            maxProgress = 100,
            progressDelayMillis = 500L,
            progressIncrement = 10
        )
        raceParticipant.hasErrored = true

        launch { raceParticipant.run() }

        advanceTimeBy(500L)
        runCurrent()
        assertTrue(raceParticipant.hasErrored)
        assertEquals(0, raceParticipant.currentProgress)
    }

    @Test
    fun raceParticipant_LowMaxProgress_CorrectlyFinishes() = runTest {
        val raceParticipant = RaceParticipant(
            name = "Test",
            maxProgress = 1,
            progressDelayMillis = 500L,
            progressIncrement = 1
        )

        launch { raceParticipant.run() }

        advanceTimeBy(500L)
        runCurrent()
        assertEquals(1, raceParticipant.currentProgress)
    }

    @Test
    fun raceParticipant_ResetAfterError_ResetsState() = runTest {
        val raceParticipant = RaceParticipant(
            name = "Test",
            maxProgress = 100,
            progressDelayMillis = 500L,
            progressIncrement = 10
        )
        raceParticipant.hasErrored = true // Simulate an error

        raceParticipant.reset()
        assertEquals(0, raceParticipant.currentProgress)
        assertEquals(false, raceParticipant.hasErrored)
    }

    @Test
    fun raceParticipant_CompleteRace_UpdatesProgressCorrectly() = runTest {
        val raceParticipant = RaceParticipant(
            name = "Test",
            maxProgress = 50,
            progressDelayMillis = 500L,
            progressIncrement = 10
        )

        launch { raceParticipant.run() }

        advanceTimeBy(2500L)
        runCurrent()
        assertEquals(50, raceParticipant.currentProgress)
    }
}
