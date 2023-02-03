package com.mocoding.geometryapp.common.drawing

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

data class RulerDrawing(
    override val position: Offset = Offset.Zero
): GeoDrawing {

    override val color: Color = Color.Black
    override fun drawOn(drawScope: DrawScope) {
        val rulerWidth = drawScope.size.width / 2f
        val rulerHeight = rulerWidth / 6f

        val rulerX = (drawScope.size.width - rulerWidth) / 2f
        val rulerY = (drawScope.size.height - rulerHeight) / 2f

        val cornerRadius = with(drawScope) { 6.dp.toPx() }

        drawScope.drawRoundRect(
            color = Color.Blue.copy(alpha = 0.4f),
            size = Size(rulerWidth, rulerHeight),
            topLeft = Offset(rulerX, rulerY) + position,
            cornerRadius = CornerRadius(cornerRadius, cornerRadius),
            style = Fill
        )
    }

    override fun moveBy(offset: Offset): RulerDrawing {
        return copy(position = position + offset)
    }

}