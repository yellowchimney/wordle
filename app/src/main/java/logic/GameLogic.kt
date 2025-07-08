package logic

import com.example.wordle.domain.models.EvaluatedLetter
import com.example.wordle.domain.models.GameStatus
import com.example.wordle.domain.models.LetterState

class GameLogic(private val target: String, private val maxAttempts: Int = 6) {
    private val _guesses = mutableListOf<List<EvaluatedLetter>>()
    val guesses: List<List<EvaluatedLetter>> get() = _guesses.toList()
    var status: GameStatus = GameStatus.IN_PROGRESS
        private set

    fun makeGuess(guess: String): List<EvaluatedLetter>? {
        if (status != GameStatus.IN_PROGRESS || guess.length != target.length) {
            return null
        }

        val evaluated = GameEvaluator.evaluateGuess(guess, target)
        _guesses.add(evaluated)

        if (evaluated.all { it.state == LetterState.CORRECT }) {
            status = GameStatus.WON
        } else if (_guesses.size >= maxAttempts) {
            status = GameStatus.LOST
        }

        return evaluated
    }

    fun reset() {
        _guesses.clear()
        status = GameStatus.IN_PROGRESS
    }
}
