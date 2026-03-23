package io.github.epicvon2468.school.prac5.extension

import io.github.epicvon2468.school.showWithFixes

import java.awt.Graphics
import java.awt.TextField

import javax.swing.JFrame
import javax.swing.JPanel

fun main() {
	System.setProperty("awt.toolkit.name", "XToolkit")
	val frame = JFrame("Calculator")
	frame.add(Calculator)
	frame.showWithFixes(fullscreen = false)
}

data object Calculator : JPanel() {

	private val resultField = TextField(75)

	init {
		add(resultField)
		resultField.isEnabled = false
	}

	@Suppress("unused")
	private fun readResolve(): Any = Calculator

	override fun paint(g: Graphics) {
		paintComponent(g)
	}
}