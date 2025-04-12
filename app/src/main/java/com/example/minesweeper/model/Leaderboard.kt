package com.example.minesweeper.model

import kotlin.time.Duration

class LeaderBoard(private val scores: ArrayList<Score> = arrayListOf()) {

    fun getScores(): List<Score> {
        return scores.toList() // Retourne une copie immuable de la liste
    }

    override fun toString(): String {
        return buildString {
            appendLine("LeaderBoard")
            appendLine("-----------")
            for (score in scores) {
                appendLine(score.toString())
            }
        }
    }

    fun add(name: String, time: Duration, difficulty: Difficulty) {
        scores.add(Score(name, time, difficulty))
    }
}