package com.example.wordle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.wordle.ui.theme.WordleTheme
import logic.GameEvaluator.evaluateGuess

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ContextProvider.init(this)

        setContent {
            WordleTheme(dynamicColor = false) {
                WordleGame()
            }
        }
    }
}