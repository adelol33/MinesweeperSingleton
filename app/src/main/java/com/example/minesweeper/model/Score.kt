package com.example.minesweeper.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

data class Score(
    @SerializedName("name") private val name: String,
    @SerializedName("timeSeconds") private val timeSeconds: Long,
    @SerializedName("difficulty") internal val difficulty: Difficulty,
    @SerializedName("timestamp") val timestamp: Long = System.currentTimeMillis()
) : Serializable, Comparable<Score> {

    companion object {
        fun fromDuration(name: String, time: Duration, difficulty: Difficulty): Score {
            return Score(name, time.inWholeSeconds, difficulty)
        }
    }

    fun getName(): String = name

    fun getTime(): Duration = timeSeconds.seconds

    fun getDifficulty(): Difficulty = difficulty

    fun getFormattedTime(): String {
        val minutes = timeSeconds / 60
        val seconds = timeSeconds % 60
        return if (minutes > 0) {
            String.format("%d:%02d", minutes, seconds)
        } else {
            "${seconds}s"
        }
    }

    override fun toString(): String {
        return "$name - ${getFormattedTime()} - ${difficulty.label}"
    }

    override fun compareTo(other: Score): Int {
        // D'abord comparer par difficulté (du plus difficile au plus facile)
        val difficultyComparison = other.difficulty.compareTo(difficulty)
        if (difficultyComparison != 0) {
            return difficultyComparison
        }

        // Ensuite par temps (du plus rapide au plus lent)
        return timeSeconds.compareTo(other.timeSeconds)
    }
}
