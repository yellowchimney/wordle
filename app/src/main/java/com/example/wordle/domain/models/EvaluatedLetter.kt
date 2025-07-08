package com.example.wordle.domain.models

data class EvaluatedLetter(
    val char: Char,
    val state: LetterState
)
