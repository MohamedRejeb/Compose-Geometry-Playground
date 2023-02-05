package com.mocoding.geometryapp.common

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

expect fun getPlatformName(): String

expect fun DrawScope.drawText(
    text: String,
    color: Color,
    fontSize: Float,
    topLeft: Offset
)