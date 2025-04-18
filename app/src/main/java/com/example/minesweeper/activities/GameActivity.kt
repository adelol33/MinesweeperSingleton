package com.example.minesweeper.activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import com.example.minesweeper.R
import com.example.minesweeper.game.Game
import com.example.minesweeper.interfaces.GameObserver
import com.example.minesweeper.model.Difficulty
import com.example.minesweeper.model.GameResult
import android.content.Intent
import android.os.Build
import android.widget.EditText
import androidx.annotation.RequiresApi
import com.example.minesweeper.model.Leaderboard
import androidx.appcompat.app.AlertDialog
import com.example.minesweeper.model.IRepository
import com.example.minesweeper.model.LeaderboardRepository
import kotlin.time.Duration


class GameActivity : AppCompatActivity(), GameObserver {
    private lateinit var game: Game
    private lateinit var gridView: GridView
    private lateinit var cellAdapter: CellAdapter
    private lateinit var timerTextView: TextView
    private lateinit var flagToggle: ToggleButton
    private lateinit var pausePlayToggle: ToggleButton
    private var isGameOver = false
    private var isGamePaused = false
    private val timerHandler = Handler(Looper.getMainLooper())
    private val timerRunnable = object : Runnable {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun run() {
            if (!isGameOver && !isGamePaused) {
                game.updateTime()
                updateTimerDisplay()
                timerHandler.postDelayed(this, 1000)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
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
        pausePlayToggle = findViewById(R.id.pausePlayToggle)

        // Setup cell adapter
        cellAdapter = CellAdapter(this, game.getBoard())
        gridView.adapter = cellAdapter

        // Setup click listeners
        setupGridClickListener()
        setupPausePlayButton()

        // Start game timer
        game.startTimer()
        timerHandler.post(timerRunnable)
    }

    private fun setupPausePlayButton() {
        pausePlayToggle.setOnCheckedChangeListener { _, isChecked ->
            isGamePaused = isChecked
            if (isGamePaused) {
                // Jeu en pause
                timerHandler.removeCallbacks(timerRunnable)
                // Désactiver le clic sur la grille pendant la pause
                gridView.isEnabled = false
                Toast.makeText(this, "Jeu en pause", Toast.LENGTH_SHORT).show()
            } else {
                // Reprendre le jeu
                if (!isGameOver) {
                    timerHandler.post(timerRunnable)
                    // Réactiver le clic sur la grille
                    gridView.isEnabled = true
                    Toast.makeText(this, "Jeu repris", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupGridClickListener() {
        gridView.setOnItemClickListener { _, _, position, _ ->
            if (isGameOver || isGamePaused) return@setOnItemClickListener

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
            if (result.isVictory) {
                showVictoryDialog(result.timePlayed)
            } else {
                showGameResultDialog(message, result.isVictory)
            }
        }, 500)
    }


    private fun showLeaderboardOption() {
        try {
            AlertDialog.Builder(this)
                .setTitle("Voir le classement?")
                .setMessage("Voulez-vous consulter le classement?")
                .setPositiveButton("Oui") { _, _ ->
                    try {
                        val intent = Intent(this, LeaderboardActivity::class.java)
                        startActivity(intent)
                        finish()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(this, "Erreur lors de l'ouverture du classement", Toast.LENGTH_SHORT).show()
                        navigateToMainMenu()
                    }
                }
                .setNegativeButton("Non") { _, _ ->
                    navigateToMainMenu()
                }
                .setCancelable(false)
                .show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Erreur lors de l'affichage du dialogue", Toast.LENGTH_SHORT).show()
            navigateToMainMenu()
        }
    }

    private fun showGameResultDialog(message: String, isVictory: Boolean) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(if (isVictory) "Victoire!" else "Défaite!")
            .setMessage(message)
            .setPositiveButton("Rejouer") { _, _ ->
                // Réinitialiser le singleton Board
                com.example.minesweeper.game.Board.resetInstance()
                finish()
                startActivity(intent)
            }
            .setNegativeButton("Menu principal") { _, _ ->
                // Créer un intent pour MainActivity et effacer la pile d'activités
                val intent = android.content.Intent(this, MainActivity::class.java)
                intent.flags = android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP or android.content.Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
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


    override fun onGameWon() {
        Toast.makeText(this, "✅ Vous avez gagné!", Toast.LENGTH_SHORT).show()
    }

    override fun onGameLost(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        isGameOver = true
        cellAdapter.notifyDataSetChanged() // Cette ligne est importante!
    }

    private fun handleGameOver(result: GameResult) {
        if (result.isVictory) {
            showVictoryDialog(result.timePlayed)
        } else {
            // Gestion de la défaite (code existant)
            Toast.makeText(this, "Vous avez perdu !", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
    override fun onPause() {
        super.onPause()
        // Si le jeu n'est pas encore en pause et qu'il n'est pas terminé, le mettre en pause
        if (!isGamePaused && !isGameOver) {
            pausePlayToggle.isChecked = true
            isGamePaused = true
            timerHandler.removeCallbacks(timerRunnable)
        }
    }

    override fun onResume() {
        super.onResume()
        // Le jeu ne reprend pas automatiquement, l'utilisateur doit appuyer sur le bouton play
    }

    private fun showVictoryDialog(timePlayed: Duration) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Félicitations !")
        builder.setMessage("Vous avez gagné en ${timePlayed.inWholeSeconds} secondes !")

        // Créer un EditText pour saisir le nom
        val input = EditText(this)
        builder.setView(input)

        builder.setPositiveButton("Enregistrer") { _, _ ->
            val playerName = input.text.toString()
            if (playerName.isNotBlank()) {
                saveScore(playerName, timePlayed)
            } else {
                // Informer l'utilisateur que le nom est requis
                Toast.makeText(this, "Veuillez entrer un nom", Toast.LENGTH_SHORT).show()
                // Retour au menu principal même si pas d'enregistrement
                navigateToMainMenu()
            }
        }

        builder.setNegativeButton("Annuler") { _, _ ->
            navigateToMainMenu()
        }

        builder.setCancelable(false)
        builder.show()
    }
    private fun navigateToMainMenu() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun saveScore(playerName: String, timePlayed: Duration) {
        try {
            // Récupérer la difficulté correctement
            val difficultyName = intent.getStringExtra("DIFFICULTY") ?: Difficulty.EASY.name
            val difficulty = Difficulty.valueOf(difficultyName)

            // Ajouter le score au leaderboard
            val leaderboardRepository: IRepository<Difficulty, Leaderboard> = LeaderboardRepository(this)

            val leaderBoard = leaderboardRepository.read(difficulty)
            leaderBoard.addScore(playerName, timePlayed, difficulty)

            leaderboardRepository.save(leaderBoard)

            // Log pour débogage
            android.util.Log.d("GameActivity", "Score sauvegardé: $playerName, ${timePlayed.inWholeSeconds}s, $difficultyName")

            // Proposer de voir le leaderboard
            showLeaderboardOption()
        } catch (e: Exception) {
            // En cas d'erreur, log et informer l'utilisateur
            e.printStackTrace()
            android.util.Log.e("GameActivity", "Erreur lors de l'enregistrement du score", e)
            Toast.makeText(this, "Erreur lors de l'enregistrement du score", Toast.LENGTH_SHORT).show()
            navigateToMainMenu()
        }
    }
}
