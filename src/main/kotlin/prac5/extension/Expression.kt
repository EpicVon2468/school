package io.github.epicvon2468.school.prac5.extension

interface Expression {

	val childCount: Int get() = 1 + rest.size
	val rest: List<Any>

	fun getChild(index: Int): Any = rest[index]

	@Suppress("UNCHECKED_CAST")
	fun <T> childAt(index: Int): T = getChild(index) as T
}

data class TermExpression(
	val child: FactorExpression,
	// Char, FactorExpression
	override val rest: List<Any> = listOf()
) : Expression {

	override fun getChild(index: Int): Any = if (index == 0) child else super.getChild(index - 1)
}

data class FactorExpression(
	val child: UnaryExpression,
	override val rest: List<Any> = listOf()
) : Expression {

	override fun getChild(index: Int): Any = if (index == 0) child else super.getChild(index - 1)
}

data class UnaryExpression(
	val next: UnaryExpression?,
	val child: PrimaryExpression?
) : Expression {

	override val childCount: Int = if (child != null) 1 else 2

	override fun getChild(index: Int): Any {
		if (child != null) return child
		return next!!
	}

	override val rest: List<Any> = listOf()
}

data class PrimaryExpression(
	val literal: Double? = null,
	val child: TermExpression? = null
)