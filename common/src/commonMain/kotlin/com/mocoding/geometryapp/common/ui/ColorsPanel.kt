package com.mocoding.geometryapp.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mocoding.geometryapp.common.event.ColorsPanelEvent

@Composable
fun ColorsPanel(
    modifier: Modifier = Modifier,
    selectedColorIndex: Int,
    colors: List<Color>,
    onEvent: (ColorsPanelEvent) -> Unit,
) {

    Column(
        modifier = modifier.padding(10.dp)
    ) {
        colors.forEachIndexed { index, color ->
            IconButton(
                onClick = { onEvent(ColorsPanelEvent.OnColorSelected(index)) },
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .background(
                        color = if (index == selectedColorIndex) Color.LightGray else Color.Transparent,
                        shape = CircleShape
                    )
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(color)
                    ,
                )
            }
        }
    }

}