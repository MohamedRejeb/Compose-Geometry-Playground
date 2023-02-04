package com.mocoding.geometryapp.common.utils

import kotlin.math.*

fun calcTriangleArea(side1: Float, side2: Float, angle: Float): Float {
    return abs(1/2f * side1 * side2 * sin(angle.toRadian()))
}

fun calcTriangleHeight(area: Float, base: Float): Float {
    return area * 2 / base
}

fun calcTriangleHeight(side1: Float, side2: Float, base: Float, angle: Float): Float {
    return calcTriangleHeight(
        area = calcTriangleArea(side1, side2, angle),
        base = base
    )
}

fun calcTriangleAngle(side1: Float, side2: Float, base: Float, height: Float): Float {
    val area = base * height / 2f
    val sinAngle = area * 2f / side1 / side2
    val cosAngle = (side1.pow(2) + side2.pow(2) - base.pow(2)) / (2 * side1 * side2)

    val angleFromSin =
        if (sinAngle >= 1f) asin(1f).toDegree()
        else if (sinAngle <= -1f) asin(-1f).toDegree()
        else asin(sinAngle).toDegree()

    val angleFromCos =
        if (cosAngle >= 1f) acos(1f).toDegree()
        else if (cosAngle <= -1f) acos(-1f).toDegree()
        else acos(cosAngle).toDegree()

    return angleFromCos
}

fun calcTriangleThirdSide(s1: Float, a1: Float, a2: Float): Float {
    val ratio1 = s1 / sin(a1.toRadian())
    return abs(ratio1 * sin(a2.toRadian()))
}

fun calcIsoscelesTriangleHeight(side: Float, base: Float): Float {
    return sqrt(abs(side.pow(2) - (base / 2f).pow(2)))
}