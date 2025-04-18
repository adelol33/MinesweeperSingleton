package com.example.minesweeper.model

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LeaderboardRepository(private val context: Context):IRepository<Difficulty,Leaderboard> {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "minesweeper_leaderboard", Context.MODE_PRIVATE
    )

    private val gson = Gson()

    override fun read(key: Difficulty): Leaderboard {
        val json = sharedPreferences.getString(getKeyForDifficulty(key), null)
            ?: return Leaderboard(mutableListOf(), key)
        return try {
            val type = object : TypeToken<List<Score>>() {}.type
            val scores:List<Score> = gson.fromJson(json, type) ?: emptyList()
            Leaderboard(scores.toMutableList(), key)
        } catch (e: Exception) {
            e.printStackTrace()
            Leaderboard(mutableListOf(), key)
        }
    }

    override fun save(entity: Leaderboard) {
        val json = gson.toJson(entity.scores)
        sharedPreferences.edit().putString(getKeyForDifficulty(entity.difficulty), json)
            .apply()
    }

    private fun getKeyForDifficulty(difficulty: Difficulty): String {
        return "scores_${difficulty.name.lowercase()}"
    }
}