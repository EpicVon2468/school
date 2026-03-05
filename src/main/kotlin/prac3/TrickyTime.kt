package io.github.epicvon2468.school.prac3

import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

private val FORMAT: DateTimeFormatter = DateTimeFormatterBuilder()
	.appendValue(/*field =*/ ChronoField.CLOCK_HOUR_OF_AMPM, /*width =*/ 2)
	.appendLiteral(':')
	.appendValue(/*field =*/ ChronoField.MINUTE_OF_HOUR, /*width =*/ 2)
	.toFormatter()

fun main() {
	val input: String? = IO.readln("Enter a time in 24-hr format (HHMM): ")
	fun badInput(): Nothing = throw IllegalArgumentException("Invalid time: '$input'!")
	require(input?.length == 4) { "Invalid time: '$input'" }
	val time: LocalTime = LocalTime.of(
		input.take(2).toIntOrNull() ?: badInput(),
		input.takeLast(2).toIntOrNull() ?: badInput()
	)
	print("The time is: ${time.format(FORMAT)}${if (time.hour >= 12) "pm" else "am"} or ${englishFormat(time.hour, time.minute)}")
}

private fun englishFormat(hour: Int, minute: Int): String {
	// Shade in the hour parameter since parameters are immutable in Kotlin
	var hour: Int = hour
	fun incHour() {
		if (hour == 23) hour = 0 else hour++
	}
	var minuteFirst = false
	val minuteText: String = when (minute) {
		0 -> "o'clock"
		10 -> {
			minuteFirst = true
			"ten past"
		}
		15 -> {
			minuteFirst = true
			"quarter past"
		}
		30 -> {
			minuteFirst = true
			"half past"
		}
		45 -> {
			minuteFirst = true
			incHour()
			"quarter to"
		}
		50 -> {
			minuteFirst = true
			incHour()
			"ten to"
		}
		else -> TODO()
	}
	val format: String = if (minuteFirst) $$"%2$s %1$s" else $$"%1$s %2$s"
	return format.format(TIME_TO_STRING[hour], minuteText)
}

private val TIME_TO_STRING: Map<Int, String> = mutableMapOf(
	1 to "one", 2 to "two", 3 to "three", 4 to "four",
	5 to "five", 6 to "six", 7 to "seven", 8 to "eight",
	9 to "nine", 10 to "ten", 11 to "eleven", 12 to "twelve"
).also {
	fun copy(new: Int, original: Int) {
		it[new] = it[original]!!
	}
	copy(13, 1)
	copy(14, 2)
	copy(15, 3)
	copy(16, 4)
	copy(17, 5)
	copy(18, 6)
	copy(19, 7)
	copy(20, 8)
	copy(21, 9)
	copy(22, 10)
	copy(23, 11)
	copy(0, 12)
}