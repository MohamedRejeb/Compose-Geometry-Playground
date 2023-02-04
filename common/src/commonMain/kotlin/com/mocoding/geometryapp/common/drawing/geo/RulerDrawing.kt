package com.mocoding.geometryapp.common.drawing.geo

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.unit.dp
import com.mocoding.geometryapp.common.helper.LineEquation
import kotlin.math.abs

data class RulerDrawing(
    override val position: Offset = Offset.Zero,
    override val rotation: Float = 0f
): GeoDrawing {

    override val color: Color = Color.Black
    override fun drawOn(drawScope: DrawScope) {
        val rulerSize = getSize(canvasSize = drawScope.size)

        val rulerTopLeft = getTopLeft(
            canvasSize = drawScope.size,
            toolSize = rulerSize
        )

        val cornerRadius = with(drawScope) { 6.dp.toPx() }

        drawScope.drawRoundRect(
            color = Color.Blue.copy(alpha = 0.4f),
            size = rulerSize,
            topLeft = rulerTopLeft,
            cornerRadius = CornerRadius(cornerRadius, cornerRadius),
            style = Fill
        )
    }

    override fun moveBy(offset: Offset): RulerDrawing {
        return copy(position = position + offset)
    }

    override fun rotateBy(rotation: Float): GeoDrawing {
        TODO("Not yet implemented")
    }

    override fun isHovered(canvasSize: Size, hoverOffset: Offset): Boolean {
        val rulerSize = getSize(canvasSize = canvasSize)

        val rulerTopLeft = getTopLeft(
            canvasSize = canvasSize,
            toolSize = rulerSize
        )

        if (hoverOffset.x < rulerTopLeft.x
            || hoverOffset.y < rulerTopLeft.y) return false

        if (hoverOffset.x > rulerTopLeft.x + rulerSize.width
            || hoverOffset.y > rulerTopLeft.y + rulerSize.height) return false

        return true
    }

    private fun getSize(canvasSize: Size): Size {
        val width = canvasSize.width / 2f
        val height = width / 6f

        return Size(width, height)
    }

    private fun getTopLeft(canvasSize: Size, toolSize: Size): Offset {
        val x = (canvasSize.width - toolSize.width) / 2f
        val y = (canvasSize.height - toolSize.height) / 2f

        return Offset(x, y) + position
    }

    fun isInRulerArea(
        canvasSize: Size,
        offset: Offset
    ): Boolean {
        val size = getSize(canvasSize)
        val topLeft = getTopLeft(canvasSize, size)

        if (topLeft.x - offset.x > 40
            || offset.x - (topLeft.x + size.width) > 40) return false

        if (topLeft.y - offset.y > 40
            || offset.y - (topLeft.y + size.height) > 40) return false

        return true
    }

    fun getLineEquation(
        canvasSize: Size,
        offset: Offset
    ): LineEquation {
        val size = getSize(canvasSize)
        val topLeft = getTopLeft(canvasSize, size)

        val intercept =
            if (abs(topLeft.y - offset.y) < abs(offset.y - (topLeft.y + size.height)))
                topLeft.y
            else
                topLeft.y + size.height

        return LineEquation(
            slope = 0f,
            intercept = intercept
        )
    }

}