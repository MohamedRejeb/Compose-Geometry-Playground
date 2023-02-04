package com.mocoding.geometryapp.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mocoding.geometryapp.common.event.ToolsPanelEvent
import com.mocoding.geometryapp.common.tools.Tool
import com.mocoding.geometryapp.common.tools.onToggleSelect

@Composable
fun ToolsPanel(
    modifier: Modifier = Modifier,
    tools: List<Tool>,
    onEvent: (ToolsPanelEvent) -> Unit,
    isUndoEnabled: Boolean,
    isRedoEnabled: Boolean
) {

    Column(
        modifier = modifier.padding(10.dp)
    ) {
        IconButton(
            onClick = {
                onEvent(ToolsPanelEvent.Undo)
            },
            enabled = isUndoEnabled
        ) {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = "Undo"
            )
        }

        IconButton(
            onClick = {
                onEvent(ToolsPanelEvent.Redo)
            },
            enabled = isRedoEnabled
        ) {
            Icon(
                Icons.Default.ArrowForward,
                contentDescription = "Redo"
            )
        }

        tools.forEach { tool ->
            IconButton(
                onClick = { tool.onToggleSelect() },
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .background(
                        color = if (tool.selected) Color.Black.copy(alpha = 0.1f) else Color.Transparent,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    tool.icon,
                    contentDescription = tool.name
                )
            }
        }
    }

}