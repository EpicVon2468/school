package io.github.epicvon2468.school.prac4

import io.github.epicvon2468.school.colour
import io.github.epicvon2468.school.showWithFixes

import java.awt.Color as Colour
import java.awt.Graphics
import java.awt.Polygon

import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.SwingUtilities

fun main() = SwingUtilities.invokeLater {
	val frame = JFrame("My House")
	frame.add(House)
	frame.showWithFixes()
}

data object House : JPanel() {

	@Suppress("unused")
	private fun readResolve(): Any = House

	override fun paint(g: Graphics) {
		super.paint(g)
		drawBackground(g)
		drawFence(
			g = g,
			startX = 20,
			startY = 220
		)
		drawFence(
			g = g,
			startX = 230,
			startY = 220
		)
		drawRoof(g)
		drawHouseBody(g)
		drawDoor(g)
		drawSun(g)
		drawChimney(g)
	}

	private fun drawFence(
		g: Graphics,
		startX: Int,
		startY: Int,
		num: Int = 4
	) {
		g.colour = Colour.WHITE
		var x = startX
		repeat(num) {
			g.fillRect(x, startY - 50, 10, 50)
			x += 20
		}
		g.fillRect(startX - 5, startY - 40, num * 20, 10)
	}

	private fun drawBackground(g: Graphics) {
		// Sky
		g.colour = Colour.CYAN
		g.fillRect(0, 0, width, 220)
		// Grass
		g.colour = Colour.GREEN
		g.fillRect(
			/*x =*/ 0,
			/*y =*/ 220,
			/*width =*/ width,
			/*height =*/ height - 220
		)
	}

	private fun drawRoof(
		g: Graphics,
		xPoints: IntArray = intArrayOf(100, 160, 220),
		yPoints: IntArray = intArrayOf(100, 50, 100)
	) {
		fun checkSize(p: IntArray, name: String) = require(p.size == 3) { "IntArray '$name' has wrong number of points!  Got ${p.size}, expected 3!  (${p.contentToString()})" }
		checkSize(xPoints, "xPoints")
		checkSize(xPoints, "yPoints")
		g.colour = Colour.RED
		val poly = Polygon(
			/*xpoints =*/ xPoints,
			/*ypoints =*/ yPoints,
			/*npoints =*/ 3
		)
		g.fillPolygon(poly)
	}

	private fun drawHouseBody(
		g: Graphics,
		x: Int = 100,
		y: Int = 100,
		width: Int = 120,
		height: Int = 120
	) {
		g.colour = Colour.BLUE
		g.fillRect(x, y, width, height)
	}

	private fun drawDoor(g: Graphics) {
		g.colour = Colour.WHITE
		g.fillRect(145, 160, 30, 60)
	}

	private fun drawSun(
		g: Graphics,
		x: Int = 240,
		y: Int = 30,
		width: Int = 50,
		height: Int = 50
	) {
		g.colour = Colour.YELLOW
		g.fillOval(x, y, width, height)
	}

	private fun drawChimney(
		g: Graphics,
		x: Int = 120,
		y: Int = 55,
		width: Int = 10,
		height: Int = 30
	) {
		g.colour = Colour.BLACK
		g.fillRect(x, y, width, height)
	}
}