package io.github.epicvon2468.school.prac3

import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random
import kotlin.random.nextInt

fun main() {
	val range: IntRange = 1..100
	val number: Int = Random.nextInt(range)
	println("I'm thinking of a number in $range; Can you guess what it is?")
	val input: String? = IO.readln("Type a number: ")
	val guess: Int = input?.toIntOrNull()?.coerceIn(range) ?: throw IllegalArgumentException("Invalid number: '$input'!")
	println("Your guess is: $guess")
	println("The number I was thinking of is: $number")
	if (guess == number) println("You guessed right!")
	else println("You were off by: ${max(number, guess) - min(number, guess)}")
}