package com.mocoding.geometryapp.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mocoding.geometryapp.common.event.ColorsPanelEvent
import com.mocoding.geometryapp.common.event.ToolsPanelEvent
import com.mocoding.geometryapp.common.state.rememberGeometryState
import com.mocoding.geometryapp.common.ui.ColorsPanel
import com.mocoding.geometryapp.common.ui.PlaygroundCanvas
import com.mocoding.geometryapp.common.ui.ToolsPanel

val lightColors = lightColors(
    background = Color.White,
    onBackground = Color(0xFF121212)
)

val darkColors = darkColors(
    background = Color(0xFF323232),
    onBackground = Color.White
)

@Composable
fun App() {
    val platformName = getPlatformName()

    val geo = rememberGeometryState()

    val colors = if (geo.isDarkMode) darkColors else lightColors

    Box(
        modifier = Modifier.background(colors.background)
    ) {
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
            colors = colors
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

        IconToggleButton(
            checked = geo.isDarkMode,
            onCheckedChange = { geo.isDarkMode = it },
            content = {
                if (geo.isDarkMode) {
                    Icon(
                        Icons.Outlined.DarkMode,
                        contentDescription = null,
                        tint = colors.onBackground
                    )
                } else {
                    Icon(
                        Icons.Outlined.LightMode,
                        contentDescription = null,
                        tint = colors.onBackground
                    )
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
        )
    }
}
