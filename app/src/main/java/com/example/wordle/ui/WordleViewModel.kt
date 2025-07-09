package com.example.wordle.ui

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.example.wordle.data.repository.WordRepository
import com.example.wordle.domain.models.EvaluatedLetter
import com.example.wordle.domain.models.GameStatus
import com.example.wordle.domain.models.LetterState
import logic.GameEvaluator.evaluateGuess
import androidx.compose.runtime.getValue

class WordleViewModel(application: Application) : AndroidViewModel(application) {
    private val wordRepository = WordRepository(getApplication())
    private val target = wordRepository.getRandomAnswer()

    //game status
    private val _gameStatus = mutableStateOf(GameStatus.IN_PROGRESS)
    val gameStatus: MutableState<GameStatus> = _gameStatus

    //state of current guess -from ui input
    private val _currentGuess = mutableStateOf("")
    val currentGuess: State<String> = _currentGuess

    //list of all previous letter results (EvaluatedLetter - char, state)
    private val _previousGuesses = mutableStateOf(listOf<List<EvaluatedLetter>>())
    val previousGuesses: State<List<List<EvaluatedLetter>>> = _previousGuesses


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

    val keyboardResults by derivedStateOf {
        val results = mutableMapOf<Char, LetterState>()
        _previousGuesses.value.flatten().forEach { evaluatedLetter ->
            val letter = evaluatedLetter.char
            val currentResult = evaluatedLetter.state
            val existingResult = results[letter]

            // Keep the best previous result for the keyboard render
            val newResult = when {
                currentResult == LetterState.CORRECT -> currentResult
                currentResult == LetterState.PRESENT &&
                        existingResult != LetterState.CORRECT -> currentResult

                currentResult == LetterState.ABSENT &&
                        existingResult == null -> currentResult

                else -> existingResult ?: currentResult
            }
            results[letter] = newResult
        }
        results
    }

    fun submitGuess(guess: String) {
        // if guess not in set of allowed words then try again ?? HOW DO WE WANT TO HANDLE?
        if (!wordRepository.isValidWord(guess)) {
            return
        } else {
            // use Irina's function to give results
            val results = evaluateGuess(guess, target)

            // update previous guesses with new word
            _previousGuesses.value = _previousGuesses.value + listOf(results)

            // Check game status
            if (results.all { it.state == LetterState.CORRECT }) {
                _gameStatus.value = GameStatus.WON
            } else if (_previousGuesses.value.size >= 6) {
                _gameStatus.value = GameStatus.LOST
            }
            // clear current guess value after this turn added
            _currentGuess.value = ""
        }
    }
}

