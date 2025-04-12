package com.example.minesweeper.interfaces

interface GameObserver {
    fun onCellRevealed(x: Int, y: Int)
    fun onCellFlagged(x: Int, y: Int)
    fun onGameWon()
    fun onGameLost(message: String)
}