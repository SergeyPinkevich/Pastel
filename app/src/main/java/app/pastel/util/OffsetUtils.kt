package app.pastel.util

import androidx.compose.ui.geometry.Offset
import kotlin.math.sqrt

fun Offset.distance(other: Offset): Float {
    val dx = this.x - other.x
    val dy = this.y - other.y
    return sqrt(dx * dx + dy * dy)
}