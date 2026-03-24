package io.github.epicvon2468.school.prac5.extension

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

abstract class PrefixedExpression<E : Expression> : Expression {

	abstract val child: E

	abstract val hasPrefix: Boolean

	override val childCount: Int = if (hasPrefix) 2 else 1

	@Suppress("UNCHECKED_CAST")
	override fun <T> getChild(index: Int): T = child as T

	override val children: List<Any> get() = listOf(child)
}

data class TermExpression(
	override val children: List<Any>
) : Expression

data class FactorExpression(
	override val children: List<Any>
) : Expression

data class UnaryExpression(
	override val child: PowExpression,
	val negate: Boolean
) : PrefixedExpression<PowExpression>() {

	override val hasPrefix: Boolean = negate
}

data class PowExpression(
	override val children: List<Any>
) : Expression

data class FunctionExpression(
	override val child: PrimaryExpression,
	val function: Function,
) : PrefixedExpression<PrimaryExpression>() {

	override val hasPrefix: Boolean = function != Function.IDENTITY

	enum class Function(val identifier: String) {
		IDENTITY(""),
		SQRT("sqrt"),
		// TODO: What should log do by default?  log10 or ln?
//		LOG("log"),
		SIN("sin"),
		COS("cos"),
		TAN("tan"),
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