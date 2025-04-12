package com.example.minesweeper.activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.minesweeper.R
import com.example.minesweeper.game.Game
import com.example.minesweeper.interfaces.GameObserver
import com.example.minesweeper.model.Difficulty
import com.example.minesweeper.model.GameResult
import java.util.*
import kotlin.time.Duration.Companion.seconds

class GameActivity : AppCompatActivity(), GameObserver {
    private lateinit var game: Game
    private lateinit var gridView: GridView
    private lateinit var cellAdapter: CellAdapter
    private lateinit var timerTextView: TextView
    private lateinit var flagToggle: ToggleButton
    private var isGameOver = false
    private val timerHandler = Handler(Looper.getMainLooper())
    private val timerRunnable = object : Runnable {
        override fun run() {
            if (!isGameOver) {
                game.updateTime()
                updateTimerDisplay()
                timerHandler.postDelayed(this, 1000)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // Get difficulty from intent
        val difficultyName = intent.getStringExtra("DIFFICULTY") ?: Difficulty.EASY.name
        val difficulty = Difficulty.valueOf(difficultyName)

        // Initialize game
        game = Game(difficulty)
        game.addObserver(this)

        // Initialize views
        gridView = findViewById(R.id.minesweeperGrid)
        timerTextView = findViewById(R.id.timerTextView)
        flagToggle = findViewById(R.id.flagToggle)

        // Setup cell adapter
        cellAdapter = CellAdapter(this, game.getBoard())
        gridView.adapter = cellAdapter

        // Setup click listeners
        setupGridClickListener()

        // Start game timer
        game.startTimer()
        timerHandler.post(timerRunnable)
    }

    private fun setupGridClickListener() {
        gridView.setOnItemClickListener { _, _, position, _ ->
            if (isGameOver) return@setOnItemClickListener

            val x = position % 10
            val y = position / 10

            if (flagToggle.isChecked) {
                game.flagCell(x, y)
            } else {
                val result = game.playMove(x, y)
                if (result != null) {
                    handleGameResult(result)
                }
            }

            cellAdapter.notifyDataSetChanged()
        }
    }

    private fun handleGameResult(result: GameResult) {
        isGameOver = true
        timerHandler.removeCallbacks(timerRunnable)

        val message = if (result.isVictory) {
            "Félicitations! Vous avez gagné en ${result.timePlayed.inWholeSeconds} secondes."
        } else {
            "Vous avez perdu. Temps joué: ${result.timePlayed.inWholeSeconds} secondes."
        }

        // Montrer la solution et le résultat après un court délai
        Handler(Looper.getMainLooper()).postDelayed({
            showGameResultDialog(message, result.isVictory)
        }, 500)
    }

    private fun showGameResultDialog(message: String, isVictory: Boolean) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(if (isVictory) "Victoire!" else "Défaite!")
            .setMessage(message)
            .setPositiveButton("Rejouer") { _, _ ->
                finish()
                startActivity(intent)
            }
            .setNegativeButton("Menu principal") { _, _ ->
                finish()
            }
            .setCancelable(false)
            .show()
    }

    private fun updateTimerDisplay() {
        val seconds = game.getPlayingTime().inWholeSeconds
        timerTextView.text = "Temps: ${seconds}s"
    }

    override fun onDestroy() {
        super.onDestroy()
        timerHandler.removeCallbacks(timerRunnable)
    }

    // GameObserver implementations
    override fun onCellRevealed(x: Int, y: Int) {
        // Notification is handled by adapter refresh
    }

    override fun onCellFlagged(x: Int, y: Int) {
        // Notification is handled by adapter refresh
    }

    override fun onGameWon() {
        Toast.makeText(this, "✅ Vous avez gagné!", Toast.LENGTH_SHORT).show()
    }

    override fun onGameLost(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
