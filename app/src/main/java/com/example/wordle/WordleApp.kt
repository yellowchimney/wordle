package com.example.wordle

import androidx.compose.runtime.Composable
import com.example.wordle.ui.GameScreen
import com.example.wordle.ui.WordleViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue


@Composable
fun WordleGame() {
    val viewModel: WordleViewModel = viewModel()
    val currentGuess by viewModel.currentGuess
    val previousGuesses by viewModel.previousGuesses
    val gameStatus by viewModel.gameStatus
    val keyboardResults = viewModel.keyboardResults

    GameScreen(
        gameStatus = gameStatus,
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
        },
        onRestart = {viewModel.startNewGame()}
    )
}