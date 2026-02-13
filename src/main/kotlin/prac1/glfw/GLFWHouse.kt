package io.github.epicvon2468.school.prac1.glfw

import org.glfw.glfw3_h.*

import java.io.InputStream

import java.lang.foreign.Arena
import java.lang.foreign.MemoryLayout
import java.lang.foreign.MemorySegment
import java.lang.foreign.SequenceLayout
import java.lang.invoke.VarHandle

val VEC2_LAYOUT: SequenceLayout = vecOf(2L)
val VEC3_LAYOUT: SequenceLayout = vecOf(3L)

fun vecOf(count: Long): SequenceLayout = MemoryLayout.sequenceLayout(count, C_FLOAT)

val verticesLayout: SequenceLayout = MemoryLayout.sequenceLayout(3L, Vertex.LAYOUT)
// https://dev.java/learn/ffm/access-structure/
val vertices: MemorySegment = Arena.global().allocate(verticesLayout).apply {
	val vh: VarHandle = Vertex.LAYOUT.arrayElementVarHandle()
	fun set(index: Long, value: Any) = vh.set(this, 0L, index, value)
	val vertices = arrayOf(
		Vertex(pos = arrayOf(-0.6f, -0.4f), col = arrayOf(1f, 0f, 0f)),
		Vertex(pos = arrayOf(0.6f, -0.4f), col = arrayOf(0f, 1f, 0f)),
		Vertex(pos = arrayOf(0f, 0.6f), col = arrayOf(0f, 0f, 1f))
	)
	set(0L, vertices[0].delegate)
	set(1L, vertices[1].delegate)
	set(2L, vertices[2].delegate)
}

// https://docs.oracle.com/en/java/javase/25/core/foreign-function-and-memory-api.html
fun main() {
	NULL()
	Vertex(pos = arrayOf(1f, 2f), col = arrayOf(3f, 4f, 5f))
	println(FRAGMENT_SHADER)
	println(VERTEX_SHADER)
}

private val implicitClass: Class<*> by lazy { Class.forName("io.github.epicvon2468.school.prac1.glfw.GLFWHouseKt") }

val FRAGMENT_SHADER: String by lazy { getResource("/shader.frag") }
val VERTEX_SHADER: String by lazy { getResource("/shader.vert") }

fun getResource(name: String): String = implicitClass.getResourceAsStream(name)!!.use(InputStream::readAllBytes).decodeToString()