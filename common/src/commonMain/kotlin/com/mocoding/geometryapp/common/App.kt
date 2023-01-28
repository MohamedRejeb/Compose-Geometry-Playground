package com.mocoding.geometryapp.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.rememberDialogState
import com.mocoding.geometryapp.common.drawing.Drawing
import com.mocoding.geometryapp.common.tools.Pencil

@Composable
fun App() {
    val platformName = getPlatformName()

    val geo = rememberGeometryState()

    Box {
        PlaygroundCanvas(
            modifier = Modifier.fillMaxSize(),
            drawings = geo.placeholder?.let { drawing -> geo.drawings + drawing } ?: geo.drawings,
            onEvent = { event ->
                geo.onEvent(event)
                /*
                when(event) {
                    is CanvasEvent.DragStart -> {
                        paths = paths.toMutableList().also {
                            it.add(listOf(event.offset))
                        }
                        pointsTrash = emptyList()
                    }

                    is CanvasEvent.DragCancel -> {
                        paths = paths.toMutableList().also {
                            it[it.lastIndex] = it[it.lastIndex].toMutableList().also { points ->
                                points.add(event.offset)
                            }
                        }
                    }
                }
                */
            }
        )

        ToolsPanel(
            modifier = Modifier.fillMaxHeight(0.8f),
            tools = geo.tools,
            onEvent = { event ->
                when(event) {
                    is ToolsPanelEvent.Undo -> {
                        geo.undo()
                    }

                    is ToolsPanelEvent.Redo -> {
                        geo.redo()
                    }
                }
            },
            isUndoEnabled = geo.drawings.isNotEmpty(),
            isRedoEnabled = geo.drawingsTrash.isNotEmpty(),
        )
    }
}
