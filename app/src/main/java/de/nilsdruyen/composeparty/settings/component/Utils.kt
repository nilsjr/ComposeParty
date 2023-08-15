package de.nilsdruyen.composeparty.settings.component

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

internal fun String.toColor(): Color = Color(android.graphics.Color.parseColor(this))

@OptIn(ExperimentalStdlibApi::class)
internal fun Color.raw(): String = "#" + this.toArgb().toHexString()