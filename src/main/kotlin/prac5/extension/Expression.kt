package io.github.epicvon2468.school.prac5.extension

interface Expression {

	val childCount: Int get() = 1 + rest.size
	val rest: MutableList<Any>

	fun getChild(index: Int): Any = rest[index]

	@Suppress("UNCHECKED_CAST")
	fun <T> childAt(index: Int): T = getChild(index) as T
}

data class TermExpression(
	val child: FactorExpression,
	// Char, FactorExpression
	override val rest: MutableList<Any> = mutableListOf()
) : Expression {

	override fun getChild(index: Int): Any = if (index == 0) child else super.getChild(index - 1)
}

data class FactorExpression(
	val child: UnaryExpression,
	override val rest: MutableList<Any> = mutableListOf()
) : Expression {

	override fun getChild(index: Int): Any = if (index == 0) child else super.getChild(index - 1)
}

data class UnaryExpression(
	val child: PrimaryExpression,
	val negate: Boolean
) : Expression {

	override val childCount: Int = if (negate) 2 else 1

	override fun getChild(index: Int): Any = child

	override val rest: MutableList<Any> = mutableListOf()
}

data class PrimaryExpression(
	val literal: Double? = null,
	val child: TermExpression? = null
)