package com.example.minesweeper.activities

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.minesweeper.R
import com.example.minesweeper.model.LeaderboardManager

class LeaderboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        // Récupérer le leaderboard sauvegardé
        val leaderboardManager = LeaderboardManager(this)
        val leaderboard = leaderboardManager.getLeaderboard()

        // Configurer le RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.leaderboardRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ScoreAdapter(leaderboard.getScores())

        // Configurer le bouton retour
        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener { finish() }
    }
}