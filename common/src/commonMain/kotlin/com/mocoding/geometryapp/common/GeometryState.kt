package com.mocoding.geometryapp.common

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.geometry.Offset
import com.mocoding.geometryapp.common.drawing.Drawing
import com.mocoding.geometryapp.common.tools.LineTool
import com.mocoding.geometryapp.common.tools.Pencil
import com.mocoding.geometryapp.common.tools.Selectable
import com.mocoding.geometryapp.common.tools.Tool

@Composable
fun rememberGeometryState(

): GeometryState = rememberSaveable(saver = GeometryState.Saver) {
    GeometryState()
}

class GeometryState(

) {
    var placeholder by mutableStateOf<Drawing?>(null)
    var drawings by mutableStateOf(emptyList<Drawing>())
        private set
    var drawingsTrash by mutableStateOf(emptyList<Drawing>())
        private set

    var tools by mutableStateOf(
        listOf<Tool>(
            LineTool(
                draw = { addDrawing(it) },
                drawPlaceholder = { placeholder = it }
            ),
            Pencil(
                draw = { addDrawing(it) },
                drawPlaceholder = { placeholder = it }
            )
        )
    )
        private set

    private fun addDrawing(drawing: Drawing) {
        drawings = drawings
            .toMutableList()
            .also {
                it.add(drawing)
            }

        drawingsTrash = emptyList()

        placeholder = null
    }

    private var hoveredToolIndex: Int = -1
    private val hoveredTool: Tool? get() = tools.getOrNull(hoveredToolIndex)

    fun onEvent(event: CanvasEvent) {

        when(event) {
            is CanvasEvent.DragStart -> {
                setHoveredToolIndex(event.offset)
                hoveredTool?.onEvent(event)
            }
            else -> hoveredTool?.onEvent(event)
        }

    }

    fun undo() {
        drawings = drawings.toMutableList().also {
            it.removeLastOrNull()?.let { drawing ->
                drawingsTrash = drawingsTrash.toMutableList().also { trash ->
                    trash.add(drawing)
                }
            }
        }
    }

    fun redo() {
        drawings = drawings.toMutableList().also {
            drawingsTrash = drawingsTrash.toMutableList().also { trash ->
                trash.removeLastOrNull()?.let { drawing ->
                    it.add(drawing)
                }
            }
        }
    }

    private fun setHoveredToolIndex(point: Offset) {
        hoveredToolIndex = tools.indexOfFirst { (it is Selectable) && it.selected.value }
    }

    private fun selectTool(): Tool? {
        return null
    }

    companion object {
        val Saver: Saver<GeometryState, *> = listSaver(
            save= {
                listOf<Any>()
            },
            restore = {
                GeometryState()
            }
        )
    }

}