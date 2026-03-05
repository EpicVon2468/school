package io.github.epicvon2468.school.prac3

import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

import kotlin.collections.set

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
	fun intermediary(oPrefix: Boolean = false): String = "${if (oPrefix) "o'" else ""}${TIME_TO_STRING[minute]}"
	val minuteText: String = when (minute) {
		0 -> "o'clock"
		1, 2, 3, 4, 5, 6, 7, 8, 9 -> intermediary(oPrefix = true)
		10 -> {
			minuteFirst = true
			"ten past"
		}
		11, 12, 13, 14 -> intermediary()
		15 -> {
			minuteFirst = true
			"quarter past"
		}
		16, 17, 18, 19 -> intermediary()
		20 -> {
			minuteFirst = true
			"twenty past"
		}
		21, 22, 23, 24, 25, 26, 27, 28, 29 -> intermediary()
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
	return format.format(HOUR_TO_STRING[hour], minuteText)
}

private fun MutableMap<Int, String>.copy(new: Int, original: Int) {
	this[new] = this[original]!!
}

private val TIME_TO_STRING: Map<Int, String> = mutableMapOf(
	1 to "one", 2 to "two", 3 to "three", 4 to "four",
	5 to "five", 6 to "six", 7 to "seven", 8 to "eight",
	9 to "nine", 10 to "ten", 11 to "eleven", 12 to "twelve"
).apply {
	for (i in 13..19) this[i] = this[i - 10]!! + "teen"
	this[20] = "twenty"
	for (i in 21..29) this[i] = "twenty-" + this[i - 20]!!
	this[30] = "thirty"
}

private val HOUR_TO_STRING: Map<Int, String> = LinkedHashMap(TIME_TO_STRING).apply {
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