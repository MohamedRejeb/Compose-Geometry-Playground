package com.mocoding.geometryapp.common.tools

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.mocoding.geometryapp.common.event.CanvasEvent
import com.mocoding.geometryapp.common.drawing.FreeDrawing
import com.mocoding.geometryapp.common.drawing.geo.RulerDrawing
import com.mocoding.geometryapp.common.helper.LineEquation
import com.mocoding.geometryapp.common.utils.calcOrthogonalProjectionPoint

data class Pencil(
    override val selected: Boolean = false,
    override val onToggleSelect: (name: String) -> Unit,

    override val draw: (FreeDrawing) -> Unit,
    override val drawPlaceholder: (FreeDrawing) -> Unit,
    override val getSelectedColor: () -> Color,
    private val getRulerDrawing: () -> RulerDrawing?,
): DrawingTool<FreeDrawing> {

    override val icon: ImageVector get() = Icons.Outlined.Edit

    override val name: String = "Free Drawing"

    override var placeholderDrawing: FreeDrawing? = null

    private var lineEquation: LineEquation? = null

    override fun onEvent(
        event: CanvasEvent
    ) {
        when(event) {
            is CanvasEvent.DragStart -> {
                val rulerDrawing = getRulerDrawing()
                val isInRulerDrawingArea = isInRulerDrawingArea(
                    rulerDrawing = rulerDrawing,
                    canvasSize = event.canvasSize,
                    offset = event.offset
                )

                if (isInRulerDrawingArea) {
                    lineEquation = rulerDrawing?.getLineEquation(
                        canvasSize = event.canvasSize,
                        offset = event.offset
                    )
                    lineEquation?.let {
                        FreeDrawing(
                            points = listOf(calcOrthogonalProjectionPoint(
                                line = it,
                                point = event.offset
                            )),
                            color = getSelectedColor()
                        ).let { drawing ->
                            placeholderDrawing = drawing
                            drawPlaceholder(drawing)
                        }
                    }
                } else {
                    lineEquation = null
                    FreeDrawing(
                        points = listOf(event.offset),
                        color = getSelectedColor()
                    ).let { drawing ->
                        placeholderDrawing = drawing
                        drawPlaceholder(drawing)
                    }
                }
            }
            is CanvasEvent.Drag -> {
                if (lineEquation != null) {
                    lineEquation?.let {
                        placeholderDrawing?.points?.reduce{ o1, o2 -> o1 + o2 }?.let { lastPoint ->
                            placeholderDrawing?.addPoint(calcOrthogonalProjectionPoint(
                                line = it,
                                point = lastPoint + event.offset
                            ) - lastPoint)?.let { drawing ->
                                placeholderDrawing = drawing
                                drawPlaceholder(drawing)
                            }
                        }
                    }
                } else {
                    placeholderDrawing?.addPoint(event.offset)?.let { drawing ->
                        placeholderDrawing = drawing
                        drawPlaceholder(drawing)
                    }
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

    override fun updateSelected(selected: Boolean): Pencil {
        return copy(selected = selected)
    }

    private fun isInRulerDrawingArea(
        rulerDrawing: RulerDrawing?,
        canvasSize: Size,
        offset: Offset
    ): Boolean {
        if (rulerDrawing == null) return false

        return  rulerDrawing.isInRulerArea(canvasSize, offset)
    }

}