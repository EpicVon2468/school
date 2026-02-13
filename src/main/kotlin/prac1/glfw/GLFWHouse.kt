package io.github.epicvon2468.school.prac1.glfw

import glad.gl_h

import org.glfw.glfw3_h.*

import java.io.InputStream

import java.lang.foreign.Arena
import java.lang.foreign.FunctionDescriptor
import java.lang.foreign.Linker
import java.lang.foreign.MemoryLayout
import java.lang.foreign.MemorySegment
import java.lang.foreign.SequenceLayout
import java.lang.foreign.ValueLayout
import java.lang.invoke.MethodHandle

import kotlin.system.exitProcess

val VEC2_LAYOUT: SequenceLayout = vecOf(2L)
val VEC3_LAYOUT: SequenceLayout = vecOf(3L)

fun vecOf(count: Long): SequenceLayout = MemoryLayout.sequenceLayout(count, C_FLOAT)

val verticesLayout: SequenceLayout = MemoryLayout.sequenceLayout(3L, Vertex.LAYOUT)
// https://dev.java/learn/ffm/access-structure/
// FIXME: Apparently I also gave up on Vertex in KMP_GE;  I'll have to use the other method for now, but hopefully I'll figure this out.
//val vertices: MemorySegment = Arena.global().allocate(verticesLayout).apply {
//	val vh: VarHandle = Vertex.LAYOUT.arrayElementVarHandle()
//	fun set(index: Long, value: Any) = vh.set(this, 0L, index, value)
//	val vertices = arrayOf(
//		Vertex(pos = arrayOf(-0.6f, -0.4f), col = arrayOf(1f, 0f, 0f)),
//		Vertex(pos = arrayOf(0.6f, -0.4f), col = arrayOf(0f, 1f, 0f)),
//		Vertex(pos = arrayOf(0f, 0.6f), col = arrayOf(0f, 0f, 1f))
//	)
//	set(0L, vertices[0].delegate)
//	set(1L, vertices[1].delegate)
//	set(2L, vertices[2].delegate)
//}
val vertices = Arena.global().allocateArray(
	vecOf(9L),
	0.0f, 0.5f, 0.0f,
	0.5f, -0.5f, 0.0f,
	-0.5f, -0.5f, 0.0f,
	is2DArray = true
)

// Bash: 'locate libGL'
// TODO: Add & use glad & see if there's a way to load the Vertex-style vertices properly.
// https://docs.oracle.com/en/java/javase/25/core/foreign-function-and-memory-api.html
// https://github.com/EpicVon2468/KMP_GE/blob/master/src/nativeMain/kotlin/io/github/epicvon2468/kmp_ge/core/main.kt
fun main() {
	NULL()
	Vertex(pos = arrayOf(1f, 2f), col = arrayOf(3f, 4f, 5f))
	println(FRAGMENT_SHADER)
	println(VERTEX_SHADER)
//	vertices
//	vecOf(9L).arrayElementVarHandle(MemoryLayout.PathElement.sequenceElement()).get(vertices, 0L, 4L, 0L).let {
//		println("Got it: $it")
//	}
	if (glfwInit() != 1) {
		println("ERROR - Failed to initialise GLFW!")
		exitProcess(1)
	}

	glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR(), 4)
	glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR(), 6)
	glfwWindowHint(GLFW_OPENGL_PROFILE(), GLFW_OPENGL_CORE_PROFILE())

	val window = glfwCreateWindow(640, 480, "My House".cstr(Arena.global()), MemorySegment.NULL, MemorySegment.NULL)
	if (window == MemorySegment.NULL) {
		println("ERROR - GLFWWindow failed to create!")
		glfwTerminate()
		exitProcess(1)
	}

	glfwMakeContextCurrent(window)
	if (gl_h.gladLoadGL(`glfwGetProcAddress$address`()) == 0) {
		println("ERROR - Glad failed to load GL!")
		glfwTerminate()
		exitProcess(1)
	}
	glfwSwapInterval(1)

	val linker: Linker = Linker.nativeLinker()
	val glEnable: MethodHandle = linker.downcallHandle(gl_h.glad_glEnable(), FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT))
	glEnable.invokeExact(GL_DEBUG_OUTPUT())
	glEnable.invokeExact(GL_DEBUG_OUTPUT_SYNCHRONOUS())

	while (glfwWindowShouldClose(window) != 1) {
		glfwSwapBuffers(window)
		glfwPollEvents()
	}

	glfwDestroyWindow(window)
	glfwTerminate()

	exitProcess(0)
}

private val implicitClass: Class<*> by lazy { Class.forName("io.github.epicvon2468.school.prac1.glfw.GLFWHouseKt") }

val FRAGMENT_SHADER: String by lazy { getResource("/shader.frag") }
val VERTEX_SHADER: String by lazy { getResource("/shader.vert") }

fun getResource(name: String): String = implicitClass.getResourceAsStream(name)!!.use(InputStream::readAllBytes).decodeToString()