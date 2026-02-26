package io.github.epicvon2468.school.prac2

import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun main() {
	val current: LocalTime = LocalTime.now().withNano(0)
	println("The time is: ${DateTimeFormatter.ISO_LOCAL_TIME.format(current)}")
	when {
		current.isBefore(START) -> println("I can't wait for Computer Science!")
		current.isAfter(END) -> println("Computer Science is over for the day… so sad!")
		else -> println("It's time for Computer Science — yay!")
	}
}

private val START: LocalTime = LocalTime.of(8, 45)
private val END: LocalTime = LocalTime.of(12, 10)