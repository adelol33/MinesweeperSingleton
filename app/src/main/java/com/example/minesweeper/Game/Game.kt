package com.example.minesweeper.game

import com.example.minesweeper.interfaces.GameObserver
import com.example.minesweeper.model.Difficulty
import com.example.minesweeper.model.GameConfig
import com.example.minesweeper.model.GameResult
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import java.time.Instant

class Game(private val difficulty: Difficulty) {
    private var playingTime: Duration = Duration.ZERO
    private var lastStartTime: Long? = null
    private var board = Board(difficulty)
    private val observers = mutableListOf<GameObserver>()

    fun addObserver(observer: GameObserver) {
        observers.add(observer)
    }

//    private fun notifyCellRevealed(x: Int, y: Int) {
//        observers.forEach { it.onCellRevealed(x, y) }
//    }
//
//    private fun notifyCellFlagged(x: Int, y: Int) {
//        observers.forEach { it.onCellFlagged(x, y) }
//    }

    private fun notifyGameWon() {
        observers.forEach { it.onGameWon() }
    }

    private fun notifyGameLost(message: String) {
        observers.forEach { it.onGameLost(message) }
    }

    fun startTimer() {
        lastStartTime = Instant.now().toEpochMilli()
    }

    fun updateTime() {
        lastStartTime?.let {
            val timeElapsed = Instant.now().toEpochMilli() - it
            playingTime += timeElapsed.milliseconds
            lastStartTime = Instant.now().toEpochMilli()
        }
    }

    fun getBoard(): Board {
        return board
    }

    fun getPlayingTime(): Duration {
        return playingTime
    }

    fun playMove(x: Int, y: Int): GameResult? {
        // Si la cellule est une bombe, on perd
        if (board.isBomb(x, y)) {
            updateTime()
            // Révéler la bombe cliquée
            board.reveal(x, y)
            // Révéler toutes les autres cellules
            board.revealAllCells()
            val message = board.executeBombBehavior(x, y) ?: "Vous avez perdu!"
            notifyGameLost(message)
            return GameResult(false, playingTime)
        }

        // Sinon, on révèle normalement
        revealCell(x, y)

        // Vérifier si le jeu est gagné
        if (board.isGameWon()) {
            updateTime()
            notifyGameWon()
            return GameResult(true, playingTime)
        }

        return null
    }


    fun flagCell(x: Int, y: Int) {
        board.flag(x, y)
        //notifyCellFlagged(x, y)
    }

    private fun revealCell(x: Int, y: Int) {
        if (x !in 0 until GameConfig.NUMBER_OF_COLUMNS || y !in 0 until GameConfig.NUMBER_OF_ROWS) {
            return
        }

        // Si la cellule est déjà révélée, ne rien faire
        if (board.isRevealed(x, y)) {
            return
        }

        board.reveal(x, y)
        //notifyCellRevealed(x, y)

        // Obtenir le nombre de bombes autour
        val numberOfBombs = board.surroundingBombs(x, y)

        // Si cette cellule n'a pas de bombes autour, alors révéler récursivement les cellules adjacentes
        if (numberOfBombs == 0) {
            for (dy in -1..1) {
                for (dx in -1..1) {
                    val newX = x + dx
                    val newY = y + dy
                    // On évite de rappeler pour la cellule actuelle
                    if (!(dx == 0 && dy == 0)) {
                        revealCell(newX, newY) // Appel récursif pour explorer
                    }
                }
            }
        }
    }
}