package com.mocoding.geometryapp.common.drawing

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

interface Drawing {

    val color: Color

    fun drawOn(
        drawScope: DrawScope
    )

}

fun DrawScope.draw(
    drawing: Drawing
) {
    drawing.drawOn(
        drawScope = this
    )
}