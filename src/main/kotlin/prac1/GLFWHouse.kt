package io.github.epicvon2468.school.prac1

import org.glfw.glfw3_h.*

import java.io.InputStream

fun main() {
	NULL()
	println(FRAGMENT_SHADER)
	println(VERTEX_SHADER)
}

private val implicitClass: Class<*> by lazy { Class.forName("io.github.epicvon2468.school.prac1.GLFWHouseKt") }

val FRAGMENT_SHADER: String by lazy { getResource("/shader.frag") }
val VERTEX_SHADER: String by lazy { getResource("/shader.vert") }

fun getResource(name: String): String = implicitClass.getResourceAsStream(name)!!.use(InputStream::readAllBytes).decodeToString()