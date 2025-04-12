package com.example.minesweeper.model

abstract class BombEmoji {
    protected var bombSymbol: String = "💣"
    protected var alienSymbol: String = "👾"
    abstract fun getSymbol(): String
    abstract fun executeBombBehavior(): String
}
