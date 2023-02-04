package com.mocoding.geometryapp.common.tools.geo

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Looks
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import com.mocoding.geometryapp.common.drawing.geo.CompassDrawing
import com.mocoding.geometryapp.common.drawing.geo.GeoDrawing
import com.mocoding.geometryapp.common.drawing.geo.ProtractorDrawing
import com.mocoding.geometryapp.common.drawing.geo.RulerDrawing
import com.mocoding.geometryapp.common.event.CanvasEvent
import com.mocoding.geometryapp.common.tools.Tool

data class Protractor(
    override val selected: Boolean = false,
    override val onToggleSelect: (name: String) -> Unit,
    override val moveBy: (offset: Offset) -> Unit,
    override val rotateBy: (angle: Float) -> Unit,
    override val getGeoDrawing: () -> ProtractorDrawing?
): GeoTool {

    override val icon: ImageVector = Icons.Outlined.Looks
    override val name: String = "Protractor"

    override fun onEvent(event: CanvasEvent) {
        when(event) {
            is CanvasEvent.Drag -> moveBy(event.offset)
            else -> {}
        }
    }

    override fun updateSelected(selected: Boolean): Protractor {
        return copy(selected = selected)
    }

    override fun isRelatedGeoDrawing(geoDrawing: GeoDrawing): Boolean {
        return geoDrawing is ProtractorDrawing
    }

}