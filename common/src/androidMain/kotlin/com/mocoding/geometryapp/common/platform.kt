package com.mocoding.geometryapp.common

import android.graphics.Paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb

actual fun getPlatformName(): String {
    return "Android"
}

actual fun DrawScope.drawText(
    text: String,
    color: Color,
    fontSize: Float,
    topLeft: Offset
) {
    drawIntoCanvas { canvas ->
        canvas.nativeCanvas.drawText(
            text,
            topLeft.x,
            topLeft.y,
            Paint().also {
                it.color = color.toArgb()
                it.textSize = fontSize
            }
        )
    }
}