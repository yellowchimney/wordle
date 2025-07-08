package com.example.wordle.data

import android.content.Context

class WordRepository(context: Context) {
    private val allowedWords: Set<String> = context.assets.open("words.txt")
        .bufferedReader()
        .useLines { lines ->
            lines.map { it.trim().uppercase() }.toSet()
        } // clean the lines from txt dictionary

    fun isValidWord(word: String): Boolean {
        return allowedWords.contains(word.uppercase()) // return true if user input is found in the dictionary
    }
}
