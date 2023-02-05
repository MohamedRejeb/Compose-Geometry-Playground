package com.mocoding.geometryapp.common.drawing.geo

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.mocoding.geometryapp.common.drawing.Drawing

interface GeoDrawing: Drawing {

    val position: Offset
    val rotation: Float

    fun moveBy(offset: Offset): GeoDrawing
    fun rotateBy(angle: Float): GeoDrawing

    fun isHovered(
        canvasSize: Size,
        hoverOffset: Offset
    ): Boolean

}