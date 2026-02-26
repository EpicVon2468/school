package io.github.epicvon2468.school.prac2

import java.time.LocalTime

private val START: LocalTime = LocalTime.of(14, 42, 52)
private val END: LocalTime = LocalTime.of(14, 55, 6)

fun main() {
	println("Seconds elapsed since midnight: ${durationSince(end = START)}")
	println("Seconds elapsed since start: ${durationSince(start = START, end = END)}")
}

private fun durationSince(start: LocalTime = LocalTime.of(0, 0, 0), end: LocalTime): Int = end.totalSeconds - start.totalSeconds

private val LocalTime.totalSeconds
	get() = (this.hour * 60 * 60) + (this.minute * 60) + this.second