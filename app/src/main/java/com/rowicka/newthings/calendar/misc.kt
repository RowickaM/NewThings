package com.rowicka.newthings.calendar

import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun IconButton(
    onClick: () -> Unit,
    icon: ImageVector,
) {
    androidx.compose.material.IconButton(onClick = onClick) {
        Icon(imageVector = icon, contentDescription = null)
    }
}