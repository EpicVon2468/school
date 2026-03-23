package io.github.epicvon2468.school.prac5.extension

import java.io.Reader

fun Reader.peek(): Char = this.peek(1, Reader::read).toChar()

inline fun <T> Reader.peek(count: Int, block: Reader.() -> T): T {
	this.mark(count)
	val result: T = this.block()
	this.reset()
	return result
}