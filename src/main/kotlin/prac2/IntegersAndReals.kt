package io.github.epicvon2468.school.prac2

fun main() {
	// Set the values that we'll work with for this task.
	val x = 25.15
	val y = 3.4

	// Convert these values to integers
	val i: Int = x.toInt()
	val j: Int = y.toInt()

	// Perform some calculations and display the results
	println("As reals, the values are x: $x and y: $y")
	println("The sum, x + y = ${(x + y)}")
	println("The difference, x - y = ${(x - y)}")
	println("The product, x * y = ${(x * y)}")
	println("The quotient, x / y = ${(x / y)}")
	println("The remainder, x % y = ${(x % y)}")

	println()

	// Repeat these calculations, but as integers
	println("As integers, these are i: $i and j: $j")
	println("The sum, i + j = ${(i + j)}")
	println("The difference, i - j = ${(i - j)}")
	println("The product, i * j = ${(i * j)}")
	println("The quotient, i / j = ${(i / j)}")
	println("The remainder, i % j = ${(i % j)}")
}