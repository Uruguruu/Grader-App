package ch.timofey.grader.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ch.timofey.grader.db.domain.school.School
import ch.timofey.grader.ui.components.cards.SchoolCard
import ch.timofey.grader.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchoolItem(
    school: School,
    onSwipe: (School) -> Unit,
    onCheckBoxClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentItem by rememberUpdatedState(school)
    val dismissState = rememberDismissState(confirmValueChange = { dismissValue ->
        when (dismissValue) {
            DismissValue.DismissedToStart -> {
                onSwipe(currentItem)
            }

            else -> Unit
        }
        true
    }, positionalThreshold = { value -> (value / 8).dp.toPx() })
    SwipeToDismiss(state = dismissState,
        modifier = Modifier,
        directions = setOf(DismissDirection.EndToStart),
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
            SchoolCard(
                modifier = Modifier.padding(MaterialTheme.spacing.small).then(modifier),
                school = currentItem,
                onCheckBoxClick = onCheckBoxClick,
                onLongClick = onLongClick
            )
        })
}