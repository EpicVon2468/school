package io.github.epicvon2468.school.prac2

// No need to reinvent the wheel
import java.time.DayOfWeek
import java.time.Month
import java.time.Year

fun main() {
	println("Hello, World!")
	val day: Pair<DayOfWeek, Int> = DayOfWeek.THURSDAY to 24
	val month: Month = Month.FEBRUARY
	val year: Year = Year.parse("2022")
	// Can't use 'te' / 'td' instead of 'd' because it seems to try and get the current date (and gets '1' instead)
	printDate($$"Australian format: %1$tA, %2$d %3$tB %4$tY", day, month, year)
	printDate($$"Yank format: %1$tA, %3$tB %2$d, %4$tY", day, month, year)
}

private fun printDate(pattern: String, day: Pair<DayOfWeek, Int>, month: Month, year: Year): Unit = println(
	pattern.format(
		day.first,
		day.second.coerceIn(1..month.maxLength()),
		month,
		year
	)
)