package io.github.epicvon2468.school.prac1

import io.github.epicvon2468.school.colour
import io.github.epicvon2468.school.showWithFixes

import java.awt.Color as Colour
import java.awt.Graphics
import java.awt.Polygon
import java.awt.Toolkit

import javax.swing.JFrame
import javax.swing.JPanel

fun main() {
	val frame = JFrame("My House")
	frame.add(House)
	frame.showWithFixes()
	println(Toolkit.getDefaultToolkit()::class.qualifiedName)
}

object House : JPanel() {

	@Suppress("unused")
	private fun readResolve(): Any = House

	override fun paint(g: Graphics) {
		// Draw the roof
		g.colour = Colour.RED
		val x: IntArray = intArrayOf(100, 160, 220)
		val y: IntArray = intArrayOf(100, 50, 100)
		val poly = Polygon(x, y, 3)
		g.fillPolygon(poly)

		// Draw the body of house
		g.colour = Colour.BLUE
		g.fillRect(100, 100, 120, 120)

		// draw the door
		g.colour = Colour.WHITE
		g.fillRect(145, 160, 30, 60)

		// draw sun
		g.colour = Colour.YELLOW
		g.fillOval(240, 30, 50, 50)

		// draw chimney
		g.colour = Colour.BLACK
		g.fillRect(120, 55, 10, 30)
	}
}