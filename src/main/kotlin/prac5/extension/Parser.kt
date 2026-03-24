package io.github.epicvon2468.school.prac5.extension

import io.github.epicvon2468.school.ZERO_TO_NINE

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
	val children: MutableList<Any> = mutableListOf(parseFunction(input))
	while (input.tryEat('^')) {
		children += '^'
		children += parseFunction(input)
	}
	return PowExpression(children)
}

private fun parseFunction(input: Reader): FunctionExpression {
	input.mark(1)
	val function: FunctionExpression.Function = if (input.tryEat('(', '.', *ZERO_TO_NINE.toCharArray())) {
		input.reset()
		FunctionExpression.Function.IDENTITY
	} else {
		val identifier: String = input.read(3)
		// sqrt is the only four-letter function
		if (input.tryEat('t')) FunctionExpression.Function.SQRT
		else FunctionExpression.Function.lookup(identifier)
	}
	val notIdentity: Boolean = function != FunctionExpression.Function.IDENTITY
	if (notIdentity) input.skip(1) // '('
	val result = FunctionExpression(
		child = parsePrimary(input),
		function = function
	)
	if (notIdentity) input.skip(1) // ')'
	return result
}

private fun parsePrimary(input: Reader): PrimaryExpression {
	if (input.tryEat('(')) {
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