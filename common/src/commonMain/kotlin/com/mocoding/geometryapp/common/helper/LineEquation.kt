package com.mocoding.geometryapp.common.helper

import androidx.compose.ui.geometry.Offset

data class LineEquation(
    val slope: Float,
    val intercept: Float
) {
    fun getYPointFrom(x: Float): Float {
        return x * slope + intercept
    }

    fun getXPointFrom(y: Float): Float {
        return (y - intercept) / slope
    }

    companion object {
        fun from(point1: Offset, point2: Offset): LineEquation {
            val xDiff = point2.x - point1.x
            val yDiff = point2.y - point1.y
            val slope =
                if (xDiff == 0f) Float.POSITIVE_INFINITY
                else if (yDiff == 0f) 0f
                else yDiff / xDiff

            val intercept = point1.y - slope * point1.x

            return LineEquation(
                slope = slope,
                intercept = intercept
            )
        }
    }
}
