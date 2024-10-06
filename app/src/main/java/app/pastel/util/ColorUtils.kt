package app.pastel.util

import androidx.compose.ui.graphics.Color
import kotlin.math.abs
import kotlin.random.Random

fun Color.maxSaturation(): Color {
    val (hue, _, lightness) = this.toHsl()
    // Max saturation is 1f
    return hslToColor(hue, 1f, lightness)
}

@Suppress("MagicNumber")
fun randomColor(): Color {
    val red = Random.nextInt(256)
    val green = Random.nextInt(256)
    val blue = Random.nextInt(256)
    return Color(red, green, blue)
}

@Suppress("MagicNumber")
private fun Color.toHsl(): FloatArray {
    val max = maxOf(red, green, blue)
    val min = minOf(red, green, blue)
    val delta = max - min

    val lightness = (max + min) / 2f
    val saturation = if (max == min) 0f else delta / (1f - abs(2f * lightness - 1f))

    val hue = when (max) {
        red -> (green - blue) / delta % 6f
        green -> (blue - red) / delta + 2f
        else -> (red - green) / delta + 4f
    } * 60f

    return floatArrayOf(if (hue < 0) hue + 360f else hue, saturation, lightness)
}

@Suppress("MagicNumber")
private fun hslToColor(hue: Float, saturation: Float, lightness: Float): Color {
    val chroma = (1f - abs(2f * lightness - 1f)) * saturation
    val secondComponent = chroma * (1f - abs((hue / 60f) % 2f - 1f))
    val lightnessAdjustment = lightness - chroma / 2f

    val (redComponent, greenComponent, blueComponent) = when {
        hue < 60 -> Triple(chroma, secondComponent, 0f)
        hue < 120 -> Triple(secondComponent, chroma, 0f)
        hue < 180 -> Triple(0f, chroma, secondComponent)
        hue < 240 -> Triple(0f, secondComponent, chroma)
        hue < 300 -> Triple(secondComponent, 0f, chroma)
        else -> Triple(chroma, 0f, secondComponent)
    }

    return Color(
        red = redComponent + lightnessAdjustment,
        green = greenComponent + lightnessAdjustment,
        blue = blueComponent + lightnessAdjustment
    )
}