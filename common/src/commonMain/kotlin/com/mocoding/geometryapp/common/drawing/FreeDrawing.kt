package com.mocoding.geometryapp.common.drawing

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.mocoding.geometryapp.common.utils.calcDistance
import com.mocoding.geometryapp.common.utils.lineTo
import com.mocoding.geometryapp.common.utils.moveTo
import com.mocoding.geometryapp.common.utils.relativeLineTo
import java.util.*

class FreeDrawing(
    val points: List<Offset> = emptyList(),
    override val color: Color
): Drawing {

    fun addPoint(point: Offset): FreeDrawing {
        return FreeDrawing(
            points = points + point,
            color = color
        )
    }

    fun addPoints(newPoints: List<Offset>): FreeDrawing {
        return FreeDrawing(
            points = points + newPoints,
            color = color
        )
    }

    override fun drawOn(
        drawScope: DrawScope
    ) {
        if (points.size < 2) return

        val path = Path()

        var lastDrawnPoint: Offset? = null
        var ignoredOffset: Offset = Offset.Zero

        points.forEachIndexed { index, point ->
            if (index == 0) {
                path.moveTo(point)
                ignoredOffset = Offset.Zero
            } else {
                lastDrawnPoint?.let {
                    if (calcDistance(point + ignoredOffset, it) < 4f) {
                        ignoredOffset += point
                        return@forEachIndexed
                    }
                }
                path.relativeLineTo(point + ignoredOffset)
                ignoredOffset = Offset.Zero
            }

            lastDrawnPoint = point
        }

        val width = with(drawScope) { 6.dp.toPx() }

        drawScope.drawPath(
            path = path,
            color = color,
            style = Stroke(
                width = width,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round,
                pathEffect = PathEffect.cornerPathEffect(width)
            )
        )
    }

}