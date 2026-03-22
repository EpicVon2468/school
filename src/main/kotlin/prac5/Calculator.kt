package io.github.epicvon2468.school.prac5

import io.github.epicvon2468.school.showWithFixes

import javax.swing.JFrame
import javax.swing.JPanel

fun main() {
	val frame = JFrame("Calculator")
	frame.add(Calculator)
	frame.showWithFixes(fullscreen = false)
}

data object Calculator : JPanel() {

	// Static initialiser block, no need for a method :)
	init {

	}

	@Suppress("unused")
	private fun readResolve(): Any = Calculator
}