package com.mocoding.geometryapp.common.state

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import com.mocoding.geometryapp.common.drawing.geo.CompassDrawing
import com.mocoding.geometryapp.common.drawing.Drawing
import com.mocoding.geometryapp.common.drawing.geo.GeoDrawing
import com.mocoding.geometryapp.common.drawing.geo.ProtractorDrawing
import com.mocoding.geometryapp.common.drawing.geo.RulerDrawing
import com.mocoding.geometryapp.common.event.CanvasEvent
import com.mocoding.geometryapp.common.tools.*
import com.mocoding.geometryapp.common.tools.geo.Compass
import com.mocoding.geometryapp.common.tools.geo.Protractor
import com.mocoding.geometryapp.common.tools.geo.Ruler

@Composable
fun rememberGeometryState(

): GeometryState = rememberSaveable(saver = GeometryState.Saver) {
    GeometryState()
}

class GeometryState {
    var isDarkMode by mutableStateOf(false)

    var placeholder by mutableStateOf<Drawing?>(null)
    var drawings by mutableStateOf(emptyList<Drawing>())
        private set
    var drawingsTrash by mutableStateOf(emptyList<Drawing>())
        private set

    var visibleGeoDrawings by mutableStateOf(emptyList<GeoDrawing>())
        private set

    private var hiddenGeoDrawings by mutableStateOf(listOf(
        RulerDrawing(),
        CompassDrawing(),
        ProtractorDrawing()
    ))

    var drawingTools by mutableStateOf(
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
                getSelectedColor = ::getSelectedColor,
                getRulerDrawing = { visibleGeoDrawings.find { it is RulerDrawing } as? RulerDrawing }
            ),
        )
    )
        private set

    var geoTools by mutableStateOf(
        listOf(
            Ruler(
                onToggleSelect = { toggleGeoToolSelect<RulerDrawing>(it) },
                moveBy = { moveGeoToolBy<RulerDrawing>(it) },
                rotateBy = { rotateGeoToolBy<RulerDrawing>(it) },
                getGeoDrawing = { visibleGeoDrawings.find { it is RulerDrawing } as? RulerDrawing },
            ),
            Compass(
                onToggleSelect = { toggleGeoToolSelect<CompassDrawing>(it) },
                moveBy = { moveGeoToolBy<CompassDrawing>(it) },
                rotateBy = { rotateGeoToolBy<CompassDrawing>(it) },
                changeAngleBy = { changeCompassAngleBy(it) },
                getGeoDrawing = { visibleGeoDrawings.find { it is CompassDrawing } as? CompassDrawing },

                draw = { addDrawing(it) },
                drawPlaceholder = { placeholder = it },
                getSelectedColor = ::getSelectedColor,
            ),
            Protractor(
                onToggleSelect = { toggleGeoToolSelect<ProtractorDrawing>(it) },
                moveBy = { moveGeoToolBy<ProtractorDrawing>(it) },
                rotateBy = {},
                getGeoDrawing = { visibleGeoDrawings.find { it is ProtractorDrawing } as? ProtractorDrawing }
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

    private var hoveredTool: Tool? = null

    fun onEvent(event: CanvasEvent) {

        when(event) {
            is CanvasEvent.DragStart -> {
                setHoveredToolIndex(
                    canvasSize = event.canvasSize,
                    hoverOffset = event.offset
                )
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

    private fun setHoveredToolIndex(canvasSize: Size, hoverOffset: Offset) {
        visibleGeoDrawings.reversed().forEach { geoDrawing ->
            val isHovered = geoDrawing.isHovered(
                canvasSize = canvasSize,
                hoverOffset = hoverOffset
            )

            if (isHovered) {
                hoveredTool = geoTools.find { it.isRelatedGeoDrawing(geoDrawing) }
                return
            }
        }
        hoveredTool = drawingTools.find { it.selected }
    }

    private fun toggleDrawingToolSelect(name: String) {
        val toolIndex = drawingTools.indexOfFirst { it.name == name }
        if (toolIndex == -1) return

        drawingTools = drawingTools.mapIndexed { index, t ->
            if (index == toolIndex)
                t.updateSelected(selected = !t.selected)
            else if (t is Pencil || t is LineTool)
                t.updateSelected(selected = false)
            else
                t
        }
    }

    private inline fun <reified T: GeoDrawing> toggleGeoToolSelect(name: String) {
        val mutableGeoTools = geoTools.toMutableList()
        val toolIndex = mutableGeoTools.indexOfFirst { it.name == name }
        val tool = mutableGeoTools.getOrNull(toolIndex) ?: return

        val selected = !tool.selected

        mutableGeoTools[toolIndex] = tool.updateSelected(selected = selected)
        geoTools = mutableGeoTools

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

    private inline fun <reified T: GeoDrawing> rotateGeoToolBy(angle: Float) {
        val geoDrawingIndex = visibleGeoDrawings.indexOfFirst { it is T }
        if (geoDrawingIndex == -1) return

        println(geoDrawingIndex)
        println(visibleGeoDrawings)

        visibleGeoDrawings = visibleGeoDrawings.toMutableList().also {
            it[geoDrawingIndex] = it[geoDrawingIndex].rotateBy(angle)
        }
    }

    private fun changeCompassAngleBy(angle: Float) {
        val geoDrawingIndex = visibleGeoDrawings.indexOfFirst { it is CompassDrawing }
        if (geoDrawingIndex == -1) return

        visibleGeoDrawings = visibleGeoDrawings.toMutableList().also {
            (it[geoDrawingIndex] as? CompassDrawing)?.let { compassDrawing ->
                it[geoDrawingIndex] = compassDrawing.changeAngleBy(angle)
            }
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