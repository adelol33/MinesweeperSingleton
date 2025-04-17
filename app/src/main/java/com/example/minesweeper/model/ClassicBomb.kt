package com.example.minesweeper.model

class ClassicBomb : BombEmoji() {
    override fun getSymbol(): String {
        return bombSymbol
    }

    override fun executeBombBehavior(): String {
        return "💥 Une bombe a explosé !"
    }
}