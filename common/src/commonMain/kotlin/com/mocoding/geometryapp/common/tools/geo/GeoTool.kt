package com.mocoding.geometryapp.common.tools.geo

import androidx.compose.ui.geometry.Offset
import com.mocoding.geometryapp.common.drawing.geo.GeoDrawing
import com.mocoding.geometryapp.common.tools.Tool

interface GeoTool: Tool {

    val getGeoDrawing: () -> GeoDrawing?

    val moveBy: (offset: Offset) -> Unit
    val rotateBy: (angle: Float) -> Unit

    override fun updateSelected(selected: Boolean): GeoTool

    fun isRelatedGeoDrawing(geoDrawing: GeoDrawing): Boolean

}