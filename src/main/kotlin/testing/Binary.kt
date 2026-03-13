package io.github.epicvon2468.school.testing

import kotlin.experimental.inv

import io.github.epicvon2468.school.*

import kotlin.math.pow

// https://discuss.kotlinlang.org/t/when-does-bit-fiddling-come-to-kotlin/2249
fun main() {
}

fun Double.readable(): String = this.toBigDecimal().stripTrailingZeros().toPlainString()

fun bitInfo(bits: Int) {
	val half: Double = 2.0.pow(bits) / 2.0
	Int.MAX_VALUE
	println("type: i$bits")
	println("max: ${(half - 1.0).readable()}")
	println("min: ${half.unaryMinus().readable()}")
	var out = 2.0
	val output: StringBuilder = StringBuilder(500)
	output.append("1 ")
	while (out <= half) {
		output.append(out.readable())
		output.append(' ')
		out *= 2.0
	}
	val reversed: MutableList<String> = output
		.dropLast(1)
		.split(' ')
		.reversed()
		.drop(1)
		.toMutableList()
	reversed.addFirst('±'.toString())
	println(
		reversed.joinToString(
			separator = " ",
			transform = {
				if (it.substringAfterLast('.') == "0") it.substringBeforeLast('.')
				else it
			}
		)
	)
	println()
	require(reversed.size == bits)
}

fun checkUnsigned(input: UByte) {
	println("normal input: $input")
	println("binary input: ${input.toBinaryString()}")
	println()
}

fun negativeOf(input: Byte): Byte = input.inv() plus 1

fun checkSigned(input: Byte) {
	println(input)
	println(-input)
	println("'normal' input: ${input.toBinaryString()}")

	val negative: Byte = negativeOf(input)
	val bin: String = negative.toBinaryString()
	println("negative input: $bin")
	val bin1: String = (-input).toByte().toBinaryString()
	println("negative input: $bin1")
	println(bin == bin1)

	println(input + negative == 0)
	println()
}