package com.mocoding.geometryapp.common.tools

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LinearScale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.mocoding.geometryapp.common.event.CanvasEvent
import com.mocoding.geometryapp.common.drawing.Line

data class LineTool(
    override val selected: Boolean = false,
    override val onToggleSelect: (Tool) -> Unit,

    override val draw: (Line) -> Unit,
    override val drawPlaceholder: (Line) -> Unit,
    override val getSelectedColor: () -> Color
): Tool, DrawingTool<Line> {

    override val icon: ImageVector = Icons.Rounded.LinearScale
    override val name: String = "Line Tool"
    override var placeholderDrawing: Line? = null

    override fun onSelect() {
        onToggleSelect(this)
    }

    override fun onEvent(event: CanvasEvent) {
        when(event) {
            is CanvasEvent.DragStart -> {
                Line(
                    startPoint = event.offset,
                    stopPoint = Offset.Zero,
                    color = getSelectedColor()
                ).let { drawing ->
                    placeholderDrawing = drawing
                    drawPlaceholder(drawing)
                }
            }
            is CanvasEvent.Drag -> {
                placeholderDrawing?.let { safePlaceholderDrawing ->
                    val drawing = Line(
                        startPoint = safePlaceholderDrawing.startPoint,
                        stopPoint = safePlaceholderDrawing.stopPoint + event.offset,
                        color = getSelectedColor()
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

    override fun updateSelected(selected: Boolean): Tool {
        return copy(selected = selected)
    }

}