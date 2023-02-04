package com.mocoding.geometryapp.common.drawing.geo

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.mocoding.geometryapp.common.utils.*

data class CompassDrawing(
    override val position: Offset = Offset.Zero,
    override val rotation: Float = 80f,
    val compassAngle: Float = 30f
): GeoDrawing {

    override val color: Color = Color.Black
    override fun drawOn(drawScope: DrawScope) {
        val size = getSize(canvasSize = drawScope.size)

        val topLeft = getTopLeft(
            canvasSize = drawScope.size,
            toolSize = size
        )

        val rotationCenter = getRotationCenter(size, topLeft)

        val radius = with(drawScope) { 12.dp.toPx() }
        val cornerRadius = with(drawScope) { 6.dp.toPx() }

        drawScope.drawCircle(
            color = Color.Blue.copy(alpha = 0.4f),
            radius = radius,
            center = Offset(topLeft.x + size.width / 2f, topLeft.y + radius).rotateBy(
                angle = rotation,
                center = rotationCenter
            ),
            style = Stroke(
                width = cornerRadius
            )
        )

        val stroke = with(drawScope) { 8.dp.toPx() }

        val path = Path()
        path.moveTo(Offset(topLeft.x + stroke / 2f, topLeft.y + size.height - stroke / 2f).rotateBy(
            angle = rotation,
            center = rotationCenter
        ))
        path.lineTo(Offset(topLeft.x + size.width / 2f, topLeft.y + radius).rotateBy(
            angle = rotation,
            center = rotationCenter
        ))
        path.lineTo(Offset(topLeft.x + size.width - stroke / 2f, topLeft.y + size.height - stroke / 2f).rotateBy(
            angle = rotation,
            center = rotationCenter
        ))

        drawScope.drawPath(
            path = path,
            color = Color.Blue.copy(alpha = 0.4f),
            style = Stroke(
                width = stroke,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round,
            )
        )
    }

    override fun moveBy(offset: Offset): CompassDrawing {
        return copy(position = this.position + offset)
    }

    override fun rotateBy(rotation: Float): GeoDrawing {
        return copy(rotation = this.rotation + rotation)
    }

    fun changeAngleBy(angle: Float): GeoDrawing {
        return copy(compassAngle = this.compassAngle + angle)
    }

    override fun isHovered(canvasSize: Size, hoverOffset: Offset): Boolean {
        val size = getSize(canvasSize = canvasSize)

        val topLeft = getTopLeft(
            canvasSize = canvasSize,
            toolSize = size
        )

        val rotationCenter = getRotationCenter(size, topLeft)

        println("hoverOffset $hoverOffset")
        println("hoverOffset rotated ${hoverOffset.rotateBy(rotation, rotationCenter)}")

        if (hoverOffset.rotateBy(rotation, rotationCenter).x < topLeft.x
            || hoverOffset.rotateBy(rotation, rotationCenter).y < topLeft.y) return false

        if (hoverOffset.rotateBy(rotation, rotationCenter).x > topLeft.x + size.width
            || hoverOffset.rotateBy(rotation, rotationCenter).y > topLeft.y + size.height) return false

        return true
    }

    fun isRotateArea(canvasSize: Size, hoverOffset: Offset): Boolean {
        val size = getSize(canvasSize = canvasSize)

        val topLeft = getTopLeft(
            canvasSize = canvasSize,
            toolSize = size
        )

        val rotationCenter = getRotationCenter(size, topLeft)

        if (hoverOffset.rotateBy(rotation, rotationCenter).y < (topLeft.y + size.height/3f)) return false

        return true
    }

    fun isCompassRightArea(canvasSize: Size, hoverOffset: Offset): Boolean {
        val size = getSize(canvasSize = canvasSize)

        val topLeft = getTopLeft(
            canvasSize = canvasSize,
            toolSize = size
        )

        val rotationCenter = getRotationCenter(size, topLeft)

        if (hoverOffset.rotateBy(rotation, rotationCenter).x < topLeft.x + size.width/2f
            || hoverOffset.rotateBy(rotation, rotationCenter).y < topLeft.y + size.height/2f) return false

        return true
    }

    fun isMoveArea(canvasSize: Size, hoverOffset: Offset): Boolean {
        return !isCompassRightArea(canvasSize, hoverOffset)
                && !isRotateArea(canvasSize, hoverOffset)
    }

    fun getSideLength(canvasSize: Size): Float {
        return canvasSize.height / 3f
    }

    fun getBaseLength(canvasSize: Size): Float {
        val sideLength = getSideLength(canvasSize)

        return calcTriangleThirdSide(
            s1 = sideLength,
            a1 = (180f - compassAngle) / 2f,
            a2 = compassAngle
        )
    }

    fun getRotationCenter(size: Size, topLeft: Offset): Offset {
        return topLeft + Offset(0f, size.height)
    }

    fun getRotationCenter(canvasSize: Size): Offset {
        val size = getSize(canvasSize = canvasSize)

        val topLeft = getTopLeft(
            canvasSize = canvasSize,
            toolSize = size
        )

        return getRotationCenter(size, topLeft)
    }

    private fun getSize(canvasSize: Size): Size {
        val sideLength = getSideLength(canvasSize)
        val base = getBaseLength(canvasSize)
        val height = calcTriangleHeight(
            side1 = sideLength,
            side2 = sideLength,
            base = base,
            angle = compassAngle
        )

        return Size(base, height)
    }

    private fun getTopLeft(canvasSize: Size, toolSize: Size): Offset {
        val x = (canvasSize.width - toolSize.width) / 2f
        val y = (canvasSize.height - toolSize.height) / 2f

        return Offset(x, y) + position
    }

}