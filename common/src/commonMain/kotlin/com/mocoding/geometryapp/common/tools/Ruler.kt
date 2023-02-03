package com.mocoding.geometryapp.common.tools

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Rule
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import com.mocoding.geometryapp.common.drawing.RulerDrawing
import com.mocoding.geometryapp.common.event.CanvasEvent

data class Ruler(
    override val selected: Boolean = false,
    override val onToggleSelect: (Tool) -> Unit,
    override val moveBy: (offset: Offset) -> Unit
): GeoTool<RulerDrawing> {

    override val icon: ImageVector = Icons.Outlined.Straighten
    override val name: String = "Ruler"

    override fun onSelect() {
        onToggleSelect(this)
    }

    override fun onEvent(event: CanvasEvent) {
        when(event) {
            is CanvasEvent.Drag -> moveBy(event.offset)
            else -> {}
        }
    }

    override fun updateSelected(selected: Boolean): Tool {
        return copy(selected = selected)
    }

}