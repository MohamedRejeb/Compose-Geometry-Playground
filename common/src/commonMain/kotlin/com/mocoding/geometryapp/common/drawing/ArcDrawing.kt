package com.mocoding.geometryapp.common.drawing

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

data class ArcDrawing(
    private val centerPoint: Offset,
    private val radius: Float,
    override val color: Color,
    val startAngle: Float,
    val endAngle: Float
): Drawing {

    override fun drawOn(
        drawScope: DrawScope,
    ) {
        drawScope.drawCircle(
            color = Color.Black,
            radius = with(drawScope) { 6.dp.toPx() },
            center = centerPoint,
            style = Fill
        )

        drawScope.drawArc(
            color = color,
            startAngle = startAngle,
            sweepAngle = endAngle - startAngle,
            useCenter = false,
            topLeft = centerPoint + Offset(-radius, -radius),
            size = Size(radius * 2f, radius * 2f),
            style = Stroke(
                width = with(drawScope) { 6.dp.toPx() }
            )
        )
    }

    fun addAngle(angle: Float): ArcDrawing {
        println(angle)
        println(startAngle)
        println(endAngle)
        return  if ((startAngle in angle..endAngle)
            || (startAngle in endAngle..angle))
            copy(startAngle = angle)
        else if ((endAngle in startAngle..angle)
            || (endAngle in angle..startAngle))
            copy(endAngle = angle)
        else this
    }

}