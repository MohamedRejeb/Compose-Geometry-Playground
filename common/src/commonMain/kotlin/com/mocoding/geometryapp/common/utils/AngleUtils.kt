package com.mocoding.geometryapp.common.utils

import kotlin.math.PI
import kotlin.math.abs

fun Float.toRadian(): Float {
    return (this * PI / 180f).toFloat()
}

fun Float.toDegree(): Float {
    return (this * 180f / PI).toFloat()
}

fun Float.shrinkAngle(): Float {
    return if (this > 180f) {
        (this % 180f) - 180f
    } else if (this < -180f) {
        (this % 180f) + 180f
    } else if (abs(this) == 180f) {
        0f
    } else {
        this
    }
}