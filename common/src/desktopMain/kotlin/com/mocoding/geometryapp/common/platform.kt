package com.mocoding.geometryapp.common

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import org.jetbrains.skia.Font

actual fun getPlatformName(): String {
    return "Desktop"
}

actual fun DrawScope.drawText(
    text: String,
    color: Color,
    fontSize: Float,
    topLeft: Offset
) {
    drawIntoCanvas { canvas ->
        canvas.nativeCanvas.drawString(
            s = text,
            x = topLeft.x,
            y = topLeft.y,
            paint = org.jetbrains.skia.Paint().also {
                it.color = color.toArgb()
            },
            font = Font().also {
                it.size = fontSize
            }
        )
    }
}