package com.mocoding.geometryapp.common.tools

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.mocoding.geometryapp.common.event.CanvasEvent
import com.mocoding.geometryapp.common.drawing.FreeDrawing

data class Pencil(
    override val selected: Boolean = false,
    override val onToggleSelect: (Tool) -> Unit,

    override val draw: (FreeDrawing) -> Unit,
    override val drawPlaceholder: (FreeDrawing) -> Unit,
    override val getSelectedColor: () -> Color
): Tool, DrawingTool<FreeDrawing> {

    override val icon: ImageVector get() = Icons.Outlined.Edit

    override val name: String = "Free Drawing"

    override var placeholderDrawing: FreeDrawing? = null

    override fun onSelect() {
        onToggleSelect(this)
    }

    override fun onEvent(event: CanvasEvent) {
        when(event) {
            is CanvasEvent.DragStart -> {
                FreeDrawing(
                    points = listOf(event.offset),
                    color = getSelectedColor()
                ).let { drawing ->
                    placeholderDrawing = drawing
                    drawPlaceholder(drawing)
                }
            }
            is CanvasEvent.Drag -> {
                placeholderDrawing?.addPoint(event.offset)?.let { drawing ->
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