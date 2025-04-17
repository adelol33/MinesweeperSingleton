package com.example.minesweeper.model

class AlienBomb : BombEmoji() {
    override fun getSymbol(): String {
        return alienSymbol
    }

    override fun executeBombBehavior(): String {
        return "😱 You got kidnapped by aliens!"
    }
}