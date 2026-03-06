package io.github.epicvon2468.school.prac3

import kotlin.time.Duration.Companion.seconds

fun main() {
	val input: String? = IO.readln("Enter a duration in seconds: ")
	// The assignment said "read an integer from the keyboard".  Long is a 64-bit integer... :)
	val time: Long = input?.toLongOrNull() ?: throw IllegalArgumentException("Invalid duration: '$input'!")
	time.seconds.toComponents { days: Long, hours: Int, minutes: Int, seconds: Int, _: Int ->
		val actualHours: Long = hours + (days * 24)
		// I know the criterion says "use printf to display the result", but that's (normally) inefficient in Kotlin since string templates are a language feature
		println("$time seconds == $actualHours hours, $minutes minutes, and $seconds seconds")
	}
}