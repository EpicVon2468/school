@file:Suppress("EXTENSION_SHADOWED_BY_MEMBER")
package io.github.epicvon2468.school

import java.awt.Color as Colour
import java.awt.*

import javax.swing.JFrame

fun JFrame.showWithFixes() {
	this.size = Dimension(600, 600)
	this.extendedState = this.extendedState or JFrame.MAXIMIZED_BOTH
	this.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
	this.isVisible = true
}

var Graphics.colour: Colour
	get() = this.color
	set(value) { this.color = value }

infix operator fun Byte.div(other: Number): Byte = (this / other.toByte()).toByte()
infix operator fun Byte.times(other: Number): Byte = (this * other.toByte()).toByte()
infix operator fun Byte.plus(other: Number): Byte = (this + other.toByte()).toByte()
infix operator fun Byte.minus(other: Number): Byte = (this - other.toByte()).toByte()

fun Byte.toBinaryString(): String = Integer.toBinaryString(toInt()).takeLast(8)