package com.mocoding.geometryapp.common.tools

import com.mocoding.geometryapp.common.drawing.Drawing

interface DrawingTool<T: Drawing> {

    var placeholderDrawing: T?

    val draw: (T) -> Unit
    val drawPlaceholder: (T) -> Unit get() = {}

}