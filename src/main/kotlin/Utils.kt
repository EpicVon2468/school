@file:Suppress("EXTENSION_SHADOWED_BY_MEMBER")
package io.github.epicvon2468.school

import java.awt.Color as Colour
import java.awt.*

import javax.swing.JFrame

fun JFrame.showWithFixes(width: Int = 600, height: Int = 600, fullscreen: Boolean = true) {
	this.size = Dimension(width, height)
	if (fullscreen) this.extendedState = this.extendedState or JFrame.MAXIMIZED_BOTH
	this.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
	this.isVisible = true
}

// FIXME: When you hover over any (J)Component, it redraws them without the correct flag set
/**
 * Updates the [Graphics] rendering hints to fix pixelated/blurred text.
 *
 * ALWAYS CALL BEFORE `super.paint(g)`!
 *
 * [See this post on StackOverflow for details](https://stackoverflow.com/questions/31536952/how-to-fix-text-quality-in-java-graphics)
 */
fun fixText(g: Graphics) {
	(g as Graphics2D).addRenderingHints(
		mapOf(
			RenderingHints.KEY_FRACTIONALMETRICS to RenderingHints.VALUE_FRACTIONALMETRICS_ON,
			RenderingHints.KEY_TEXT_ANTIALIASING to RenderingHints.VALUE_TEXT_ANTIALIAS_ON
		)
	)
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

fun Double.readable(): String = this.toBigDecimal().stripTrailingZeros().toPlainString()