package com.mocoding.geometryapp.common.tools.geo

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import com.mocoding.geometryapp.common.drawing.geo.GeoDrawing
import com.mocoding.geometryapp.common.drawing.geo.RulerDrawing
import com.mocoding.geometryapp.common.event.CanvasEvent

data class Ruler(
    override val selected: Boolean = false,
    override val onToggleSelect: (name: String) -> Unit,
    override val moveBy: (offset: Offset) -> Unit,
    override val rotateBy: (angle: Float) -> Unit,
    override val getGeoDrawing: () -> RulerDrawing?
): GeoTool {

    override val icon: ImageVector = Icons.Outlined.Straighten
    override val name: String = "Ruler"

    override fun onEvent(event: CanvasEvent) {
        when(event) {
            is CanvasEvent.Drag -> moveBy(event.offset)
            else -> {}
        }
    }

    override fun updateSelected(selected: Boolean): Ruler {
        return copy(selected = selected)
    }

    override fun isRelatedGeoDrawing(geoDrawing: GeoDrawing): Boolean {
        return geoDrawing is RulerDrawing
    }

}