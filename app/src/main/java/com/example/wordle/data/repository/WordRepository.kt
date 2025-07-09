package com.example.wordle.data.repository

import android.content.Context

class WordRepository(context: Context) {
    private val allowedWords: Set<String> = context.assets.open("words.txt")
        .bufferedReader()
        .useLines { lines ->
            lines.map { it.trim().uppercase() }.toSet()
        } // clean the lines from txt dictionary
    private val answerWords: List<String> = context.assets.open("answers.txt")
        .bufferedReader()
        .readLines()
        .map { it.trim().uppercase() } // clean lines in dictionary


    fun isValidWord(word: String): Boolean {
        return allowedWords.contains(word.uppercase()) // return true if user input is found in the dictionary
    }
    fun getRandomAnswer(): String {
        return answerWords.random() //returns random word from the list
    }
}
