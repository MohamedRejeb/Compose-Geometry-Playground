package com.mocoding.geometryapp.common.tools.geo

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Architecture
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.mocoding.geometryapp.common.drawing.ArcDrawing
import com.mocoding.geometryapp.common.drawing.geo.CompassDrawing
import com.mocoding.geometryapp.common.drawing.geo.GeoDrawing
import com.mocoding.geometryapp.common.event.CanvasEvent
import com.mocoding.geometryapp.common.helper.LineEquation
import com.mocoding.geometryapp.common.tools.DrawingTool
import com.mocoding.geometryapp.common.utils.calcTriangleAngle
import com.mocoding.geometryapp.common.utils.rotateBy
import com.mocoding.geometryapp.common.utils.toDegree
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.pow
import kotlin.math.sqrt

data class Compass(
    override val selected: Boolean = false,
    override val onToggleSelect: (name: String) -> Unit,
    override val moveBy: (offset: Offset) -> Unit,
    override val rotateBy: (angle: Float) -> Unit,
    override val getGeoDrawing: () -> CompassDrawing?,

    override val draw: (ArcDrawing) -> Unit,
    override var drawPlaceholder: (ArcDrawing) -> Unit,
    override val getSelectedColor: () -> Color,

    val changeAngleBy: (angle: Float) -> Unit
): GeoTool, DrawingTool<ArcDrawing> {

    override val icon: ImageVector = Icons.Outlined.Architecture
    override val name: String = "Compass"

    private var canvasSize = Size.Zero
    private var compassEvent: Event? = null

    override var placeholderDrawing: ArcDrawing? = null

    override fun onEvent(event: CanvasEvent) {
        when(event) {
            is CanvasEvent.DragStart-> {
                canvasSize = event.canvasSize
                val compassDrawing = getGeoDrawing() ?: return
                if (
                    compassDrawing.isMoveArea(
                        canvasSize = canvasSize,
                        hoverOffset = event.offset
                    )
                ) {
                    compassEvent = Event.Move
                } else if (
                    compassDrawing.isCompassRightArea(
                        canvasSize = canvasSize,
                        hoverOffset = event.offset
                    )
                ) {
                    compassEvent = Event.ChangeAngle
                } else if (
                    compassDrawing.isRotateArea(
                        canvasSize = canvasSize,
                        hoverOffset = event.offset
                    )
                ) {
                    compassEvent = Event.Rotate
                    placeholderDrawing = ArcDrawing(
                        centerPoint = compassDrawing.getRotationCenter(canvasSize),
                        radius = compassDrawing.getBaseLength(canvasSize),
                        color = getSelectedColor(),
                        startAngle = compassDrawing.rotation,
                        endAngle = compassDrawing.rotation
                    )
                } else {
                    compassEvent = null
                }
            }
            is CanvasEvent.Drag -> {
                println(compassEvent)
                when(compassEvent) {
                    Event.Move -> moveBy(event.offset)
                    Event.Rotate -> {
                        val compassDrawing = getGeoDrawing() ?: return
                        val size = compassDrawing.getSize(canvasSize)
                        val rotationCenter = compassDrawing.getRotationCenter(canvasSize)
                        val topLeft = compassDrawing.getTopLeft(canvasSize, size)
                        val rotatedDragOffset = event.offset.times(100f).rotateBy(-compassDrawing.rotation, Offset.Zero).div(100f)
                        val newTopLeftPoint = topLeft.copy(
                            x = LineEquation.from(rotationCenter, topLeft + rotatedDragOffset).getXPointFrom(topLeft.y),
                            y = topLeft.y
                        )
                        val cosAngle = size.height / sqrt(size.height.pow(2) + (newTopLeftPoint.x - topLeft.x).pow(2))
                        val calculatedAngle =
                            if (cosAngle >= 1f) acos(1f).toDegree()
                            else if (cosAngle <= -1f) acos(-1f).toDegree()
                            else acos(cosAngle).toDegree()

                        val sinAngle = (newTopLeftPoint.x - topLeft.x) / sqrt(size.height.pow(2) + (newTopLeftPoint.x - topLeft.x).pow(2))
                        val angleFromSin =
                            if (sinAngle >= 1f) asin(1f).toDegree()
                            else if (sinAngle <= -1f) asin(-1f).toDegree()
                            else asin(sinAngle).toDegree()

                        val angle =
                            if (calculatedAngle.isNaN() || calculatedAngle < 2f) 2f
                            else calculatedAngle

                        if (!angleFromSin.isNaN()) {
                            rotateBy(angleFromSin)
                            placeholderDrawing = placeholderDrawing?.addAngle(compassDrawing.rotation + angleFromSin)

                            placeholderDrawing?.let(drawPlaceholder)
                        }

                    }
                    Event.ChangeAngle -> {
                        val compassDrawing = getGeoDrawing() ?: return
                        val sideLength = compassDrawing.getSideLength(canvasSize)
                        val rotationCenter = compassDrawing.getRotationCenter(canvasSize)
                        val rotatedDragX = event.offset.times(100f).rotateBy(-compassDrawing.rotation, rotationCenter).div(100f).x
                        val newBaseLength = compassDrawing.getBaseLength(canvasSize) + rotatedDragX
                        val newAngle = calcTriangleAngle(
                            side1 = sideLength,
                            side2 = sideLength,
                            base = newBaseLength,
                        )

                        if (!newAngle.isNaN() && newAngle > 10 && newAngle < 160) {
                            changeAngleBy(newAngle - compassDrawing.compassAngle)
                        }
                    }
                    else -> {}
                }
            }
            is CanvasEvent.DragStop, CanvasEvent.DragCancel -> {
                if (compassEvent == Event.Rotate) {
                    placeholderDrawing?.let(draw)
                    placeholderDrawing = null
                }
            }
        }
    }

    override fun updateSelected(selected: Boolean): Compass {
        return copy(selected = selected)
    }

    override fun isRelatedGeoDrawing(geoDrawing: GeoDrawing): Boolean {
        return geoDrawing is CompassDrawing
    }

    enum class Event { Move, Rotate, ChangeAngle }

}