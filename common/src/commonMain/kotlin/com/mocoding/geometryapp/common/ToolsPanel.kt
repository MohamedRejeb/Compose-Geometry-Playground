package com.mocoding.geometryapp.common

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mocoding.geometryapp.common.tools.Tool

@Composable
fun ToolsPanel(
    modifier: Modifier = Modifier,
    tools: List<Tool>,
    onEvent: (ToolsPanelEvent) -> Unit,
    isUndoEnabled: Boolean,
    isRedoEnabled: Boolean
) {

    Column {
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
                onClick = { tool.onSelect() }
            ) {
                Icon(
                    tool.icon,
                    contentDescription = tool.name
                )
            }
        }
    }

}