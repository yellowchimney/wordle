package com.example.wordle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.wordle.ui.GameScreen
import com.example.wordle.ui.WordleViewModel
import android.app.Application
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue


@Composable
fun WordleGame() {
    val viewModel: WordleViewModel = viewModel()
    val currentGuess by viewModel.currentGuess
    val previousGuesses by viewModel.previousGuesses
    val keyboardResults = viewModel.keyboardResults

    GameScreen(
        currentGuess = currentGuess,
        previousGuesses = previousGuesses,
        keyboardResults = keyboardResults,
        onSubmit = { guess ->
            viewModel.submitGuess(currentGuess)
        },
        onLetterClick = { letter ->
            viewModel.addLetter(letter)
        },
        onBackspace = {
            viewModel.removeLetter()
        }
    )
}