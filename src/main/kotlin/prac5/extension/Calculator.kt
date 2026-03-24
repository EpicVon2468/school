package io.github.epicvon2468.school.prac5.extension

import io.github.epicvon2468.school.*

import java.awt.Font
import java.awt.Graphics
import java.awt.GridLayout
import java.awt.Color as Colour
import java.awt.event.ActionEvent

import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JTextArea
import javax.swing.SwingUtilities
import javax.swing.UIManager

// Good examples:
// 2*3+4; 10
// 2*(3+4); 14
// 2*(3+4/2+1*3+(2*3)); 28
// -2^2; -4
// (-2)^2; 4
fun main() = SwingUtilities.invokeLater {
	UIManager.setLookAndFeel(CalculatorLookAndFeel())
	val frame = JFrame("Calculator")
	frame.add(Calculator)
	frame.showWithFixes(
		width = 525,
		height = 700,
		fullscreen = false
	)
}

data object Calculator : JPanel() {

	@Suppress("unused")
	private fun readResolve(): Any = Calculator

	private val resultField: JTextArea = KTextArea("> ")

	init {
		font = Font(Font.MONOSPACED, Font.PLAIN, font.size)
		layout = GridLayout(/*rows =*/ 6, /*cols =*/ 0)
		add(KScrollPane(resultField))
		resultField.isEnabled = false
		resultField.disabledTextColor = Colour.BLACK

		initialiseButtons()
	}

	private var display: String by resultField::text
	private inline var currentExpression: String
		get() = display.substringAfterLast("> ")
		set(value) {
			// IDEA's recursion note in the gutter is wrong here, this isn't recursive
			val prev: String = currentExpression
			if (prev.isNotEmpty() && prev.isNotBlank() && histIndex > history.lastIndex) {
				history += prev
			}
			display = display.substringBeforeLast(' ') + ' ' + value
		}

	private val history: MutableList<String> = mutableListOf()
	private var histIndex: Int = history.size

	private fun initialiseButtons() {
		fun JComponent.createButton(
			label: String,
			onClick: JButton.(ActionEvent) -> Unit = { display += label }
		) {
			val button: JButton = KButton(label)
			add(button)
			button.addActionListener { event: ActionEvent ->
				button.onClick(event)
				repaint()
			}
		}
		fun JComponent.createButtons(vararg labels: String) {
			labels.forEach(this::createButton)
		}
		fun row(block: JComponent.() -> Unit) {
			val row = JPanel()
			row.layout = GridLayout(0, 5)
			add(row)
			row.block()
		}

		// ←  ↑  ↓  →  ^
		// /  7  8  9  (
		// *  4  5  6  )
		// -  1  2  3  ⌫
		// +  0  .  √ exe
		row {
			createButton("←") {
			}
			createButton("↑") {
				if (history.isEmpty()) return@createButton
				val newIndex: Int = (histIndex - 1).coerceAtLeast(0)
				currentExpression = history[newIndex]
				histIndex = newIndex
			}
			createButton("↓") {
				if (history.isEmpty()) return@createButton
				val newIndex: Int = (histIndex + 1).coerceAtMost(history.size)
				currentExpression = history.getOrNull(newIndex) ?: ""
				histIndex = newIndex
			}
			createButton("→") {
			}
			createButton("^")
		}
		row {
			createButtons("/", "7", "8", "9", "(")
		}
		row {
			createButtons("*", "4", "5", "6", ")")
		}
		row {
			createButtons("-", "1", "2", "3")
			createButton("⌫") { event: ActionEvent ->
				// The shift key constant with `and()` is just kind of... not working?
				// The only real tell of shift being pressed is the least significant bit being 1
				val isShiftPressed: Boolean = event.modifiers.takeLowestOneBit() == 1
				display = if (isShiftPressed) display.dropLastWhile { it != ' ' }
				else if (currentExpression.isNotEmpty()) display.dropLast(1)
				else display
			}
		}
		row {
			createButtons("+", "0", ".")
			createButton("√") {
				TODO("Root...?")
			}
			createButton("exe") {
				var input: String = currentExpression
				if (input.isEmpty() || input.isBlank()) input = "0"
				history += input
				histIndex = history.size
				// TODO: Graceful catch
				val result: Double = evaluateExpression(input)
				display += "\n${result.readable()}\n> "
			}
		}
	}

	override fun paint(g: Graphics): Unit = super.paint(fixText(g))
}