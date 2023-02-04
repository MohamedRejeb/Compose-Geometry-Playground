package com.mocoding.geometryapp.common.utils

import androidx.compose.ui.geometry.Offset
import kotlin.math.cos
import kotlin.math.sin

fun Offset.rotateBy(angle: Float, center: Offset): Offset {
    val sin = sin(angle.toRadian())
    val cos = cos(angle.toRadian())

    val translatePoint = this - center

    val newPoint = Offset(
        x = translatePoint.x * cos - translatePoint.y * sin,
        y = translatePoint.x * sin + translatePoint.y * cos
    )

    return center + newPoint
}