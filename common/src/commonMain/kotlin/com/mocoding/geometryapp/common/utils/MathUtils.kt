package com.mocoding.geometryapp.common.utils

import androidx.compose.ui.geometry.Offset
import com.mocoding.geometryapp.common.helper.LineEquation
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * Calculate Line Slope fromIndex Two Points From The Line:
 *  => Point.y = Point.x * lineSlope + lineIntercept
 */
fun calcLineSlopeFromTwoPoints(point1: Offset, point2: Offset): Float = (point2.y - point1.y) / (point2.x - point1.x)

/**
 * Calculate Line Intercept fromIndex Line Slope and Point From The Line:
 *  => Point.y = Point.x * lineSlope + lineIntercept
 */
fun calcLineIntercept(linePoint: Offset, lineSlope: Float): Float = (linePoint.y - (lineSlope * linePoint.x))

/**
 * Calculate Distance Between Two Point in 2D
 */
fun calcDistanceBetweenTwoPoint(point1: Offset, point2: Offset): Float {
    val xDiff = point1.x - point2.x
    val yDiff = point1.y - point2.y
    return sqrt((xDiff * xDiff + yDiff * yDiff))
}

/**
 * Calculate Orthogonal projection Point between one Line and one Point
 */
fun calcOrthogonalProjectionPoint(line: LineEquation, point: Offset): Offset {
    return Offset(
        x = point.x,
        y = line.intercept
    )
    // if the line is horizontal
    if (line.slope == 0f) return Offset(point.x, line.getYPointFrom(0f))
    // if the line is vertical
    else if (abs(line.slope) == Float.POSITIVE_INFINITY) return Offset(0f, point.y)

    // equation Of The Perpendicular Line : yD = aD.x + bD
    val perpendicularSlope = -(1f / line.slope)
    val perpendicularIntercept = calcLineIntercept(linePoint = point, lineSlope = perpendicularSlope)

    // coordinates Of The Low Intersection Point : intersectionX, intersectionY
    val intersectionX = ((perpendicularIntercept - line.intercept) / (line.slope - perpendicularSlope))
    val intersectionY = (intersectionX * line.slope + line.intercept)

    return Offset(intersectionX, intersectionY)
}
