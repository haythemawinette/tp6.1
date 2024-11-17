package com.example.racetracker.ui


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay


/**
 * This class represents a state holder for race participant.
 */
class RaceParticipant(
    val name: String,
    val maxProgress: Int = 100,
    val progressDelayMillis: Long = 500L,
    private val progressIncrement: Int = 1,
    private val initialProgress: Int = 0
) {
    init {
        require(maxProgress > 0) { "maxProgress=$maxProgress; must be > 0" }
        require(progressIncrement > 0) { "progressIncrement=$progressIncrement; must be > 0" }
    }

    var currentProgress by mutableStateOf(initialProgress)
        private set

    var hasErrored by mutableStateOf(false)

    val progressFactor: Float
        get() = currentProgress.toFloat() / maxProgress

    suspend fun run() {
        try {
            while (currentProgress < maxProgress && !hasErrored) {
                delay(progressDelayMillis)
                currentProgress += progressIncrement
                if (currentProgress > maxProgress) {
                    currentProgress = maxProgress
                }
            }
        } catch (e: Exception) {
            hasErrored = true
        }
    }

    fun reset() {
        currentProgress = initialProgress
        hasErrored = false
    }
}
