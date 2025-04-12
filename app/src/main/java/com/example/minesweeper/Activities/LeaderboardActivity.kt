package com.example.minesweeper.activities

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.minesweeper.R
import com.example.minesweeper.model.Difficulty
import com.example.minesweeper.model.LeaderBoard
import kotlin.time.Duration.Companion.seconds

class LeaderboardActivity : AppCompatActivity() {

    private lateinit var leaderboard: LeaderBoard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        // Initialiser le leaderboard avec les données fictives
        leaderboard = createFakeLeaderboard()

        // Configurer le RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.leaderboardRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ScoreAdapter(leaderboard.getScores())

        // Configurer le bouton retour
        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener { finish() }
    }

    private fun createFakeLeaderboard(): LeaderBoard {
        val leaderBoard = LeaderBoard()
        leaderBoard.add("Tiago", 217.seconds, Difficulty.HARD)
        leaderBoard.add("Romane", 286.seconds, Difficulty.EASY)
        leaderBoard.add("Victoria", 401.seconds, Difficulty.MEDIUM)
        leaderBoard.add("Romain", 613.seconds, Difficulty.MEDIUM)
        leaderBoard.add("Adel", 845.seconds, Difficulty.HARD)
        leaderBoard.add("Laurie", 1355.seconds, Difficulty.EASY)
        return leaderBoard
    }
}