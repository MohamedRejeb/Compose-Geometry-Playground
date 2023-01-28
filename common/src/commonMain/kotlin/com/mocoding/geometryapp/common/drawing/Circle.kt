package com.mocoding.geometryapp.common.drawing

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import java.util.UUID

class Circle(
    private val centerPoint: Offset,
    private val radius: Float
): Drawing {

    override fun drawOn(drawScope: DrawScope) {
        drawScope.drawCircle(
            color = Color.Black,
            radius = with(drawScope) { 6.dp.toPx() },
            center = centerPoint,
            style = Fill
        )

        drawScope.drawCircle(
            color = Color.Black,
            radius = radius,
            center = centerPoint,
            style = Stroke(
                width = with(drawScope) { 6.dp.toPx() }
            )
        )
    }

}