package com.example.minesweeper.model

import kotlin.time.Duration

class LeaderBoard(private val scores: ArrayList<Score> = arrayListOf()) {

    fun getScores(): List<Score> {
        return scores.toList()
    }

    fun add(score: Score) {
        scores.add(score)
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
}
