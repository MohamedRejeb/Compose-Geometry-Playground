package com.mocoding.geometryapp.common.tools.geo

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.vector.ImageVector
import com.mocoding.geometryapp.common.drawing.geo.GeoDrawing
import com.mocoding.geometryapp.common.drawing.geo.RulerDrawing
import com.mocoding.geometryapp.common.event.CanvasEvent
import com.mocoding.geometryapp.common.helper.LineEquation
import com.mocoding.geometryapp.common.utils.rotateBy
import com.mocoding.geometryapp.common.utils.toDegree
import kotlin.math.*

data class Ruler(
    override val selected: Boolean = false,
    override val onToggleSelect: (name: String) -> Unit,
    override val moveBy: (offset: Offset) -> Unit,
    override val rotateBy: (angle: Float) -> Unit,
    override val getGeoDrawing: () -> RulerDrawing?
): GeoTool {

    override val icon: ImageVector = Icons.Outlined.Straighten
    override val name: String = "Ruler"

    private var canvasSize = Size.Zero
    private var isRotate = false

    override fun onEvent(event: CanvasEvent) {
        when(event) {
            is CanvasEvent.DragStart -> {
                canvasSize = event.canvasSize
                val rulerDrawing = getGeoDrawing() ?: return
                isRotate = rulerDrawing.isRotateArea(canvasSize, event.offset)
            }
            is CanvasEvent.Drag -> {
                if (isRotate) {
                    val rulerDrawing = getGeoDrawing() ?: return
                    val size = rulerDrawing.getSize(canvasSize)
                    val topLeft = rulerDrawing.getTopLeft(canvasSize, size)
                    val rotationCenter = topLeft
                    val rotatedDragOffset = event.offset.times(100f).rotateBy(-rulerDrawing.rotation, Offset.Zero).div(100f)
                    val newTopLeftPoint = topLeft.copy(
                        x = topLeft.x + size.width,
                        y = LineEquation.from(rotationCenter, topLeft + Offset(size.width, 0f) + rotatedDragOffset).getYPointFrom(topLeft.x + size.width)
                    )

                    val sinAngle = (newTopLeftPoint.y - topLeft.y) / sqrt(size.width.pow(2) + (newTopLeftPoint.y - topLeft.y).pow(2))
                    val angleFromSin =
                        if (sinAngle >= 1f) asin(1f).toDegree()
                        else if (sinAngle <= -1f) asin(-1f).toDegree()
                        else asin(sinAngle).toDegree()

                    if (!angleFromSin.isNaN())
                        rotateBy(angleFromSin)
                } else {
                    moveBy(event.offset)
                }
            }
            else -> {}
        }
    }

    override fun updateSelected(selected: Boolean): Ruler {
        return copy(selected = selected)
    }

    override fun isRelatedGeoDrawing(geoDrawing: GeoDrawing): Boolean {
        return geoDrawing is RulerDrawing
    }

}