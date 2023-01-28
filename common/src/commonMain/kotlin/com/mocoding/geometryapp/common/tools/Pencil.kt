package com.mocoding.geometryapp.common.tools

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import com.mocoding.geometryapp.common.CanvasEvent
import com.mocoding.geometryapp.common.drawing.Drawing
import com.mocoding.geometryapp.common.drawing.FreeDrawing

class Pencil(
    override val draw: (FreeDrawing) -> Unit,
    override val drawPlaceholder: (FreeDrawing) -> Unit
): Tool, Selectable, DrawingTool<FreeDrawing> {

    override val selected: MutableState<Boolean> = mutableStateOf(false)

    override val icon: ImageVector get() =
        if (selected.value) Icons.Rounded.Edit
        else Icons.Outlined.Edit

    override val name: String = "Free Drawing"

    override var placeholderDrawing: FreeDrawing? = null

    override fun onSelect() {
        toggleSelect()
    }

    override fun onEvent(event: CanvasEvent) {
        when(event) {
            is CanvasEvent.DragStart -> {
                FreeDrawing(points = listOf(event.offset)).let { drawing ->
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

}