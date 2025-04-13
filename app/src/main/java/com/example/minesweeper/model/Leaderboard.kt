package com.example.minesweeper.model

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.time.Duration

class Leaderboard(private val context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "minesweeper_leaderboard", Context.MODE_PRIVATE
    )

    private val gson = Gson()

    fun addScore(playerName: String, timePlayed: Duration, difficulty: Difficulty) {
        val score = Score(playerName, timePlayed, difficulty)
        val scores = getScores(difficulty).toMutableList()
        scores.add(score)
        scores.sort() // Trie les scores (le plus rapide en premier)

        // Limiter à 10 scores maximum par difficulté
        val limitedScores = scores.take(10)

        // Sauvegarder les scores
        val json = gson.toJson(limitedScores)
        sharedPreferences.edit().putString(getKeyForDifficulty(difficulty), json).apply()
    }

    fun getScores(difficulty: Difficulty): List<Score> {
        val json = sharedPreferences.getString(getKeyForDifficulty(difficulty), null) ?: return emptyList()
        val type = object : TypeToken<List<Score>>() {}.type
        return try {
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    fun getAllScores(): Map<Difficulty, List<Score>> {
        return Difficulty.values().associateWith { getScores(it) }
    }

    fun clearScores(difficulty: Difficulty? = null) {
        val editor = sharedPreferences.edit()

        if (difficulty != null) {
            editor.remove(getKeyForDifficulty(difficulty))
        } else {
            Difficulty.values().forEach {
                editor.remove(getKeyForDifficulty(it))
            }
        }

        editor.apply()
    }

    private fun getKeyForDifficulty(difficulty: Difficulty): String {
        return "scores_${difficulty.name.lowercase()}"
    }
}