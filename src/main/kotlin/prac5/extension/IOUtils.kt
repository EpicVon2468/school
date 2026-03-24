package io.github.epicvon2468.school.prac5.extension

import java.io.Reader

fun Reader.tryEat(vararg chars: Char): Boolean {
	return if (this.peek() in chars) {
		this.skip(1)
		true
	} else false
}

fun Reader.peek(): Char = this.peek(1, Reader::read).toChar()

inline fun <T> Reader.peek(count: Int, block: Reader.() -> T): T {
	this.mark(count)
	val result: T = this.block()
	this.reset()
	return result
}