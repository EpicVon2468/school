package io.github.epicvon2468.school.prac1

import org.glfw.glfw3_h.*

import java.io.InputStream

import java.lang.foreign.MemoryLayout
import java.lang.foreign.SequenceLayout
import java.lang.foreign.StructLayout

val VEC2_LAYOUT: SequenceLayout = vecOf(2L)
val VEC3_LAYOUT: SequenceLayout = vecOf(3L)

fun vecOf(count: Long): SequenceLayout = MemoryLayout.sequenceLayout(count, C_FLOAT)

val VERTEX_LAYOUT: StructLayout = MemoryLayout.structLayout(
	VEC2_LAYOUT.withName("pos"),
	VEC3_LAYOUT.withName("col")
).withName("Vertex")

fun main() {
	NULL()
	println(FRAGMENT_SHADER)
	println(VERTEX_SHADER)
}

private val implicitClass: Class<*> by lazy { Class.forName("io.github.epicvon2468.school.prac1.GLFWHouseKt") }

val FRAGMENT_SHADER: String by lazy { getResource("/shader.frag") }
val VERTEX_SHADER: String by lazy { getResource("/shader.vert") }

fun getResource(name: String): String = implicitClass.getResourceAsStream(name)!!.use(InputStream::readAllBytes).decodeToString()