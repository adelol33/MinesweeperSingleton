package com.example.minesweeper.model

import kotlin.time.Duration

class Score(private val name: String, private val time: Duration, private val difficulty: Difficulty) {

    fun getName(): String = name

    fun getTime(): Duration = time

    fun getDifficulty(): Difficulty = difficulty

    override fun toString(): String {
        return "$name - $time - ${difficulty.name}"
    }
}