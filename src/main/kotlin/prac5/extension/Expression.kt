package io.github.epicvon2468.school.prac5.extension

interface Expression {

	val childCount: Int get() = children.size
	val children: MutableList<Any>

	fun getChild(index: Int): Any = children[index]

	@Suppress("UNCHECKED_CAST")
	fun <T> childAt(index: Int): T = getChild(index) as T

	fun validate(): Unit = children.forEach { child: Any ->
		if (child is Expression) child.validate()
		else require(child is Char)
	}
}

data class TermExpression(
	override val children: MutableList<Any>
) : Expression {

	constructor(child: FactorExpression) : this(children = mutableListOf(child))
}

data class FactorExpression(
	override val children: MutableList<Any>
) : Expression {

	constructor(child: UnaryExpression) : this(children = mutableListOf(child))
}

data class UnaryExpression(
	val child: PrimaryExpression,
	val negate: Boolean
) : Expression {

	override val childCount: Int = if (negate) 2 else 1

	override fun getChild(index: Int): Any = child

	override val children: MutableList<Any> = mutableListOf(child)
}

data class PrimaryExpression(
	val literal: Double? = null,
	val child: TermExpression? = null
) : Expression {

	override val children: MutableList<Any> = mutableListOf()
}