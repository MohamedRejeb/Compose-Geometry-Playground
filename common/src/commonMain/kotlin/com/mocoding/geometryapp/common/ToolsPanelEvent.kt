package com.mocoding.geometryapp.common

sealed class ToolsPanelEvent {
    object Redo: ToolsPanelEvent()
    object Undo: ToolsPanelEvent()
}
