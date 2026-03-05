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
	val minuteText: String = when (minute.coerceIn(0..59)) {
		0 -> "o'clock"
		in 1..9 -> intermediary(oPrefix = true)

		10 -> {
			minuteFirst = true
			"ten past"
		}
		in 11..14 -> intermediary()

		15 -> {
			minuteFirst = true
			"quarter past"
		}
		in 16..19 -> intermediary()

		20 -> {
			minuteFirst = true
			"twenty past"
		}
		in 21..29 -> intermediary()

		30 -> {
			minuteFirst = true
			"half past"
		}

		in 31..39 -> intermediary()
		40 -> {
			minuteFirst = true
			"forty past"
		}

		in 41..44 -> intermediary()
		45 -> {
			minuteFirst = true
			incHour()
			"quarter to"
		}

		in 46..49 -> intermediary()
		50 -> {
			minuteFirst = true
			incHour()
			"ten to"
		}

		in 51..59 -> intermediary()
		else -> error("This line should NEVER run ($minute escaped range coercion 0..59)")
	}
	val format: String = if (minuteFirst) $$"%2$s %1$s" else $$"%1$s %2$s"
	return format.format(HOUR_TO_STRING[hour], minuteText)
}

private fun MutableMap<Int, String>.copy(from: Int, to: Int) {
	this[to] = this[from]!!
}

private val TIME_TO_STRING: Map<Int, String> = mutableMapOf(
	1 to "one", 2 to "two", 3 to "three", 4 to "four",
	5 to "five", 6 to "six", 7 to "seven", 8 to "eight",
	9 to "nine", 10 to "ten", 11 to "eleven", 12 to "twelve"
).apply {
	for (i in 13..19) this[i] = this[i - 10]!! + "teen"
	fun cloneTens(tens: Int, word: String) {
		this[tens] = word
		for (i in (1 + tens)..(9 + tens)) this[i] = "$word-${this[i - tens]}"
	}
	cloneTens(20, "twenty")
	cloneTens(30, "thirty")
	cloneTens(40, "forty")
	cloneTens(50, "fifty")
	cloneTens(60, "sixty")
	cloneTens(70, "seventy")
	cloneTens(80, "eighty")
	cloneTens(90, "ninety")
}

private val HOUR_TO_STRING: Map<Int, String> = LinkedHashMap(TIME_TO_STRING).apply {
	for (i in 1..11) copy(from = i, to = i + 12)
	copy(from = 12, to = 0)
}