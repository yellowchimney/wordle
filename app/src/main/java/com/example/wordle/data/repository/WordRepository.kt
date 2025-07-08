package com.example.wordle.data.repository

import android.content.Context

class WordRepository(private val context: Context) {

    fun getAllowedWords(): Set<String> {
        return try {
            context.assets.open("words.txt").bufferedReader().readLines()
                .map { it.uppercase().trim() }
                .filter { it.length == 5 }
                .toSet()
        } catch (e: Exception) {
            emptySet()
        }
    }

    fun getTargetWords(): List<String> {
        return try {
            context.assets.open("answers.txt").bufferedReader().readLines()
                .map { it.uppercase().trim() }
                .filter { it.length == 5 }
        } catch (e: Exception) {
            listOf("CRANE", "SPEED", "HOUSE", "PLANT", "WORLD")
        }
    }
}