package com.example.wordle

import android.os.Bundle
import androidx.activity.ComponentActivity
import logic.GameEvaluator.evaluateGuess

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val result = evaluateGuess(
            guess = "ladle",
            target = "apple"
        )

        println(result)
    }
}