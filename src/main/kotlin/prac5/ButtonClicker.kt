package io.github.epicvon2468.school.prac5

import io.github.epicvon2468.school.*

import java.awt.Graphics
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.SwingUtilities

fun main() = SwingUtilities.invokeLater {
	val frame = JFrame("Button Clicker")
	frame.add(ButtonClicker)
	frame.showWithFixes(fullscreen = false)
}

data object ButtonClicker : JPanel(), ActionListener {

	@Suppress("unused")
	private fun readResolve(): Any = ButtonClicker

	private val button: JButton = KButton("Press Here")
	private var count: Int = 0

	// Static initialiser block, no need for a method :)
	init {
		add(button)
		button.addActionListener(this)
	}

	override fun paint(g: Graphics) {
		super.paint(fixText(g))
		g.drawString("Number of button presses is $count", 20, 120)
	}

	override fun actionPerformed(e: ActionEvent) {
		count++
		repaint()
	}
}