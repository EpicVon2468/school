package io.github.epicvon2468.school.prac1.glfw

import org.glfw.glfw3_h.*

import java.io.InputStream

import java.lang.foreign.Arena
import java.lang.foreign.MemoryLayout
import java.lang.foreign.MemorySegment
import java.lang.foreign.SequenceLayout

val VEC2_LAYOUT: SequenceLayout = vecOf(2L)
val VEC3_LAYOUT: SequenceLayout = vecOf(3L)

fun vecOf(count: Long): SequenceLayout = MemoryLayout.sequenceLayout(count, C_FLOAT)

// https://dev.java/learn/ffm/access-structure/
//val vertices: MemorySegment = Arena.global().allocate(Vertex.LAYOUT).apply {
//}

fun main() {
	NULL()
	Vertex.LAYOUT__POS
	Arena.ofShared().use {
		Vertex(pos = 1f to 2f, col = Triple(3f, 4f, 5f))
	}
	println(FRAGMENT_SHADER)
	println(VERTEX_SHADER)
}

private val implicitClass: Class<*> by lazy { Class.forName("io.github.epicvon2468.school.prac1.glfw.GLFWHouseKt") }

val FRAGMENT_SHADER: String by lazy { getResource("/shader.frag") }
val VERTEX_SHADER: String by lazy { getResource("/shader.vert") }

fun getResource(name: String): String = implicitClass.getResourceAsStream(name)!!.use(InputStream::readAllBytes).decodeToString()