package com.mocoding.geometryapp.common.event

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size

sealed class CanvasEvent {
    data class DragStart(
        val canvasSize: Size,
        val offset: Offset
    ): CanvasEvent()
    data class Drag(val offset: Offset): CanvasEvent()
    object DragStop: CanvasEvent()
    object DragCancel: CanvasEvent()
}