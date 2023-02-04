package com.mocoding.geometryapp.common.tools

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LinearScale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.mocoding.geometryapp.common.event.CanvasEvent
import com.mocoding.geometryapp.common.drawing.LineDrawing

data class LineTool(
    override val selected: Boolean = false,
    override val onToggleSelect: (name: String) -> Unit,

    override val draw: (LineDrawing) -> Unit,
    override val drawPlaceholder: (LineDrawing) -> Unit,
    override val getSelectedColor: () -> Color
): DrawingTool<LineDrawing> {

    override val icon: ImageVector = Icons.Rounded.LinearScale
    override val name: String = "Line Tool"
    override var placeholderDrawing: LineDrawing? = null

    override fun onEvent(event: CanvasEvent) {
        when(event) {
            is CanvasEvent.DragStart -> {
                LineDrawing(
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
                    val drawing = LineDrawing(
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

    override fun updateSelected(selected: Boolean): LineTool {
        return copy(selected = selected)
    }

}