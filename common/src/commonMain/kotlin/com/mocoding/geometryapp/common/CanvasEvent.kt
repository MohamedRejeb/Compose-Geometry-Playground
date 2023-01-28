package com.mocoding.geometryapp.common

import androidx.compose.ui.geometry.Offset

sealed class CanvasEvent {
    data class DragStart(val offset: Offset): CanvasEvent()
    data class Drag(val offset: Offset): CanvasEvent()
    object DragStop: CanvasEvent()
    object DragCancel: CanvasEvent()
}