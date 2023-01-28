package com.mocoding.geometryapp.common.drawing

import androidx.compose.ui.graphics.drawscope.DrawScope

interface Drawing {

    fun drawOn(drawScope: DrawScope)

}

fun DrawScope.draw(drawing: Drawing) {
    drawing.drawOn(this)
}