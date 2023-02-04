package com.mocoding.geometryapp.common.utils

import kotlin.math.PI

fun Float.toRadian(): Float {
    return (this * PI / 180f).toFloat()
}

fun Float.toDegree(): Float {
    return (this * 180f / PI).toFloat()
}