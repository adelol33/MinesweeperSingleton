package com.example.minesweeper.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.minesweeper.R
import com.example.minesweeper.model.Difficulty

class DifficultySelectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_difficulty_select)
        setupDifficultyButtons()
    }

    private fun setupDifficultyButtons() {
        val easyButton = findViewById<Button>(R.id.easyButton)
        val mediumButton = findViewById<Button>(R.id.mediumButton)
        val hardButton = findViewById<Button>(R.id.hardButton)

        easyButton.setOnClickListener {
            startGame(Difficulty.EASY)
        }

        mediumButton.setOnClickListener {
            startGame(Difficulty.MEDIUM)
        }

        hardButton.setOnClickListener {
            startGame(Difficulty.HARD)
        }
    }

    private fun startGame(difficulty: Difficulty) {
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("DIFFICULTY", difficulty.name)
        startActivity(intent)
    }
}
