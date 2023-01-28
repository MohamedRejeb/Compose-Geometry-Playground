package com.mocoding.geometryapp.common.tools

import androidx.compose.runtime.MutableState

interface Selectable {
    val selected: MutableState<Boolean>

    fun toggleSelect() {
        selected.value = !selected.value
    }
}