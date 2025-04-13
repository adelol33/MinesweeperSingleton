package com.example.minesweeper.model

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.time.Duration

class LeaderboardManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("leaderboard_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun getLeaderboard(): LeaderBoard {
        val leaderboardJson = sharedPreferences.getString("leaderboard", null)

        if (leaderboardJson != null) {
            val type = object : TypeToken<ArrayList<Score>>() {}.type
            val scores = gson.fromJson<ArrayList<Score>>(leaderboardJson, type)
            return LeaderBoard(scores)
        }

        return LeaderBoard()
    }

    fun addScore(name: String, time: Duration, difficulty: Difficulty) {
        val leaderboard = getLeaderboard()
        leaderboard.add(Score.fromDuration(name, time, difficulty))
        saveLeaderboard(leaderboard)
    }

    private fun saveLeaderboard(leaderboard: LeaderBoard) {
        val scores = leaderboard.getScores()
        val leaderboardJson = gson.toJson(scores)
        sharedPreferences.edit().putString("leaderboard", leaderboardJson).apply()
    }
}