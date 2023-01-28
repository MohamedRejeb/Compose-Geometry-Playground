package com.mocoding.geometryapp.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.mocoding.geometryapp.common.drawing.Drawing
import com.mocoding.geometryapp.common.drawing.draw

@Composable
fun PlaygroundCanvas(
    modifier: Modifier,
    drawings: List<Drawing>,
    onEvent: (CanvasEvent) -> Unit
) {
    Canvas(
        modifier = modifier
            .pointerInput(true) {
                detectDragGestures(
                    onDragStart = { startOffset ->
                        onEvent(CanvasEvent.DragStart(startOffset))
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
            draw(drawing)
        }
    }
}