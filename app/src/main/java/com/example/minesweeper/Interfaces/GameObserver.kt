package com.example.minesweeper.interfaces

interface GameObserver {
    fun onGameWon()
    fun onGameLost(message: String)
}