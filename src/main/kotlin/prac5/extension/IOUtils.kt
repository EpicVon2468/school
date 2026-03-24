package io.github.epicvon2468.school.prac5.extension

import java.io.Reader

fun Reader.read(count: Int): String = when {
	count == 1 -> this.read().toChar().toString()
	count > 1 -> CharArray(count).apply(this::read).concatToString()
	else -> throw IllegalArgumentException("Bad read request, cannot read zero or negative number of characters: $count!")
}

fun Reader.tryEat(vararg chars: Char): Boolean = if (peek() in chars) {
	skip(1)
	true
} else false

fun Reader.peek(): Char = this.peek(1, Reader::read).toChar()

inline fun <T> Reader.peek(count: Int, block: Reader.() -> T): T {
	this.mark(count)
	val result: T = this.block()
	this.reset()
	return result
}