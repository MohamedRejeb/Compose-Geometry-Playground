package com.mocoding.geometryapp.common.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import com.mocoding.geometryapp.common.drawing.Drawing
import com.mocoding.geometryapp.common.drawing.draw
import com.mocoding.geometryapp.common.event.CanvasEvent

@Composable
fun PlaygroundCanvas(
    modifier: Modifier,
    drawings: List<Drawing>,
    onEvent: (CanvasEvent) -> Unit,
) {
    Canvas(
        modifier = modifier
            .pointerInput(true) {
                detectDragGestures(
                    onDragStart = { startOffset ->
                        onEvent(CanvasEvent.DragStart(
                            canvasSize = Size(size.width.toFloat(), size.height.toFloat()),
                            offset = startOffset
                        ))
                    },
                    onDragEnd = {
                        onEvent(CanvasEvent.DragStop)
                    },
                    onDragCancel = {
                        onEvent(CanvasEvent.DragCancel)
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()

                        onEvent(CanvasEvent.Drag(dragAmount))
                    }
                )
            }
    ) {
        drawings.forEach { drawing ->
            draw(
                drawing = drawing,
            )
        }
    }
}