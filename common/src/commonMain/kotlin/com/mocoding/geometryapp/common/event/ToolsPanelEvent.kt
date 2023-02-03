package com.mocoding.geometryapp.common.event

sealed class ToolsPanelEvent {
    object Redo: ToolsPanelEvent()
    object Undo: ToolsPanelEvent()
}
