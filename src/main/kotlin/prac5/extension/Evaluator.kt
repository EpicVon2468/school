package io.github.epicvon2468.school.prac5.extension

import kotlin.math.pow

fun evaluateExpression(input: String): Double = evaluateExpression(parse(input))

private fun evaluateOp(
	expr: Expression,
	evaluate: (index: Int) -> Double,
	operation: (op: Char, lhs: Double, rhs: Double) -> Double
): Double {
	var value: Double = evaluate(0)
	var index = 0
	while (index < expr.childCount) {
		value = operation(
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

fun evaluateExpression(expr: TermExpression): Double = when (expr.childCount) {
	0 -> error("No children for expression '$expr'!")
	1 -> evaluateExpression(expr.getChild<FactorExpression>(0))
	else -> evaluateOp(
		expr = expr,
		evaluate = { index: Int ->
			evaluateExpression(expr.getChild<FactorExpression>(index))
		},
		operation = { op: Char, lhs: Double, rhs: Double ->
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
	1 -> evaluateExpression(expr.getChild<UnaryExpression>(0))
	else -> evaluateOp(
		expr = expr,
		evaluate = { index: Int ->
			evaluateExpression(expr.getChild<UnaryExpression>(index))
		},
		operation = { op: Char, lhs: Double, rhs: Double ->
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

private fun evaluateExpression(expr: PowExpression): Double = when (expr.childCount) {
	0 -> error("No children for expression '$expr'!")
	1 -> evaluateExpression(expr.getChild<PrimaryExpression>(0))
	else -> evaluateOp(
		expr = expr,
		evaluate = { index: Int ->
			evaluateExpression(expr.getChild<PrimaryExpression>(index))
		},
		operation = { op: Char, lhs: Double, rhs: Double ->
			require(op == '^') { "Illegal operator '$op', expected '^'!" }
			lhs.pow(rhs)
		}
	)
}

private fun evaluateExpression(expr: PrimaryExpression): Double = expr.child?.let(::evaluateExpression) ?: expr.literal!!