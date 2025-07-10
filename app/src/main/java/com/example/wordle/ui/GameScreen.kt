package com.example.wordle.ui


import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordle.R
import com.example.wordle.domain.models.EvaluatedLetter
import com.example.wordle.domain.models.GameStatus
import com.example.wordle.domain.models.LetterState
import kotlin.math.roundToInt
import androidx.compose.runtime.setValue


@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    gameStatus: Enum<GameStatus>,
    currentGuess: String,
    previousGuesses: List<List<EvaluatedLetter>>,
    keyboardResults: Map<Char, LetterState>,
    onSubmit: (String) -> Unit,
    onLetterClick: (Char) -> Unit,
    onBackspace: () -> Unit,
    onRestart: () -> Unit,
    shouldShake: Boolean
) {
    // Convert string to padded list for display
    val currentPosition = currentGuess.length.coerceAtMost(5)
    var enterIsClicked by remember { mutableStateOf(0) }




    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Spacer(modifier = Modifier.height(14.dp))
        // Word input row (5 boxes)

        Grid(previousGuesses, currentGuess, shouldShake, enterIsClicked)

        Spacer(modifier = Modifier.weight(1f))

        when (gameStatus) {
            GameStatus.IN_PROGRESS -> {
                // Custom Wordle-style keyboard
                WordleKeyboard(
                    onLetterClick = onLetterClick,
                    onBackspace = onBackspace,
                    onEnter = {
                        if (currentGuess.length == 5) {
                            onSubmit(currentGuess)
                            if (shouldShake) {
                                enterIsClicked++
                            }
                        }
                    },
                    keyboardResults = keyboardResults,
                    canSubmit = currentGuess.length == 5
                )
            }
            GameStatus.WON -> {
                WinLoseBlock(
                    modifier = modifier,
                    text = stringResource(R.string.win_message),
                    onClick = onRestart
                )
            }
            else -> {
                WinLoseBlock(
                    modifier = modifier,
                    text = stringResource(R.string.lose_message),
                    onClick = onRestart
                )

            }
        }
    }
}

@Composable
fun WordleKeyboard(
    modifier: Modifier = Modifier,
    onLetterClick: (Char) -> Unit,
    onBackspace: () -> Unit,
    onEnter: () -> Unit,
    keyboardResults: Map<Char, LetterState>,
    canSubmit: Boolean = true,
) {
    val keyboardRows = listOf(
        listOf('Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P'),
        listOf('A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L'),
        listOf('Z', 'X', 'C', 'V', 'B', 'N', 'M')
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // First two rows
        keyboardRows.take(2).forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(horizontal = 4.dp)
            ) {
                row.forEach { letter ->
                    KeyboardButton(
                        text = letter.toString(),
                        onClick = { onLetterClick(letter) },
                        state = keyboardResults[letter] ?: LetterState.UNUSED,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Third row with Enter and Backspace
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            // Enter button
            KeyboardButton(
                text = "ENTER",
                onClick = onEnter,
                state = LetterState.UNUSED,
                enabled = canSubmit,
                modifier = Modifier.weight(2f)
            )

            // Letter keys
            keyboardRows[2].forEach { letter ->
                KeyboardButton(
                    text = letter.toString(),
                    onClick = { onLetterClick(letter) },
                    state = keyboardResults[letter] ?: LetterState.UNUSED,
                    modifier = Modifier.weight(1f)
                )
            }

            // Backspace button
            KeyboardButton(
                text = "⌫",
                onClick = onBackspace,
                state = LetterState.UNUSED,
                modifier = Modifier.weight(1.5f)
            )
        }
    }
}

@Composable
fun KeyboardButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    state: LetterState,
    enabled: Boolean = true,
) {
    val backgroundColor = when {
        !enabled -> Color(0xFFD3D6DA).copy(alpha = 0.5f)
        state == LetterState.CORRECT -> Color(0xFF6AAA64) // Green
        state == LetterState.PRESENT -> Color(0xFFC9B458) // Yellow
        state == LetterState.ABSENT -> Color(0xFF787C7E) // Gray
        else -> Color(0xFFD3D6DA) // Light gray
    }

    val textColor = when {
        !enabled -> Color.Gray
        state == LetterState.UNUSED -> Color.Black
        else -> Color.White
    }

    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = textColor,
            disabledContainerColor = backgroundColor,
            disabledContentColor = textColor
        ),
        shape = RoundedCornerShape(4.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun WinLoseBlock(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {
    Text(
        text = text,
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(8.dp))
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.onTertiaryContainer,
            contentColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Text(
            modifier = Modifier.padding(10.dp),
            text = stringResource(R.string.restart_button),
            color = MaterialTheme.colorScheme.tertiaryContainer,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
    Spacer(modifier = Modifier.height(10.dp))
}


@Composable
fun LetterBox(letter: Char?, state: LetterState?, currentPosition: Int? = null, index: Int? = null) {
    val backgroundColor = when (state) {
        LetterState.CORRECT -> Color(0xFF6AAA64) // Green
        LetterState.PRESENT -> Color(0xFFC9B458) // Yellow
        LetterState.ABSENT -> Color(0xFF787C7E) // Gray
        else -> Color(0xFFD3D6DA) // Light gray
    }

    Box(
        modifier = Modifier
            .size(60.dp)
            .background(
                backgroundColor,
                RoundedCornerShape(8.dp)
            )
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        if (letter != null) {
            Text(
                letter.toString().uppercase(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun Modifier.shake(enterIsClicked: Int, shouldShake: Boolean): Modifier {
    val offsetX = remember { Animatable(0f) }
    var isFirstLoad by remember { mutableStateOf(true) }

    LaunchedEffect(enterIsClicked) {
        if (!isFirstLoad && shouldShake) {
            // Shake: left-right-left-right-neutral
            offsetX.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 100) // reset immediately
            )
            listOf(-12f, 12f, -8f, 8f, -4f, 4f, 0f).forEach {
                offsetX.animateTo(it, animationSpec = tween(durationMillis = 40))
            }
        }
        if (isFirstLoad) {
            isFirstLoad = false
        }
    }

    return this.offset {
        IntOffset(offsetX.value.roundToInt(), 0)
    }
}

@Composable
fun Grid(previousGuesses: List<List<EvaluatedLetter>>, currentGuess: String, shouldShake: Boolean, enterIsClicked: Int) {
    //    LaunchedEffect(shouldShake) {
//    }
    for (rowIndex in 0 until 6) {
        when {
            rowIndex < previousGuesses.size -> {
                // Full evaluated row
                val evaluatedRow = previousGuesses[rowIndex]
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Absolute.SpaceEvenly
                ){
                    evaluatedRow.forEach{ eval ->
                        LetterBox(
                            letter = eval.char,
                            state = eval.state
                            )
                    }
                }
            }

            rowIndex == previousGuesses.size -> {
                // Current guess row
                val letters = currentGuess.padEnd(5).toCharArray()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shake(enterIsClicked, shouldShake),
                    horizontalArrangement = Arrangement.Absolute.SpaceEvenly
                ) {
                    letters.forEach { char ->
                        LetterBox(letter = if (char != ' ') char else null, state = null)
                    }
                }
            }

            else -> {
                // Future guesses - completely empty
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Absolute.SpaceEvenly,

                    ){
                    repeat(5) {
                        LetterBox(letter = null, state = null)
                    }
                }
            }
        }
    }
}