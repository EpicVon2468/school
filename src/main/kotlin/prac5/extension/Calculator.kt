package io.github.epicvon2468.school.prac5.extension

import io.github.epicvon2468.school.readable
import io.github.epicvon2468.school.showWithFixes

import java.awt.Button
import java.awt.Color as Colour
import java.awt.Container
import java.awt.Font
import java.awt.GridLayout
import java.awt.event.ActionEvent
import java.io.Reader

import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTextArea
import javax.swing.SwingUtilities
import javax.swing.UIManager

import kotlin.math.pow

// Good examples:
// 2*3+4; 10
// 2*(3+4); 14
// 2*(3+4/2+1*3+(2*3)); 28
// -2^2; -4
// (-2)^2; 4
fun main() {
	// WLToolkit is broken with Components
	System.setProperty("awt.toolkit.name", "XToolkit")
	UIManager.setLookAndFeel(CalculatorLookAndFeel())
	// Apparently all Swing/AWT apps should be started like this.
	SwingUtilities.invokeLater {
		val frame = JFrame("Calculator")
		frame.add(Calculator)
		frame.showWithFixes(fullscreen = false)
	}
}

data object Calculator : JPanel() {

	@Suppress("unused")
	private fun readResolve(): Any = Calculator

	private val resultField = JTextArea("> ")

	init {
		font = Font(Font.MONOSPACED, Font.PLAIN, font.size)
		layout = GridLayout(/*rows =*/ 6, /*cols =*/ 0)
		add(JScrollPane(resultField))
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
		fun Container.createButton(
			label: String,
			onClick: Button.(ActionEvent) -> Unit = { display += label }
		) {
			val button = Button(label)
			add(button)
			button.addActionListener { event: ActionEvent ->
				button.onClick(event)
				repaint()
			}
		}
		fun Container.createButtons(vararg labels: String) {
			labels.forEach(this::createButton)
		}
		fun row(block: Container.() -> Unit) {
			val row = Container()
			row.layout = GridLayout(0, 5)
			add(row)
			row.block()
		}

		// ←  ↑  ↓  →  ^
		// /  7  8  9  (
		// *  4  5  6  )
		// -  1  2  3  ⌫
		// +  0  . (-) exe
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
			createButton("(-)") {
				// ndash character is used for unary minus, to avoid potential parsing bugs; might try to parse raw soon
				display += "–"
			}
			createButton("exe") {
				var input: String = currentExpression
				if (input.isEmpty() || input.isBlank()) input = "0"
				history += input
				histIndex = history.size
				val result: Double = eval(input)
				display += "\n${result.readable()}\n> "
			}
		}
	}

	fun eval(input: String): Double = evalExpr(parse(input))

	fun parse(input: String): TermExpression {
		val expr: TermExpression = parseTerm(input.reader())
		expr.validate()
		return expr
	}

	private fun parseTerm(input: Reader): TermExpression {
		val children: MutableList<Any> = mutableListOf(parseFactor(input))
		var next: Char = input.peek()
		while (next == '+' || next == '-') {
			input.skip(1)
			children += next
			children += parseFactor(input)
			next = input.peek()
		}
		return TermExpression(children)
	}

	private fun parseFactor(input: Reader): FactorExpression {
		val children: MutableList<Any> = mutableListOf(parseUnary(input))
		var next: Char = input.peek()
		while (next == '/' || next == '*') {
			input.skip(1)
			children += next
			children += parseUnary(input)
			next = input.peek()
		}
		return FactorExpression(children)
	}

	private fun parseUnary(input: Reader): UnaryExpression {
		val negate: Boolean
		// TODO: use normal minus for unary? is that possible?
		// Use '–' for unary minus to avoid misreading the subtraction operator '-'
		if (input.peek() == '–') {
			input.skip(1)
			negate = true
		} else negate = false
		return UnaryExpression(
			child = parsePow(input),
			negate = negate
		)
	}

	private fun parsePow(input: Reader): PowExpression {
		val children: MutableList<Any> = mutableListOf(parsePrimary(input))
		var next: Char = input.peek()
		while (next == '^') {
			input.skip(1)
			children += next
			children += parsePrimary(input)
			next = input.peek()
		}
		return PowExpression(children)
	}

	private fun parsePrimary(input: Reader): PrimaryExpression {
		if (input.peek() == '(') {
			input.skip(1) // '('
			val result = PrimaryExpression(child = parseTerm(input))
			input.skip(1) // ')'
			return result
		}
		return PrimaryExpression(literal = input.readDouble())
	}

	private fun Reader.readDouble(): Double {
		val value: String = this.peek(512) {
			val result: StringBuilder = StringBuilder(12)
			var read: Char = this.read().toChar()
			while (read.isDigit() || read == '.') {
				result.append(read)
				read = this.read().toChar()
			}
			result.toString()
		}
		this.skip(value.length.toLong())
		return value.toDouble()
	}

	private fun evaluateOp(
		expr: Expression,
		evaluate: (index: Int) -> Double,
		evaluateOp: (op: Char, lhs: Double, rhs: Double) -> Double
	): Double {
		var value: Double = evaluate(0)
		var index = 0
		while (index < expr.childCount) {
			value = evaluateOp(
				/*op =*/ expr.getChild(index + 1),
				/*lhs =*/ value,
				/*rhs =*/ evaluate(index + 2)
			)
			// cRHS op cLHS op nLHS
			if (index + 4 >= expr.childCount) break
			index += 2
		}
		return value
	}

	fun evalExpr(expr: TermExpression): Double = when (expr.childCount) {
		0 -> error("No children for expression '$expr'!")
		1 -> evalExpr(expr.getChild<FactorExpression>(0))
		else -> evaluateOp(
			expr = expr,
			evaluate = { index: Int ->
				evalExpr(expr.getChild<FactorExpression>(index))
			},
			evaluateOp = { op: Char, lhs: Double, rhs: Double ->
				when (op) {
					'+' -> lhs + rhs
					'-' -> lhs - rhs
					else -> error("Illegal operator '$op', expected '+' or '-'!")
				}
			}
		)
	}

	private fun evalExpr(expr: FactorExpression): Double = when (expr.childCount) {
		0 -> error("No children for expression '$expr'!")
		1 -> evalExpr(expr.getChild<UnaryExpression>(0))
		else -> evaluateOp(
			expr = expr,
			evaluate = { index: Int ->
				evalExpr(expr.getChild<UnaryExpression>(index))
			},
			evaluateOp = { op: Char, lhs: Double, rhs: Double ->
				when (op) {
					'/' -> lhs / rhs
					'*' -> lhs * rhs
					else -> error("Illegal operator '$op', expected '/' or '*'!")
				}
			}
		)
	}

	private fun evalExpr(expr: UnaryExpression): Double = evalExpr(expr.child).let {
		if (expr.negate) it.unaryMinus() else it
	}

	private fun evalExpr(expr: PowExpression): Double = when (expr.childCount) {
		0 -> error("No children for expression '$expr'!")
		1 -> evalExpr(expr.getChild<PrimaryExpression>(0))
		else -> evaluateOp(
			expr = expr,
			evaluate = { index: Int ->
				evalExpr(expr.getChild<PrimaryExpression>(index))
			},
			evaluateOp = { op: Char, lhs: Double, rhs: Double ->
				require(op == '^') { "Illegal operator '$op', expected '^'!" }
				lhs.pow(rhs)
			}
		)
	}

	private fun evalExpr(expr: PrimaryExpression): Double = if (expr.child != null) evalExpr(expr.child) else expr.literal!!
}