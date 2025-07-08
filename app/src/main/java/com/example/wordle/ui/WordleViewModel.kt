package com.example.wordle.ui

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import domain.GameEvaluator.evaluateGuess
import domain.LetterState
import com.example.wordle.data.repository.WordRepository

class WordleViewModel(application: Application) : AndroidViewModel(application) {
    private val wordRepository = WordRepository(getApplication())
    private val allowedWords = wordRepository.getAllowedWords()
    private val targetWords = wordRepository.getTargetWords()
    private val target = targetWords.random()

    // irina game status
//    private val _gameStatus = mutableStateOf(GameStatus.IN_PROGRESS)
//    val gameState: MutableState<GameStatus> = _gameStatus

    //state of current guess -from ui input
    private val _currentGuess = mutableStateOf("")
    val currentGuess: State<String> = _currentGuess

    //list of all previous letter results (EvaluatedLetter - char, state)
    private val _previousResults = mutableStateOf(mapOf<Char, LetterState>())
    val previousResults: State<Map<Char, LetterState>> = _previousResults


    fun addLetter(letter: Char) {
        if (_currentGuess.value.length < 5) {
            _currentGuess.value += letter
        }
    }

    fun removeLetter() {
        if (_currentGuess.value.isNotEmpty()) {
            _currentGuess.value = _currentGuess.value.dropLast(1)
        }
    }

    fun submitGuess(guess: String) {
        // if guess not in set of allowed words then try again ?? HOW DO WE WANT TO HANDLE?
        if (!allowedWords.contains(guess)) {
            return
        } else {
            // use Irina's function to give results
            val results = evaluateGuess(guess, target)

            // use results to update colour states for letters and previous results list
            results.forEach { evaluatedLetter ->
                val currentResult = evaluatedLetter.state
                val letter = evaluatedLetter.char
                val existingResult = _previousResults.value[letter]

                val newResult = when {
                    currentResult == LetterState.CORRECT -> currentResult
                    currentResult == LetterState.PRESENT &&
                            existingResult != LetterState.CORRECT -> currentResult

                    currentResult == LetterState.ABSENT &&
                            existingResult == null -> currentResult

                    else -> existingResult ?: currentResult
                }

                _previousResults.value = _previousResults.value + (letter to newResult)
            }
            // clear current guess value after this turn added
            _currentGuess.value = ""
        }
    }
}

