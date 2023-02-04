package com.mocoding.geometryapp.common.helper

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
}
