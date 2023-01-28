package com.mocoding.geometryapp.common.tools

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import com.mocoding.geometryapp.common.CanvasEvent
import com.mocoding.geometryapp.common.drawing.FreeDrawing
import com.mocoding.geometryapp.common.drawing.Line

class LineTool(
    override val draw: (Line) -> Unit,
    override val drawPlaceholder: (Line) -> Unit
): Tool, Selectable, DrawingTool<Line> {

    override val selected: MutableState<Boolean> = mutableStateOf(false)

    override val icon: ImageVector =
        if (selected.value) Icons.Rounded.Menu
        else Icons.Outlined.Menu

    override val name: String = "Line Tool"

    override var placeholderDrawing: Line? = null

    override fun onSelect() {
        toggleSelect()
    }

    override fun onEvent(event: CanvasEvent) {
        when(event) {
            is CanvasEvent.DragStart -> {
                Line(startPoint = event.offset, stopPoint = Offset.Zero).let { drawing ->
                    placeholderDrawing = drawing
                    drawPlaceholder(drawing)
                }
            }
            is CanvasEvent.Drag -> {
                placeholderDrawing?.let { safePlaceholderDrawing ->
                    val drawing = Line(
                        startPoint = safePlaceholderDrawing.startPoint,
                        stopPoint = safePlaceholderDrawing.stopPoint + event.offset
                    )
                    placeholderDrawing = drawing
                    drawPlaceholder(drawing)
                }
            }
            is CanvasEvent.DragStop, CanvasEvent.DragCancel -> {
                placeholderDrawing?.let { drawing ->
                    placeholderDrawing = null
                    draw(drawing)
                }
            }
        }
    }

}