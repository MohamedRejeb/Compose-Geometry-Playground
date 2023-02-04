package com.mocoding.geometryapp.common.drawing.geo

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill

data class ProtractorDrawing(
    override val position: Offset = Offset.Zero,
    override val rotation: Float = 0f
): GeoDrawing {

    override val color: Color = Color.Black
    override fun drawOn(drawScope: DrawScope) {
        val size = getSize(canvasSize = drawScope.size)

        val topLeft = getTopLeft(
            canvasSize = drawScope.size,
            toolSize = size
        )

//        drawScope.drawCircle(
//            color = Color.Blue.copy(0.4f),
//            radius = size.width / 2f,
//            center = Offset(topLeft.x + size.width / 2f, topLeft.y + size.height / 2f),
//            style = Fill
//        )

        drawScope.drawArc(
            color = Color.Blue.copy(0.4f),
            -180f,
            180f,
            useCenter = false,
            topLeft = topLeft,
            size = Size(size.width, size.height * 2f),
            style = Fill
        )
    }

    override fun moveBy(offset: Offset): ProtractorDrawing {
        return copy(position = position + offset)
    }

    override fun rotateBy(angle: Float): GeoDrawing {
        return copy(rotation = this.rotation + angle)
    }

    override fun isHovered(canvasSize: Size, hoverOffset: Offset): Boolean {
        val size = getSize(canvasSize)

        val topLeft = getTopLeft(canvasSize, size)

        if (hoverOffset.x < topLeft.x
            || hoverOffset.y < topLeft.y) return false

        if (hoverOffset.x > topLeft.x + size.width
            || hoverOffset.y > topLeft.y + size.height) return false

        return true
    }

    private fun getSize(canvasSize: Size): Size {
        return Size(canvasSize.width / 3f, canvasSize.width / 6f)
    }

    private fun getTopLeft(canvasSize: Size, toolSize: Size): Offset {
        val x = (canvasSize.width - toolSize.width) / 2f
        val y = (canvasSize.height - toolSize.height) / 2f

        return Offset(x, y) + position
    }

}