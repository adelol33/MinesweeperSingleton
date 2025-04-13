package com.example.minesweeper.model

import com.google.gson.annotations.SerializedName
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class Score private constructor(
    @SerializedName("name") private val name: String,
    @SerializedName("timeSeconds") private val timeSeconds: Long,
    @SerializedName("difficulty") private val difficulty: Difficulty
) {
    companion object {
        fun fromDuration(name: String, time: Duration, difficulty: Difficulty): Score {
            return Score(name, time.inWholeSeconds, difficulty)
        }
    }

    fun getName(): String = name

    fun getTime(): Duration = timeSeconds.seconds

    fun getDifficulty(): Difficulty = difficulty

    override fun toString(): String {
        return "$name - ${getTime()} - ${difficulty.label}"
    }
}
