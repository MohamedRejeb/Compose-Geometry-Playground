package com.mocoding.geometryapp.common.tools.geo

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Architecture
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.vector.ImageVector
import com.mocoding.geometryapp.common.drawing.geo.CompassDrawing
import com.mocoding.geometryapp.common.drawing.geo.GeoDrawing
import com.mocoding.geometryapp.common.event.CanvasEvent
import com.mocoding.geometryapp.common.utils.calcIsoscelesTriangleHeight
import com.mocoding.geometryapp.common.utils.calcTriangleAngle

data class Compass(
    override val selected: Boolean = false,
    override val onToggleSelect: (name: String) -> Unit,
    override val moveBy: (offset: Offset) -> Unit,
    override val rotateBy: (angle: Float) -> Unit,
    override val getGeoDrawing: () -> CompassDrawing?,
    val changeAngleBy: (angle: Float) -> Unit
): GeoTool {

    override val icon: ImageVector = Icons.Outlined.Architecture
    override val name: String = "Compass"

    private var canvasSize = Size.Zero
    private var compassEvent: Event? = null
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
                } else {
                    compassEvent = null
                }
            }
            is CanvasEvent.Drag -> {
                when(compassEvent) {
                    Event.Move -> moveBy(event.offset)
                    Event.Rotate -> rotateBy(0f)
                    Event.ChangeAngle -> {
                        val compassDrawing = getGeoDrawing() ?: return
                        val sideLength = compassDrawing.getSideLength(canvasSize)
                        val newBaseLength = compassDrawing.getBaseLength(canvasSize) + event.offset.x
                        val newHeight = calcIsoscelesTriangleHeight(
                            side = sideLength,
                            base = newBaseLength
                        )
                        val newAngle = calcTriangleAngle(
                            side1 = sideLength,
                            side2 = sideLength,
                            base = newBaseLength,
                            height = newHeight
                        )

                        if (!newAngle.isNaN() && newAngle > 10 && newAngle < 160) {
                            changeAngleBy(newAngle - compassDrawing.compassAngle)
                        }
                    }
                    else -> {}
                }
            }
            else -> {}
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