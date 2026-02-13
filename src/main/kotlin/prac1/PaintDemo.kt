package io.github.epicvon2468.school.prac1

import io.github.epicvon2468.school.colour
import io.github.epicvon2468.school.showWithFixes

import java.awt.Font
import java.awt.Graphics
import java.awt.Color as Colour

import javax.swing.JFrame
import javax.swing.JPanel

fun main() {
	val frame = JFrame()
	frame.add(PaintDemo)
	frame.showWithFixes()
}

object PaintDemo : JPanel() {

	@Suppress("unused")
	private fun readResolve(): Any = PaintDemo

	override fun paint(g: Graphics) {
		// draw a rectangle using the default colour
		g.drawRect(10, 10, 340, 200)

		// draw a red square using RGB values
		g.colour = Colour(255, 0, 0)
		g.drawRect(50, 100, 80, 80)

		// draw a solid cyan circle inside the square
		g.colour = Colour.CYAN
		g.fillOval(50, 100, 80, 80)

		// draw a solid rectangle
		g.colour = Colour.BLUE
		g.fillRect(20, 20, 225, 40)

		// display some text
		g.font = Font("serif", Font.BOLD, 13)
		g.colour = Colour.YELLOW
		g.drawString("Computer Science is awesome!", 30, 40)
	}
}