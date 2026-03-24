package io.github.epicvon2468.school.prac5.extension

import java.io.Reader

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

private fun parseUnary(input: Reader): UnaryExpression = UnaryExpression(
	negate = input.tryEat('-'),
	child = parsePow(input)
)

private fun parsePow(input: Reader): PowExpression {
	val children: MutableList<Any> = mutableListOf(parsePrimary(input))
	while (input.tryEat('^')) {
		children += '^'
		children += parsePrimary(input)
	}
	return PowExpression(children)
}

private fun parsePrimary(input: Reader): PrimaryExpression {
	if (input.tryEat('(')) {
		val result = PrimaryExpression(child = parseTerm(input))
		input.skip(1) // ')'
		return result
	}
	return PrimaryExpression(literal = input.readDouble())
}

// TODO: Could swap this out for BigDecimal for infinite* precision
// *memory limits apply
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