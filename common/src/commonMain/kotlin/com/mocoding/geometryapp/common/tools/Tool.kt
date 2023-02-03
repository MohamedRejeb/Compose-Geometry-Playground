package com.mocoding.geometryapp.common.tools

import androidx.compose.ui.graphics.vector.ImageVector
import com.mocoding.geometryapp.common.event.CanvasEvent

interface Tool {

    val icon: ImageVector
    val name: String

    fun onSelect()
    fun onEvent(event: CanvasEvent)

    val selected: Boolean
    val onToggleSelect: (Tool) -> Unit
    fun updateSelected(selected: Boolean): Tool

}