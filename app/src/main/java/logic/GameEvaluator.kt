package logic

import com.example.wordle.domain.models.EvaluatedLetter
import com.example.wordle.domain.models.LetterState

object GameEvaluator {

    fun evaluateGuess(guess: String, target: String): List<EvaluatedLetter> {
        val guessChars = guess.uppercase().toCharArray()
        val targetChars = target.uppercase().toCharArray()
        val result = MutableList(5) { index ->
            EvaluatedLetter(guessChars[index], LetterState.ABSENT) //by default let's mark all chars as absent
        }
        val letterUsage = mutableMapOf<Char, Int>()

        for (char in targetChars) {
            letterUsage[char] = letterUsage.getOrDefault(char, 0) + 1
        }

        for (i in guessChars.indices) {
            if (guessChars[i] == targetChars[i]) {
                result[i] = EvaluatedLetter(guessChars[i], LetterState.CORRECT) // find all correctly guessed chars
                letterUsage[guessChars[i]] = letterUsage[guessChars[i]]!! - 1
            }
        }

        for (i in guessChars.indices) {
            if (result[i].state == LetterState.CORRECT) continue
            val ch = guessChars[i]
            if (letterUsage.getOrDefault(ch, 0) > 0) {
                result[i] = EvaluatedLetter(ch, LetterState.PRESENT) // for the ones that are not guessed correctly mark chars that are present
                letterUsage[ch] = letterUsage[ch]!! - 1
            }
        }

        return result
    }
}

