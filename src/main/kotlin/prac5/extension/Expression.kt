package io.github.epicvon2468.school.prac5.extension

interface Expression {

	val childCount: Int get() = children.size
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

data class PrimaryExpression(
	val literal: Double? = null,
	val child: TermExpression? = null
) : Expression {

	override val childCount: Int = 1
	override val children: List<Any> = listOf()
}