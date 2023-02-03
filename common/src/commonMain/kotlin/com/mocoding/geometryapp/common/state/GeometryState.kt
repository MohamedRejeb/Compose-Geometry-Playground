package com.mocoding.geometryapp.common.state

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.mocoding.geometryapp.common.drawing.Drawing
import com.mocoding.geometryapp.common.drawing.GeoDrawing
import com.mocoding.geometryapp.common.drawing.RulerDrawing
import com.mocoding.geometryapp.common.event.CanvasEvent
import com.mocoding.geometryapp.common.tools.*

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

    var visibleGeoDrawings by mutableStateOf(emptyList<GeoDrawing>())
        private set

    private var hiddenGeoDrawings by mutableStateOf(listOf<GeoDrawing>(
        RulerDrawing()
    ))

    var tools by mutableStateOf(
        listOf(
            LineTool(
                onToggleSelect = { toggleDrawingToolSelect(it) },
                draw = { addDrawing(it) },
                drawPlaceholder = { placeholder = it },
                getSelectedColor = ::getSelectedColor
            ),
            Pencil(
                onToggleSelect = { toggleDrawingToolSelect(it) },
                draw = { addDrawing(it) },
                drawPlaceholder = { placeholder = it },
                getSelectedColor = ::getSelectedColor
            ),
            Ruler(
                onToggleSelect = { toggleGeoToolSelect<RulerDrawing>(it) },
                moveBy = { moveGeoToolBy<RulerDrawing>(it) }
            )
        )
    )
        private set

    var selectedColorIndex by mutableStateOf(0)

    val colors = listOf(
        Color.Black,
        Color.Red,
        Color.Green,
        Color.Blue,
        Color.Cyan,
        Color.Yellow
    )

    private fun getSelectedColor(): Color {
        return colors.getOrElse(selectedColorIndex) { Color.Black }
    }

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

    fun selectColor(index: Int) {
        selectedColorIndex = index
    }

    private fun setHoveredToolIndex(point: Offset) {
        hoveredToolIndex = tools.indexOfFirst { it.selected }
    }

    private fun toggleDrawingToolSelect(tool: Tool) {
        val toolIndex = tools.indexOf(tool)
        if (toolIndex == -1) return
        tools = tools.mapIndexed { index, t ->
            if (index == toolIndex)
                t.updateSelected(selected = !t.selected)
            else if (t is Pencil || t is LineTool)
                t.updateSelected(selected = false)
            else
                t
        }
    }

    private inline fun <reified T: GeoDrawing> toggleGeoToolSelect(tool: Tool) {
        val toolIndex = tools.indexOf(tool)
        if (toolIndex == -1) return

        val selected = !tool.selected

        tools = tools.toMutableList().also {
            it[toolIndex] = tool.updateSelected(selected = selected)
        }

        if (selected) {
            hiddenGeoDrawings.find { it is T }?.let { drawing ->
                visibleGeoDrawings = visibleGeoDrawings.toMutableList().also {
                    it.add(drawing)
                }

                hiddenGeoDrawings = hiddenGeoDrawings.toMutableList().also {
                    it.remove(drawing)
                }
            }
        } else {
            visibleGeoDrawings.find { it is T }?.let { drawing ->
                hiddenGeoDrawings = hiddenGeoDrawings.toMutableList().also {
                    it.add(drawing)
                }

                visibleGeoDrawings = visibleGeoDrawings.toMutableList().also {
                    it.remove(drawing)
                }
            }
        }
    }

    private inline fun <reified T: GeoDrawing> moveGeoToolBy(offset: Offset) {
        val geoDrawingIndex = visibleGeoDrawings.indexOfFirst { it is T }
        if (geoDrawingIndex == -1) return

        visibleGeoDrawings = visibleGeoDrawings.toMutableList().also {
            it[geoDrawingIndex] = it[geoDrawingIndex].moveBy(offset)
        }
    }

    private fun toggleTollSelect(tool: Tool) {
        val toolIndex = tools.indexOf(tool)
        if (toolIndex == -1) return
        tools = tools.mapIndexed { index, t ->
            if (index == toolIndex)
                t.updateSelected(selected = !t.selected)
            else
                t
        }
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