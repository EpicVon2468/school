package io.github.epicvon2468.school.prac5.extension

import io.github.epicvon2468.school.ZERO_TO_NINE

import java.io.Reader

fun parse(input: String): TermExpression {
	val expr: TermExpression = parseTerm(input.reader())
	expr.validate()
	return expr
}

// factorExpr (('+' | '-') factorExpr)*
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

// unaryExpr (('/' | '*') unaryExpr)*
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

// '-'? powExpr
private fun parseUnary(input: Reader): UnaryExpression = UnaryExpression(
	negate = input.tryEat('-'),
	child = parsePow(input)
)

// functionExpr ('^' functionExpr)*
private fun parsePow(input: Reader): PowExpression {
	val children: MutableList<Any> = mutableListOf(parseFunction(input))
	while (input.tryEat('^')) {
		children += '^'
		children += parseFunction(input)
	}
	return PowExpression(children)
}

// ('sqrt' | 'sin' | 'cos' | 'tan') '(' primaryExpr ')' | primaryExpr
private fun parseFunction(input: Reader): FunctionExpression {
	var hasFunction = false
	val function: FunctionExpression.Function = if (input.peek() in arrayOf('(', '.', *ZERO_TO_NINE)) {
		FunctionExpression.Function.IDENTITY
	} else {
		hasFunction = true
		val identifier: String = input.read(3)
		// sqrt is the only four-letter function
		if (input.tryEat('t')) FunctionExpression.Function.SQRT
		else FunctionExpression.Function.lookup(identifier)
	}
	if (hasFunction) input.skip(1) // '('
	val result = FunctionExpression(
		child = parsePrimary(input),
		function = function
	)
	if (hasFunction) input.skip(1) // ')'
	return result
}

// literal | '(' termExpr ')'
private fun parsePrimary(input: Reader): PrimaryExpression {
	if (input.tryEat('(')) {
		val result = PrimaryExpression(child = parseTerm(input))
		input.skip(1) // ')'
		return result
	}
	return PrimaryExpression(literal = parseLiteral(input))
}

// [0-9]* '.'* [0-9]*
private fun parseLiteral(input: Reader): Double {
	val value: String = input.peek(512) {
		val result: StringBuilder = StringBuilder(12)
		var read: Char = this.read().toChar()
		while (read.isDigit() || read == '.') {
			result.append(read)
			read = this.read().toChar()
		}
		result.toString()
	}
	input.skip(value.length.toLong())
	return value.toDouble()
}