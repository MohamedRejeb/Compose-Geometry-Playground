package com.mocoding.geometryapp.common.tools

import androidx.compose.ui.geometry.Offset
import com.mocoding.geometryapp.common.drawing.GeoDrawing

interface GeoTool<T: GeoDrawing>: Tool {

    val moveBy: (offset: Offset) -> Unit

}