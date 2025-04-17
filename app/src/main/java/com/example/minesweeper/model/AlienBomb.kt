package com.example.minesweeper.model

class AlienBomb : BombEmoji() {
    override fun getSymbol(): String {
        return alienSymbol
    }

    override fun executeBombBehavior(): String {
        return "😱 Vous avez été kidnappé par des aliens ! "
    }
}