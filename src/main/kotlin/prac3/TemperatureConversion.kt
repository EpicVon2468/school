package io.github.epicvon2468.school.prac3

fun main() {
	val input: String? = IO.readln("Enter a temperature in Celsius: ")
	val celsius: Double = input?.toDoubleOrNull() ?: throw IllegalArgumentException("Invalid temperature: '$input'!")
	val fahrenheit: Double = celsius * (9.0 / 5.0) + 32.0
	println("%1$.1f °C == %2$.1f °F".format(celsius, fahrenheit))
}