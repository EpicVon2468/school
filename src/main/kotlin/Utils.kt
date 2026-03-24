@file:Suppress("EXTENSION_SHADOWED_BY_MEMBER", "NOTHING_TO_INLINE", "FunctionName")
package io.github.epicvon2468.school

import java.awt.Graphics
import java.awt.Component
import java.awt.Dimension
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.Color as Colour

import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JScrollPane
import javax.swing.JTextArea
import javax.swing.JTextField

fun JFrame.showWithFixes(width: Int = 600, height: Int = 600, fullscreen: Boolean = true) {
	this.size = Dimension(width, height)
	if (fullscreen) this.extendedState = this.extendedState or JFrame.MAXIMIZED_BOTH
	this.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
	this.isVisible = true
}

// FIXME: When you hover over any (J)Component, it redraws them without the correct flag set
// Temporary(?) solution: K<COMP_NAME> functions to act as fake "constructors" with the fixes applied
/**
 * Updates the [Graphics] rendering hints to fix pixelated/blurred text.
 *
 * ALWAYS CALL BEFORE `super.paint(g)`!
 *
 * [See this post on StackOverflow for details](https://stackoverflow.com/questions/31536952/how-to-fix-text-quality-in-java-graphics)
 */
fun fixText(g: Graphics): Graphics {
	(g as Graphics2D).addRenderingHints(
		mapOf(
			RenderingHints.KEY_FRACTIONALMETRICS to RenderingHints.VALUE_FRACTIONALMETRICS_ON,
			RenderingHints.KEY_TEXT_ANTIALIASING to RenderingHints.VALUE_TEXT_ANTIALIAS_ON
		)
	)
	return g
}

// Kotlin 'fixes' for the Java Components.  I wish there was an easier way to do this.

inline fun KTextField(columns: Int): JTextField = object : JTextField(columns) {

	override fun paint(g: Graphics): Unit = super.paint(fixText(g))
}

inline fun KTextArea(text: String? = null): JTextArea = object : JTextArea(text) {

	override fun paint(g: Graphics) = super.paint(fixText(g))
}

inline fun KLabel(text: String): JLabel = object : JLabel(text) {

	override fun paint(g: Graphics): Unit = super.paint(fixText(g))
}

inline fun KButton(text: String? = null): JButton = object : JButton(text) {

	override fun paint(g: Graphics) = super.paint(fixText(g))
}

inline fun KScrollPane(view: Component): JScrollPane = object : JScrollPane(view) {

	override fun paint(g: Graphics): Unit = super.paint(fixText(g))
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