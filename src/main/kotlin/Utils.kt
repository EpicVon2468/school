@file:Suppress("EXTENSION_SHADOWED_BY_MEMBER", "NOTHING_TO_INLINE", "FunctionName")
package io.github.epicvon2468.school

import java.awt.Graphics
import java.awt.Dimension
import java.awt.RenderingHints
import java.awt.Color as Colour

import javax.swing.JFrame
import javax.swing.LookAndFeel
import javax.swing.UIDefaults

val ZERO_TO_NINE: Array<Char> = arrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')

fun JFrame.showWithFixes(width: Int = 600, height: Int = 600, fullscreen: Boolean = true) {
	this.size = Dimension(width, height)
	this.minimumSize = this.size
	if (fullscreen) this.extendedState = this.extendedState or JFrame.MAXIMIZED_BOTH
	this.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
	this.isVisible = true
}

/**
 * Updates the rendering hints globally to fix pixelated/blurred text.
 *
 * [See this post on StackOverflow for details](https://stackoverflow.com/questions/31536952/how-to-fix-text-quality-in-java-graphics)
 */
fun <T : LookAndFeel> T.fixText(): T = this.apply {
	val uiDefaults: UIDefaults = this.defaults
	uiDefaults[RenderingHints.KEY_FRACTIONALMETRICS] = RenderingHints.VALUE_FRACTIONALMETRICS_ON
	uiDefaults[RenderingHints.KEY_TEXT_ANTIALIASING] = RenderingHints.VALUE_TEXT_ANTIALIAS_ON
}

var Graphics.colour: Colour
	get() = this.color
	set(value) { this.color = value }

infix operator fun Byte.div(other: Number): Byte = (this / other.toByte()).toByte()
infix operator fun Byte.times(other: Number): Byte = (this * other.toByte()).toByte()
infix operator fun Byte.plus(other: Number): Byte = (this + other.toByte()).toByte()
infix operator fun Byte.minus(other: Number): Byte = (this - other.toByte()).toByte()

fun Int.toBinaryString(): String = Integer.toBinaryString(this).takeLast(8).padStart(8, '0')
fun Byte.toBinaryString(): String = toInt().toBinaryString()
fun UByte.toBinaryString(): String = toInt().toBinaryString()

fun Double.readable(): String = if (isNaN() || isInfinite()) toString() else toBigDecimal().stripTrailingZeros().toPlainString()