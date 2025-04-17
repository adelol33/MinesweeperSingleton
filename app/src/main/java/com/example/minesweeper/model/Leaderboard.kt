package com.example.minesweeper.model

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.time.Duration

interface IRepository<TKey,TEntity> {
    fun read(key:TKey):TEntity
    fun save(entity:TEntity) {
        // optional body
    }
}

class LeaderboardRepository(private val context: Context):IRepository<Difficulty,Leaderboard> {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "minesweeper_leaderboard", Context.MODE_PRIVATE
    )

    private val gson = Gson()

    //Reading the leader board
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

    //Saving the leaderboard
    override fun save(entity: Leaderboard) {
        val json = gson.toJson(entity.scores)
        sharedPreferences.edit().putString(getKeyForDifficulty(entity.difficulty), json)
            .apply()
    }

    private fun getKeyForDifficulty(difficulty: Difficulty): String {
        return "scores_${difficulty.name.lowercase()}"
    }
}

class Leaderboard(val scores: MutableList<Score>, val difficulty: Difficulty) {

    fun addScore(playerName: String, timePlayed: Duration, difficulty: Difficulty) {
        val score = Score(playerName, timePlayed, difficulty)
        scores.add(score)
        scores.sort() // Trie les scores (le plus rapide en premier)

        // Limiter à 10 scores maximum par difficulté
        val limitedScores = scores.take(10)
    }

}