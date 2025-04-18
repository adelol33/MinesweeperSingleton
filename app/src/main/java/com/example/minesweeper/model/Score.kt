package com.example.minesweeper.model

import kotlin.time.Duration

data class Score(
    val playerName: String,
    val timePlayed: Duration,
    val difficulty: Difficulty,
    val timestamp: Long = System.currentTimeMillis()
) : Comparable<Score> {
    override fun compareTo(other: Score): Int {

        return timePlayed.compareTo(other.timePlayed)
    }
}