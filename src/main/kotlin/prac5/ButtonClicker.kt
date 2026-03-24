package io.github.epicvon2468.school.prac5

import io.github.epicvon2468.school.fixText
import io.github.epicvon2468.school.showWithFixes

import java.awt.Graphics
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.SwingUtilities

fun main() {
	System.setProperty("sun.java2d.opengl", "true")
	SwingUtilities.invokeLater {
		val frame = JFrame("Calculator")
		frame.add(ButtonClicker)
		frame.showWithFixes(fullscreen = false)
	}
}

data object ButtonClicker : JPanel(), ActionListener {

	@Suppress("unused")
	private fun readResolve(): Any = ButtonClicker

	private val button: JButton = JButton("Press Here")
	private var count: Int = 0

	// Static initialiser block, no need for a method :)
	init {
		add(button)
		button.addActionListener(this)
	}

	override fun paint(g: Graphics) {
		fixText(g)
		super.paint(g)
		g.drawString("Number of button presses is $count", 20, 120)
	}

	override fun actionPerformed(e: ActionEvent) {
		count++
		repaint()
	}
}