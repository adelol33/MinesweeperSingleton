package com.example.minesweeper.model

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.time.Duration

class LeaderboardManager(private val context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        LEADERBOARD_PREFS, Context.MODE_PRIVATE
    )
    private val gson = Gson()

    fun addScore(playerName: String, duration: Duration, difficulty: Difficulty) {
        val score = Score.fromDuration(playerName, duration, difficulty)
        addScore(score)
    }

    fun addScore(score: Score) {
        val scores = getAllScores().toMutableList()
        scores.add(score)

        // Trier les scores
        scores.sort()

        // Limiter à 100 scores maximum
        val limitedScores = if (scores.size > MAX_SCORES) {
            scores.subList(0, MAX_SCORES)
        } else {
            scores
        }

        saveAllScores(limitedScores)
    }

    fun getAllScores(): List<Score> {
        val json = sharedPreferences.getString(KEY_SCORES, null) ?: return emptyList()
        val type = object : TypeToken<List<Score>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    fun getScoresByDifficulty(difficulty: Difficulty): List<Score> {
        return getAllScores().filter { it.getDifficulty() == difficulty }
    }

    fun clearAllScores() {
        sharedPreferences.edit().remove(KEY_SCORES).apply()
    }

    private fun saveAllScores(scores: List<Score>) {
        val json = gson.toJson(scores)
        sharedPreferences.edit().putString(KEY_SCORES, json).apply()
    }

    companion object {
        private const val LEADERBOARD_PREFS = "leaderboard_preferences"
        private const val KEY_SCORES = "scores"
        private const val MAX_SCORES = 100
    }
}