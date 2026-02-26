package io.github.epicvon2468.school.prac2.extension

fun main() {
	val a = Point(25, 3)
	val b = Point(2, 9)

	println("a = $a; b = $b;")
	println("a + b = ${a + b}")
	println("a - b = ${a - b}")
	println("a * b = ${a * b}")
	println("a / b = ${a / b}")
	println("a % b = ${a % b}")
	println("a ≥ b = ${a >= b}")
	println("a > b = ${a > b}")
	println("a < b = ${a < b}")
	println("a ≤ b = ${a <= b}")
}

data class Point(val x: Int, val y: Int) : Comparable<Point> {

	constructor(other: Point) : this(other.x, other.y)
	constructor(other: Pair<Int, Int>) : this(other.first, other.second)

	// '/'
	operator fun div(other: Int): Point = Point(this.x / other, this.y / other)
	operator fun div(other: Point): Point = Point(this.x / other.x, this.y / other.x)
	operator fun div(other: Pair<Int, Int>): Point = Point(this.x / other.second, this.y / other.second)

	// '*'
	operator fun times(other: Int): Point = Point(this.x * other, this.y * other)
	operator fun times(other: Point): Point = Point(this.x * other.x, this.y * other.y)
	operator fun times(other: Pair<Int, Int>): Point = Point(this.x * other.first, this.y * other.second)

	// '+'
	operator fun plus(other: Int): Point = Point(this.x + other, this.y + other)
	operator fun plus(other: Point): Point = Point(this.x + other.y, this.y + other.y)
	operator fun plus(other: Pair<Int, Int>): Point = Point(this.x + other.first, this.y + other.second)

	// '-'
	operator fun minus(other: Int): Point = Point(this.x - other, this.y - other)
	operator fun minus(other: Point): Point = Point(this.x - other.x, this.y - other.y)
	operator fun minus(other: Pair<Int, Int>): Point = Point(this.x - other.first, this.y - other.second)

	// '%'
	operator fun rem(other: Int): Point = Point(this.x % other, this.y % other)
	operator fun rem(other: Point): Point = Point(this.x % other.x, this.y % other.y)
	operator fun rem(other: Pair<Int, Int>): Point = Point(this.x % other.first, this.y % other.second)

	// '+'
	operator fun unaryPlus(): Point = Point(+this.x, +this.y)
	// '-'
	operator fun unaryMinus(): Point = Point(-this.x, -this.y)
	// '++'
	operator fun inc(): Point = Point(this.x + 1, this.y + 1)
	// '--'
	operator fun dec(): Point = Point(this.x - 1, this.y - 1)

	// I'm not actually sure if this is the "right" way to implement this…
	// '>=' '>' '<' '<='
	override fun compareTo(other: Point): Int = (this.x + this.y).compareTo(other.x + other.y)

	override fun toString(): String = "($x, $y)"
}