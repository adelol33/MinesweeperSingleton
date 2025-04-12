package com.example.minesweeper.model

class Cell {
    private var bomb: BombEmoji? = null
    private var isRevealed: Boolean = false
    private var isFlagged: Boolean = false

    fun isRevealed() = isRevealed
    fun isBomb() = bomb != null
    fun markAsBomb(bombEmoji: BombEmoji) {
        bomb = bombEmoji
    }

    fun isFlagged() = isFlagged
    fun flag() {
        if (!isRevealed) isFlagged = !isFlagged
    }

    fun reveal() {
        isRevealed = true
        isFlagged = false
    }

    fun getBombSymbol(): String? {
        return bomb?.getSymbol()
    }

    fun executeBombBehavior(): String? {
        return bomb?.executeBombBehavior()
    }
}