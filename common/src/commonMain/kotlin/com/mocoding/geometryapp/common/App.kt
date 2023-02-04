package com.mocoding.geometryapp.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mocoding.geometryapp.common.event.ColorsPanelEvent
import com.mocoding.geometryapp.common.event.ToolsPanelEvent
import com.mocoding.geometryapp.common.state.rememberGeometryState
import com.mocoding.geometryapp.common.ui.ColorsPanel
import com.mocoding.geometryapp.common.ui.PlaygroundCanvas
import com.mocoding.geometryapp.common.ui.ToolsPanel

@Composable
fun App() {
    val platformName = getPlatformName()

    val geo = rememberGeometryState()

    Box {
        val drawings = (geo.drawings + geo.placeholder + geo.visibleGeoDrawings).filterNotNull()

        PlaygroundCanvas(
            modifier = Modifier.fillMaxSize(),
            drawings = drawings,
            onEvent = { event -> geo.onEvent(event) },
        )

        ToolsPanel(
            modifier = Modifier.fillMaxHeight(0.8f),
            tools = geo.drawingTools + geo.geoTools,
            onEvent = { event ->
                when(event) {
                    is ToolsPanelEvent.Undo -> {
                        geo.undo()
                    }

                    is ToolsPanelEvent.Redo -> {
                        geo.redo()
                    }
                }
            },
            isUndoEnabled = geo.drawings.isNotEmpty(),
            isRedoEnabled = geo.drawingsTrash.isNotEmpty(),
        )

        ColorsPanel(
            modifier = Modifier
                .fillMaxHeight(0.8f)
                .align(Alignment.TopEnd)
            ,
            colors = geo.colors,
            selectedColorIndex = geo.selectedColorIndex,
            onEvent = { event ->
                when(event) {
                    is ColorsPanelEvent.OnColorSelected -> {
                        geo.selectColor(index = event.colorIndex)
                    }
                }
            },
        )
    }
}
