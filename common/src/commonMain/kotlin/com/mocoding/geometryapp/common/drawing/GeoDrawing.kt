package com.mocoding.geometryapp.common.drawing

import androidx.compose.ui.geometry.Offset

interface GeoDrawing: Drawing {

    val position: Offset

    fun moveBy(offset: Offset): GeoDrawing

}