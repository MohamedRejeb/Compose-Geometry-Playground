package com.mocoding.geometryapp.common.drawing

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.mocoding.geometryapp.common.utils.lineTo
import com.mocoding.geometryapp.common.utils.moveTo
import com.mocoding.geometryapp.common.utils.relativeLineTo
import java.util.*

class Line(
    val startPoint: Offset,
    val stopPoint: Offset
): Drawing {

    override fun drawOn(drawScope: DrawScope) {
        val path = Path()
        path.moveTo(startPoint)
        path.relativeLineTo(stopPoint)

        val width = with(drawScope) { 6.dp.toPx() }

        drawScope.drawPath(
            path = path,
            color = Color.Black,
            style = Stroke(
                width = width,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round,
                pathEffect = PathEffect.cornerPathEffect(width)
            )
        )
    }

}