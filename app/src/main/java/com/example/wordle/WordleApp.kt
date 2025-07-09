package com.example.wordle

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import com.example.wordle.ui.GameScreen
import com.example.wordle.ui.WordleViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordleGame() {
    val viewModel: WordleViewModel = viewModel()
    val currentGuess by viewModel.currentGuess
    val previousGuesses by viewModel.previousGuesses
    val gameStatus by viewModel.gameStatus
    val keyboardResults = viewModel.keyboardResults

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                colors = topAppBarColors(

                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(stringResource(R.string.top_bar_title))
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.height(48.dp),
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.tertiary,
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = stringResource(R.string.bottom_bar_title),
                )
            }
        },
    ) {innerPadding ->
        GameScreen(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
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
}
