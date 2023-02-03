package com.mocoding.geometryapp.common.tools

import androidx.compose.ui.graphics.Color
import com.mocoding.geometryapp.common.drawing.Drawing

interface DrawingTool<T: Drawing> {

    var placeholderDrawing: T?

    val draw: (T) -> Unit
    val drawPlaceholder: (T) -> Unit get() = {}
    val getSelectedColor: () -> Color

}