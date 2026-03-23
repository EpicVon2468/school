package io.github.epicvon2468.school.prac5.extension

import io.github.epicvon2468.school.showWithFixes

import java.awt.Graphics
import java.awt.TextField
import java.io.Reader

import javax.swing.JFrame
import javax.swing.JPanel

fun main() {
	System.setProperty("awt.toolkit.name", "XToolkit")
	val frame = JFrame("Calculator")
	frame.add(Calculator)
	frame.showWithFixes(fullscreen = false)
}

data object Calculator : JPanel() {

	private val resultField = TextField(75)

	init {
		add(resultField)
		resultField.isEnabled = false
	}

	@Suppress("unused")
	private fun readResolve(): Any = Calculator

	override fun paint(g: Graphics) {
		paintComponent(g)
	}

	fun parseTerm(input: Reader): TermExpression {
		val result = TermExpression(child = parseFactor(input))
		var next: Char = input.peek()
		while (next == '+' || next == '-') {
			input.skip(1)
			result.rest += next
			result.rest += parseFactor(input)
			next = input.peek()
		}
		return result
	}

	fun parseFactor(input: Reader): FactorExpression {
		val result = FactorExpression(child = parseUnary(input))
		var next: Char = input.peek()
		while (next == '/' || next == '*') {
			input.skip(1)
			result.rest += next
			result.rest += parseUnary(input)
			next = input.peek()
		}
		return result
	}

	fun parseUnary(input: Reader): UnaryExpression {
		if (input.peek() == '|') {
			input.skip(1)
			return UnaryExpression(negateMe = parsePrimary(input))
		}
		return UnaryExpression(child = parsePrimary(input))
	}

	fun parsePrimary(input: Reader): PrimaryExpression {
		if (input.peek() == '(') {
			input.skip(1) // '('
			val result = PrimaryExpression(child = parseTerm(input))
			input.skip(1) // ')'
			return result
		}
		return PrimaryExpression(literal = input.readDouble())
	}

	fun Reader.readDouble(): Double {
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

	fun evaluateOp(
		expr: Expression,
		evaluate: (index: Int) -> Double,
		evaluateOp: (op: Char, lhs: Double, rhs: Double) -> Double
	): Double {
		var value = evaluate(0)
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
		1 -> evaluateExpression(expr.child)
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

	fun evaluateExpression(expr: FactorExpression): Double = when (expr.childCount) {
		0 -> error("No children for expression '$expr'!")
		1 -> evaluateExpression(expr.child)
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

	fun evaluateExpression(expr: UnaryExpression): Double = when (expr.childCount) {
		0 -> error("No children for expression '$expr'!")
		1 -> evaluateExpression(expr.child!!)
		else -> return evaluateExpression(expr.negateMe!!).unaryMinus()
	}

	fun evaluateExpression(expr: PrimaryExpression): Double = if (expr.child != null) evaluateExpression(expr.child) else expr.literal!!
}