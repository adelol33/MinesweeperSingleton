package com.example.minesweeper.model

import kotlin.time.Duration

class Leaderboard(val scores: MutableList<Score>, val difficulty: Difficulty) {

    fun addScore(playerName: String, timePlayed: Duration, difficulty: Difficulty) {
        val score = Score(playerName, timePlayed, difficulty)
        scores.add(score)
        scores.sort()
        val limitedScores = scores.take(10)
    }

}