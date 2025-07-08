package com.example.wordle.data

import android.content.Context
import kotlin.random.Random

class WordProvider(context: Context) {
    private val answerWords: List<String> = context.assets.open("answers.txt")
        .bufferedReader()
        .readLines()
        .map { it.trim().uppercase() } // clean lines in dictionary

    fun getRandomAnswer(): String {
        return answerWords[Random.nextInt(answerWords.size)] //returns random word from the list by index
    }
}
