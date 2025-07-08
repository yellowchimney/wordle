package com.example.wordle.domain.models

data class GameState(
    val target: String,
    val guesses: List<List<EvaluatedLetter>> = emptyList(),
    val status: GameStatus = GameStatus.IN_PROGRESS,
    val maxAttempts: Int = 6
)

// not sure if needed - keeping it in case the state is tricky to list to ViewModel