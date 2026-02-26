package io.github.epicvon2468.school.prac2

import java.time.LocalTime

val START_TIME: LocalTime = LocalTime.of(14, 42, 52)
val END_TIME: LocalTime = LocalTime.of(14, 55, 6)

fun main() {
	println("Seconds elapsed since midnight: ${durationSince(end = START_TIME)}")
	println("Seconds elapsed since start: ${durationSince(start = START_TIME, end = END_TIME)}")
}

fun durationSince(start: LocalTime = LocalTime.of(0, 0, 0), end: LocalTime): Int = end.totalSeconds - start.totalSeconds

val LocalTime.totalSeconds
	get() = (this.hour * 60 * 60) + (this.minute * 60) + this.second