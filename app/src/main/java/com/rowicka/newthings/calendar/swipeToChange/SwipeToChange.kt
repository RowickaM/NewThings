package com.rowicka.newthings.calendar.swipeToChange

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch

enum class SwipeDirection {
    Left,
    Initial,
    Right,
}

@ExperimentalMaterialApi
@Composable
fun SwipeToChange(
    onLeft: (() -> Unit)? = null,
    onRight: (() -> Unit)? = null,
    actionOffset: Float = 100f,
    content: @Composable () -> Unit
) {
    val swipeableState = rememberSwipeableState(SwipeDirection.Initial)

    val anchor = mutableMapOf<Float, SwipeDirection>()
    onLeft?.let { anchor[-actionOffset] = SwipeDirection.Left }
    anchor[0f] = SwipeDirection.Initial
    onRight?.let { anchor[actionOffset] = SwipeDirection.Right }

    val scope = rememberCoroutineScope()

    if (swipeableState.isAnimationRunning) {
        DisposableEffect(Unit) {

            onDispose {
                when (swipeableState.currentValue) {
                    SwipeDirection.Right -> {
                        onRight?.invoke()
                    }
                    SwipeDirection.Left -> {
                        onLeft?.invoke()
                    }
                    else -> {
                        return@onDispose
                    }
                }
                scope.launch {
                    swipeableState.snapTo(targetValue = SwipeDirection.Initial)
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .swipeable(
                state = swipeableState,
                anchors = anchor,
                thresholds = { _, _ -> FractionalThreshold(0.3f) },
                orientation = Orientation.Horizontal
            )
    ) {
        content()
    }
}