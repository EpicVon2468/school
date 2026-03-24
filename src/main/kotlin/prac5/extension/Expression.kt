package io.github.epicvon2468.school.prac5.extension

import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

interface Expression {

	val childCount: Int get() = children.size
	// expr (op expr)*
	val children: List<Any>

	@Suppress("UNCHECKED_CAST")
	fun <T> getChild(index: Int): T = children[index] as T

	fun validate() {
		require(childCount > 0)
		children.forEach { child: Any ->
			if (child is Expression) child.validate()
			else require(child is Char)
		}
	}
}

data class TermExpression(
	override val children: List<Any>
) : Expression

data class FactorExpression(
	override val children: List<Any>
) : Expression

data class UnaryExpression(
	val child: PowExpression,
	val negate: Boolean
) : Expression {

	override val childCount: Int = if (negate) 2 else 1

	@Suppress("UNCHECKED_CAST")
	override fun <T> getChild(index: Int): T = child as T

	override val children: List<Any> = listOf(child)
}

data class PowExpression(
	override val children: List<Any>
) : Expression

data class FunctionExpression(
	val child: PrimaryExpression,
	val function: Function,
) : Expression {

	override val childCount: Int = if (function != Function.IDENTITY) 2 else 1

	@Suppress("UNCHECKED_CAST")
	override fun <T> getChild(index: Int): T = child as T

	override val children: List<Any> = listOf(child)

	// TODO: To stay consistent, the logic shouldn't be stored here, it should be executed in the evaluateExpression function in a when statement
	enum class Function(
		val identifier: String,
		val invoke: (Double) -> Double
	) {
		IDENTITY(
			identifier = "",
			invoke = { it }
		),
		SQRT(
			identifier = "sqrt",
			invoke = { sqrt(it) }
		),
		// TODO: What should log do by default?  log10 or ln?
//		LOG(
//			identifier = "log",
//			invoke = { log10(it) }
//		),
		SIN(
			identifier = "sin",
			invoke = { sin(it) }
		),
		COS(
			identifier = "cos",
			invoke = { sin(it) }
		),
		TAN(
			identifier = "tan",
			invoke = { tan(it) }
		)
		;

		override fun toString(): String = this.identifier

		companion object {

			fun lookup(identifier: String): Function = entries.first { it.identifier == identifier }
		}
	}
}

data class PrimaryExpression(
	val literal: Double? = null,
	val child: TermExpression? = null
) : Expression {

	override val childCount: Int = 1
	override val children: List<Any> = listOf()
}