package io.github.epicvon2468.school.prac5

import io.github.epicvon2468.school.*

import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.SwingUtilities

import kotlin.random.Random

fun main() = SwingUtilities.invokeLater {
	val frame = JFrame("Button Clicker")
	frame.add(ButtonClicker)
	frame.showWithFixes(fullscreen = false)
}

data object ButtonClicker : JPanel(), ActionListener {

	@Suppress("unused")
	private fun readResolve(): Any = ButtonClicker

	private val button: JButton = JButton("Press Here")
	// Use Long so that Random.nextInt() doesn't cause a near-instant overflow
	private var count: Long = 0

	// Static initialiser block, no need for a method :)
	init {
		add(button)
		button.addActionListener(this)
	}

	override fun paint(g: Graphics) {
		// FIXME: LookAndFeel didn't affect Graphics!
		g as Graphics2D
		g.addRenderingHints(
			mapOf(
				RenderingHints.KEY_FRACTIONALMETRICS to RenderingHints.VALUE_FRACTIONALMETRICS_ON,
				RenderingHints.KEY_TEXT_ANTIALIASING to RenderingHints.VALUE_TEXT_ANTIALIAS_ON
			)
		)
		super.paint(g)
		g.drawString("Number of button presses is $count", 20, 120)
	}

	override fun actionPerformed(e: ActionEvent) {
		count += Random.nextInt()
		repaint()
	}
}