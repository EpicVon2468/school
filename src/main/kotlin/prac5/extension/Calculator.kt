package io.github.epicvon2468.school.prac5.extension

import io.github.epicvon2468.school.showWithFixes

import java.awt.Button
import java.awt.Graphics
import java.awt.TextField
import java.io.Reader

import javax.swing.JFrame
import javax.swing.JPanel

fun main() {
	System.setProperty("awt.toolkit.name", "XToolkit")
	println(Calculator.eval("2*3+4"))
	println(Calculator.eval("2*(3+4)"))
	println(Calculator.eval("2*(3+4/2+1*3+(2*3))"))
	val frame = JFrame("Calculator")
	frame.add(Calculator)
	frame.showWithFixes(fullscreen = false)
}

data object Calculator : JPanel() {

	private val resultField = TextField(75)
	private val expression: StringBuilder = StringBuilder(50)

	init {
		add(resultField)
		resultField.isEnabled = false
		fun createButton(display: String, expressionText: String = display) {
			val button = Button(display)
			add(button)
			button.addActionListener {
				expression.append(expressionText)
				resultField.text = expression.toString()
				repaint()
			}
		}
		//⌫
		//expression.deleteCharAt(expression.lastIndex)
		createButton("+")
		createButton("-")
		createButton("/")
		createButton("*")
		createButton("(")
		createButton(")")
		createButton("(-)", "|")
		for (num: Int in 0..9) createButton(num.digitToChar().toString())
		val button = Button("exe")
		add(button)
		button.addActionListener {
		}
	}

	@Suppress("unused")
	private fun readResolve(): Any = Calculator

	override fun paint(g: Graphics) {
		paintComponent(g)
	}

	fun eval(input: String): Double = evaluateExpression(parse(input))

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
		// Use '|' for unary minus to avoid misreading the subtraction operator
		if (input.peek() == '|') {
			input.skip(1)
			negate = true
		} else negate = false
		return UnaryExpression(
			child = parsePrimary(input),
			negate = negate
		)
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
		val str: String = this.peek(512) {
			val result: StringBuilder = StringBuilder(12)
			var read: Char = this.read().toChar()
			while (read.isDigit() || read == '.') {
				result.append(read)
				read = this.read().toChar()
			}
			result.toString()
		}
		this.skip(str.length.toLong())
		return str.toDouble()
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
				/*op =*/ expr.childAt(index + 1),
				/*lhs =*/ value,
				/*rhs =*/ evaluate(index + 2)
			)
			// cRHS op cLHS op nLHS
			if (index + 4 >= expr.childCount) break
			index += 2
		}
		return value
	}

	fun evaluateExpression(expr: TermExpression): Double = when (expr.childCount) {
		0 -> error("No children for expression '$expr'!")
		1 -> evaluateExpression(expr.childAt<FactorExpression>(0))
		else -> evaluateOp(
			expr,
			evaluate = { index: Int ->
				evaluateExpression(expr.childAt<FactorExpression>(index))
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

	private fun evaluateExpression(expr: FactorExpression): Double = when (expr.childCount) {
		0 -> error("No children for expression '$expr'!")
		1 -> evaluateExpression(expr.childAt<UnaryExpression>(0))
		else -> evaluateOp(
			expr,
			evaluate = { index: Int ->
				evaluateExpression(expr.childAt<UnaryExpression>(index))
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

	private fun evaluateExpression(expr: UnaryExpression): Double = evaluateExpression(expr.child).let {
		if (expr.negate) it.unaryMinus() else it
	}

	private fun evaluateExpression(expr: PrimaryExpression): Double = if (expr.child != null) evaluateExpression(expr.child) else expr.literal!!
}