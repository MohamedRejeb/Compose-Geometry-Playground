package com.mocoding.geometryapp.common.event

sealed class ColorsPanelEvent {
    data class OnColorSelected(
        val colorIndex: Int
    ): ColorsPanelEvent()
}
