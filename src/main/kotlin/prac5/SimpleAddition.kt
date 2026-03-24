package io.github.epicvon2468.school.prac5

import io.github.epicvon2468.school.showWithFixes

import java.awt.Graphics
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.SwingUtilities

fun main() = SwingUtilities.invokeLater {
	val frame = JFrame("Simple Addition")
	frame.add(SimpleAddition)
	frame.showWithFixes(
		width = 300,
		height = 300,
		fullscreen = false
	)
}

data object SimpleAddition : JPanel(), ActionListener {

	@Suppress("unused")
	private fun readResolve(): Any = SimpleAddition

	private var a: Int = 0
	private var b: Int = 0

	private val prompt1 = JLabel("This class will add two numbers.")
	private val prompt2 = JLabel("Type in the numbers in the spaces.")
	private val value1 = JTextField(/*columns =*/ 10)
	private val value2 = JTextField(/*columns =*/ 10)
	private val button = JButton("Press to add")

	init {
		add(prompt1)
		add(prompt2)
		add(value1)
		add(value2)
		add(button)
		button.addActionListener(this)
	}

	override fun paint(g: Graphics) {
		super.paint(g)
		val result: Int = a + b
		println(result)
		g.drawString("$a + $b = $result", 100, 200)
	}

	override fun actionPerformed(e: ActionEvent) {
		// No exceptions & fields are forced into a valid state
		a = value1.text.toIntOrNull() ?: 0
		if (a == 0) value1.text = "0"
		b = value2.text.toIntOrNull() ?: 0
		if (b == 0) value2.text = "0"
		repaint()
	}
}