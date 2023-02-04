package com.mocoding.geometryapp.common.drawing.geo

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.mocoding.geometryapp.common.utils.calcTriangleHeight
import com.mocoding.geometryapp.common.utils.calcTriangleThirdSide

data class CompassDrawing(
    override val position: Offset = Offset.Zero,
    override val rotation: Float = 0f,
    val compassAngle: Float = 30f
): GeoDrawing {

    override val color: Color = Color.Black
    override fun drawOn(drawScope: DrawScope) {
        val size = getSize(canvasSize = drawScope.size)

        val topLeft = getTopLeft(
            canvasSize = drawScope.size,
            toolSize = size
        )

        val radius = with(drawScope) { 12.dp.toPx() }
        val cornerRadius = with(drawScope) { 6.dp.toPx() }

        drawScope.drawCircle(
            color = Color.Blue.copy(alpha = 0.4f),
            radius = radius,
            center = Offset(topLeft.x + size.width / 2f, topLeft.y + radius),
            style = Stroke(
                width = cornerRadius
            )
        )

        val stroke = with(drawScope) { 8.dp.toPx() }

        val path = Path()
        path.moveTo(topLeft.x + stroke / 2f, topLeft.y + size.height - stroke / 2f)
        path.lineTo(topLeft.x + size.width / 2f, topLeft.y + radius)
        path.lineTo(topLeft.x + size.width - stroke / 2f, topLeft.y + size.height - stroke / 2f)

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

        if (hoverOffset.x < topLeft.x
            || hoverOffset.y < topLeft.y) return false

        if (hoverOffset.x > topLeft.x + size.width
            || hoverOffset.y > topLeft.y + size.height) return false

        return true
    }

    fun isRotateArea(canvasSize: Size, hoverOffset: Offset) {

    }

    fun isCompassRightArea(canvasSize: Size, hoverOffset: Offset): Boolean {
        val size = getSize(canvasSize = canvasSize)

        val topLeft = getTopLeft(
            canvasSize = canvasSize,
            toolSize = size
        )

        if (hoverOffset.x < topLeft.x + size.width/2f
            || hoverOffset.y < topLeft.y + size.height/2f) return false

        return true
    }

    fun isMoveArea(canvasSize: Size, hoverOffset: Offset): Boolean {
        return !isCompassRightArea(canvasSize, hoverOffset)
    }

    fun getSideLength(canvasSize: Size): Float {
        return canvasSize.height / 3f
    }

    fun getBaseLength(canvasSize: Size): Float {
        val sideLength = getSideLength(canvasSize)

        println("compassAngle $compassAngle")

        return calcTriangleThirdSide(
            s1 = sideLength,
            a1 = (180f - compassAngle) / 2f,
            a2 = compassAngle
        )
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

        println(base)
        println(height)

        return Size(base, height)
    }

    private fun getTopLeft(canvasSize: Size, toolSize: Size): Offset {
        val x = (canvasSize.width - toolSize.width) / 2f
        val y = (canvasSize.height - toolSize.height) / 2f

        return Offset(x, y) + position
    }

}