package com.mocoding.geometryapp.common.utils

import androidx.compose.ui.geometry.Offset
import kotlin.math.pow
import kotlin.math.sqrt

fun calcDistance(p1: Offset, p2: Offset): Float {
    return sqrt((p1.x - p2.x).pow(2) + (p1.y - p2.y).pow(2))
}