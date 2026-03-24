package io.github.epicvon2468.school.prac5.extension

import io.github.epicvon2468.school.*

import java.awt.GridLayout
import java.awt.KeyboardFocusManager
import java.awt.Color as Colour
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent

import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTextArea
import javax.swing.SwingUtilities
import javax.swing.UIManager

// Good examples:
// 2*3+4; 10
// 2*(3+4); 14
// 2*(3+4/2+1*3+(2*3)); 28
// -2^2; -4
// (-2)^2; 4
// 0/0; NaN
// 1/0; Infinity
// -1/0; -Infinity
// -0; 0
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

	private val resultField: JTextArea = JTextArea("> ")

	init {
		layout = GridLayout(/*rows =*/ 6, /*cols =*/ 0)
		add(JScrollPane(resultField))
		resultField.isEnabled = false
		resultField.disabledTextColor = Colour.BLACK

		initialiseButtons()

		val allowedLiterals: Array<Char> = arrayOf(*ZERO_TO_NINE.toTypedArray(), '/', '*', '-', '+', '.', '(', ')', '^', 'A')
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher { event: KeyEvent ->
			if (event.id != KeyEvent.KEY_PRESSED) return@addKeyEventDispatcher false
			val input: Char = event.keyChar
			if (input in allowedLiterals) {
				display += if (input == 'A') "ANS" else input
				return@addKeyEventDispatcher true
			}
			return@addKeyEventDispatcher when (event.keyCode) {
				KeyEvent.VK_ENTER -> {
					exe()
					true
				}
				KeyEvent.VK_BACK_SPACE -> {
					backspace(massDelete = event.isAltDown)
					true
				}
				KeyEvent.VK_DOWN -> {
					downArrow()
					true
				}
				KeyEvent.VK_UP -> {
					upArrow()
					true
				}
				else -> false
			}
		}
	}

	private var ANS: String = "0"

	private var display: String by resultField::text
	private inline var currentExpression: String
		get() = display.substringAfterLast("> ")
		set(value) {
			// IDEA's recursion note in the gutter is wrong here, this isn't recursive
			val prev: String = currentExpression
			if (prev.isNotEmpty() && prev.isNotBlank() && histIndex > history.lastIndex && history.lastOrNull() != prev) {
				history += prev
			}
			display = display.substringBeforeLast(' ') + ' ' + value
		}

	private val history: MutableList<String> = mutableListOf()
	private var histIndex: Int = history.size

	private fun initialiseButtons() {
		fun JComponent.createButton(
			label: String,
			onClick: JButton.(ActionEvent) -> Unit = {
				display += label
				repaint()
			}
		) {
			val button = JButton(label)
			add(button)
			button.addActionListener { event: ActionEvent ->
				button.onClick(event)
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
		// +  0  . ANS exe
		row {
			createButton("←") {
			}
			createButton("↑") {
				upArrow()
			}
			createButton("↓") {
				downArrow()
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
				// alternatively: `event.modifiers.shr(3).takeLowestOneBit() == 1`
				backspace(massDelete = (event.modifiers and ActionEvent.ALT_MASK) != 0)
			}
		}
		row {
			createButtons("+", "0", ".")
			createButton("ANS")
			createButton("exe") {
				exe()
			}
		}
	}

	private fun upArrow() {
		if (history.isEmpty()) return
		val newIndex: Int = (histIndex - 1).coerceAtLeast(0)
		currentExpression = history[newIndex]
		histIndex = newIndex
		repaint()
	}

	private fun downArrow() {
		if (history.isEmpty()) return
		val newIndex: Int = (histIndex + 1).coerceAtMost(history.size)
		currentExpression = history.getOrNull(newIndex) ?: ""
		histIndex = newIndex
		repaint()
	}

	private fun backspace(massDelete: Boolean) {
		display = if (massDelete) display.dropLastWhile { it != ' ' }
		else if (currentExpression.isNotEmpty()) display.dropLast(
			n = if (currentExpression.endsWith("ANS")) 3 else 1
		) else display
		repaint()
	}

	private fun exe() {
		var input: String = currentExpression.replace("ANS", "($ANS)")
		if (input.isEmpty() || input.isBlank()) input = "0"
		if (history.lastOrNull() != input) {
			history += input
			histIndex = history.size
		}
		val result: String = try {
			evaluateExpression(input).readable()
		} catch (e: ArithmeticException) {
			"Syntax Error!  Arithmetic failed!  Cause: '${e.localizedMessage}'!"
		} catch (e: NumberFormatException) {
			"Syntax Error!  Invalid number!  Cause: '${e.localizedMessage}'!"
		} catch (_: Exception) {
			"Syntax error!"
		}
		if (result.first() != 'S') ANS = input
		// If the JScrollPane has to add a horizontal scroll due to line length, it doesn't fully reset to the start on the horizontal access because of the '> '
		// However, even appending them separately with calls to repaint() in-between doesn't help
		display += "\n$result\n> "
		repaint()
	}
}