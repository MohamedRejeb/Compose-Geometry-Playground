package com.mocoding.geometryapp.common.tools

interface Selectable<T> {
    val selected: Boolean
    val onToggleSelect: (T) -> Unit
    fun updateSelected(selected: Boolean): T
}