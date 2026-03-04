package io.github.epicvon2468.school.testing

import kotlin.experimental.inv

import io.github.epicvon2468.school.*

// https://discuss.kotlinlang.org/t/when-does-bit-fiddling-come-to-kotlin/2249
fun main() {
}

fun negativeOf(input: Byte): Byte = input.inv() plus 1

fun check(input: Byte) {
	println(input.toBinaryString())

	val negative: Byte = negativeOf(input)
	val bin: String = negative.toBinaryString()
	println(bin)
	val bin1: String = (-input).toByte().toBinaryString()
	println(bin1)
	println(bin == bin1)

	println(input + negative == 0)
}