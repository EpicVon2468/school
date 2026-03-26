package io.github.epicvon2468.school.prac5

import io.github.epicvon2468.school.*

import java.awt.Graphics

import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.SwingUtilities

fun main() = SwingUtilities.invokeLater {
	val frame = JFrame("Four Operation Calculator")
	frame.add(FourButtons)
	frame.showWithFixes(fullscreen = false)
}

data object FourButtons : JPanel() {

	@Suppress("unused")
	private fun readResolve(): Any = FourButtons

	private var a: Int = 0
	private var b: Int = 0
	private var result: Int = 0

	private val value1: JTextField = JTextField(/*columns =*/ 10)
	private val value2: JTextField = JTextField(/*columns =*/ 10)

	private val addButton: JButton = JButton("Press to add")
	private val subButton: JButton = JButton("Press to subtract")
	private val mulButton: JButton = JButton("Press to multiply")
	private val divButton: JButton = JButton("Press to divide")

	private var op: Char = '+'

	init {
		add(JLabel("This data object will perform a mathematical operation on two numbers."))
		add(JLabel("The first number:"))
		add(value1)
		add(JLabel("The second number:"))
		add(value2)

		add(addButton)
		add(subButton)
		add(mulButton)
		add(divButton)

		fun JButton.op(op: Char, block: (lhs: Int, rhs: Int) -> Int) = this.addActionListener {
			a = value1.text.toIntOrNull() ?: 0
			if (a == 0) value1.text = "0"
			b = value2.text.toIntOrNull() ?: 0
			if (b == 0) value2.text = "0"
			this@FourButtons.op = op
			// No exceptions here :)
			result = try { block(a, b) } catch (_: ArithmeticException) { 0 }
			this@FourButtons.repaint()
		}

		addButton.op('+') { lhs: Int, rhs: Int -> lhs + rhs }
		subButton.op('-') { lhs: Int, rhs: Int -> lhs - rhs }
		mulButton.op('*') { lhs: Int, rhs: Int -> lhs * rhs }
		divButton.op('/') { lhs: Int, rhs: Int -> lhs / rhs }
	}

	override fun paint(g: Graphics) {
		super.paint(g.fixText())
		val str = "$a $op $b = $result"
		g.drawString(str, (width / 2) - str.length, height / 2)
	}
}