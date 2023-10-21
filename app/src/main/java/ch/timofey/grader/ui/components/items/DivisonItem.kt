package ch.timofey.grader.ui.components.items

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ch.timofey.grader.db.domain.division.Division
import ch.timofey.grader.ui.components.DismissDeleteBackground
import ch.timofey.grader.ui.components.cards.DivisionCard
import ch.timofey.grader.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DivisionItem(
    division: Division,
    onSwipe: (Division) -> Unit,
    onCheckBoxClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentItem by rememberUpdatedState(division)
    val dismissState = rememberDismissState(confirmValueChange = { dismissValue ->
        when (dismissValue) {
            DismissValue.DismissedToStart -> {
                onSwipe(currentItem)
            }

            else -> Unit
        }
        true
    }, positionalThreshold = { value -> (value / 8).dp.toPx() })
    SwipeToDismiss(
        modifier = Modifier,
        directions = setOf(DismissDirection.EndToStart),
        state = dismissState,
        background = {
            val isVisible = dismissState.targetValue == DismissValue.DismissedToStart
            AnimatedVisibility(
                visible = isVisible, enter = fadeIn(
                    animationSpec = TweenSpec(
                        durationMillis = 400
                    )
                ), exit = fadeOut(
                    animationSpec = TweenSpec(
                        durationMillis = 400
                    )
                )
            ) {
                DismissDeleteBackground(dismissState)
            }
        },
        dismissContent = {
            DivisionCard(
                modifier = Modifier.padding(MaterialTheme.spacing.small).then(modifier),
                division = division,
                onCheckBoxClick = onCheckBoxClick,
                onLongClick = onLongClick
            )
        }
    )
}