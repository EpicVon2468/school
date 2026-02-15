package io.github.epicvon2468.school.prac1.glfw

import org.glfw.glfw3_h.*

import java.io.InputStream

import java.lang.foreign.Arena
import java.lang.foreign.MemoryLayout
import java.lang.foreign.MemorySegment
import java.lang.foreign.SequenceLayout

import kotlin.system.exitProcess

val VEC2_LAYOUT: SequenceLayout = vecOf(2L)
val VEC3_LAYOUT: SequenceLayout = vecOf(3L)

fun vecOf(count: Long): SequenceLayout = MemoryLayout.sequenceLayout(count, C_FLOAT)

val global: Arena = Arena.global()

// https://dev.java/learn/ffm/access-structure/
// https://antongerdelan.net/opengl/hellotriangle.html
//val vertices: MemorySegment = global.allocateArray(
//	C_FLOAT,
//	0.0f, 0.35f, 0.0f,
//	0.25f, 0.0f, 0.0f,
//	-0.25f, 0.0f, 0.0f
//)
//
//val vertices2: MemorySegment = global.allocateArray(
//	C_FLOAT,
//	0.0f, 0.5f, 0.0f,
//	0.5f, -0.5f, 0.0f,
//	-0.5f, -0.5f, 0.0f
//)

interface Shape {

	val vertexBuffer: MemorySegment
	val vertexArray: MemorySegment
	val vertices: MemorySegment
	val verticesCount: Long

	fun genVertexBuffer() {
		GL.genBuffers(1, vertexBuffer)
		GL.bindBuffer(GL_ARRAY_BUFFER(), vertexBuffer[GLuint, 0])
		GL.bufferData(GL_ARRAY_BUFFER(), verticesCount * C_FLOAT.byteSize(), vertices, GL_STATIC_DRAW())
	}

	fun genVertexArray() {
		GL.genVertexArrays(1, vertexArray)
		GL.bindVertexArray(vertexArray[GLuint, 0])
		GL.enableVertexAttribArray(0)
		GL.bindBuffer(GL_ARRAY_BUFFER(), vertexArray[GLuint, 0])
		GL.vertexAttribPointer(
			index = 0,
			size = 3,
			type = GL_FLOAT(),
			normalised = false,
			stride = 0,
			pointer = MemorySegment.NULL
		)
		GL.bindVertexArray(0)
	}

	fun draw() {
		GL.bindVertexArray(vertexArray[GLuint, 0])
		GL.drawArrays(mode = GL_TRIANGLES(), first = 0, count = (verticesCount / 3).toInt())
	}
}

data class Triangle(
	override val vertices: MemorySegment,
	override val verticesCount: Long
) : Shape {

	override val vertexBuffer: MemorySegment = global.allocate(GLuint)
	override val vertexArray: MemorySegment = global.allocate(GLuint)

	constructor(vararg vertices: Float) : this(global.allocateArray(C_FLOAT, *vertices.toTypedArray()), vertices.size.toLong())
}

data class Square(override val vertices: MemorySegment) : Shape {

	override val vertexBuffer: MemorySegment = global.allocate(GLuint)
	override val vertexArray: MemorySegment = global.allocate(GLuint)
	override val verticesCount: Long = 18

	// (x1, y1) -- (x2, y1)
	// |				|
	// |				|
	// (x1, y2) -- (x2, y2)
	constructor(x1: Float, y1: Float, x2: Float, y2: Float) : this(
		global.allocateArray(
			C_FLOAT,

			x1, y1, 0.0f, // top left
			x1, y2, 0.0f, // bottom left
			x2, y1, 0.0f, // top right

			x2, y1, 0.0f, // top right
			x1, y2, 0.0f, // bottom left
			x2, y2, 0.0f, // bottom right
		)
	)
}

// Bash: 'locate libGL'
// https://docs.oracle.com/en/java/javase/25/core/foreign-function-and-memory-api.html
// https://github.com/EpicVon2468/KMP_GE/blob/master/src/nativeMain/kotlin/io/github/epicvon2468/kmp_ge/core/main.kt
// https://docs.oracle.com/en/java/javase/25/core/upcalls-passing-java-code-function-pointer-foreign-function.html
// It's not stealing if it's my code that I'm 1:1 porting ;)
fun main() {
//	NULL()
//	Vertex(pos = arrayOf(1f, 2f), col = arrayOf(3f, 4f, 5f))
//	println(FRAGMENT_SHADER)
//	println(VERTEX_SHADER)

	GLFW.setErrorCallback { errorCode: Int, description: String? ->
		println("ERROR - GLFWErrorFun: (errorCode: '$errorCode', message: '$description')")
	}

	if (!GLFW.init()) {
		println("ERROR - Failed to initialise GLFW!")
		exitProcess(1)
	}

	glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR(), 4)
	glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR(), 6)
	glfwWindowHint(GLFW_OPENGL_PROFILE(), GLFW_OPENGL_CORE_PROFILE())
	val window = glfwCreateWindow(600, 600, "My House".cstr(global), MemorySegment.NULL, MemorySegment.NULL)
	if (window == MemorySegment.NULL) {
		println("ERROR - GLFWWindow failed to create!")
		glfwTerminate()
		exitProcess(1)
	}

	glfwMakeContextCurrent(window)
	if (!GL.loadGL(`glfwGetProcAddress$address`())) {
		println("ERROR - Glad failed to load GL!")
		glfwTerminate()
		exitProcess(1)
	}
	GLFW.setFramebufferSizeCallback(window) { _, width: Int, height: Int ->
		println("Called: ($width, $height)")
		GL.viewport(0, 0, width, height)
	}
	GLFW.swapInterval(1)

	GL.enable(GL_DEBUG_OUTPUT())
	GL.enable(GL_DEBUG_OUTPUT_SYNCHRONOUS())

	println("OpenGL shader language version: ${GL.getString(GL_SHADING_LANGUAGE_VERSION())}")

	GL.debugMessageCallback { source: Int, type: Int, id: Int, severity: Int, length: Int, message: String?, _ ->
		println("GL debug callback invoked! Debug info:")
		println("DebugProc {\n\tsource = $source,\n\ttype = $type,\n\tid = $id,\n\tseverity = $severity,\n\tlength = $length,\n\tmessage = '$message'\n}")
	}

	val triangle = Triangle(
		0.0f, 0.35f, 0.0f,
		0.25f, 0.0f, 0.0f,
		-0.25f, 0.0f, 0.0f
	)
	triangle.genVertexBuffer()
	triangle.genVertexArray()

	val triangle2 = Triangle(
		0.0f, 0.35f, 0.0f,
		0.5f, 1f, 0.0f,
		-0.5f, 1f, 0.0f
	)
	triangle2.genVertexBuffer()
	triangle2.genVertexArray()
	val square = Square(
		x1 = -1.0f, y1 = 1.0f,
		x2 = 1.0f, y2 = -1.0f
	)
	square.genVertexBuffer()
	square.genVertexArray()

	val vertexShader: Int = GL.createShader(GL_VERTEX_SHADER())
	if (vertexShader == 0) error("Could not create vertexShader!")
	GL.shaderSource(vertexShader, 1, VERTEX_SHADER)
	GL.compileShader(vertexShader)

	val fragmentShader: Int = GL.createShader(GL_FRAGMENT_SHADER())
	if (fragmentShader == 0) error("Could not create fragmentShader!")
	GL.shaderSource(fragmentShader, 1, FRAGMENT_SHADER)
	GL.compileShader(fragmentShader)

	val program: Int = GL.createProgram()
	GL.attachShader(program, fragmentShader)
	GL.attachShader(program, vertexShader)
	GL.linkProgram(program)

	val uTimeLocation: Int = GL.getUniformLocation(program, "time")
	if (uTimeLocation == -1) error("Couldn't get location of uniform 'time'!")

	// Do a first glViewport to fix alignment.
	Arena.ofShared().use { arena: Arena ->
		val width: MemorySegment = arena.allocate(C_INT)
		val height: MemorySegment = arena.allocate(C_INT)
		glfwGetFramebufferSize(window, width, height)
		GL.viewport(0, 0, width[C_INT, 0], height[C_INT, 0])
	}

	while (!GLFW.windowShouldClose(window)) {
		GL.clearColour(1f, 1f, 1f, 1f)
		GL.clear(GL_COLOR_BUFFER_BIT() or GL_DEPTH_BUFFER_BIT())

		GL.useProgram(program)
		GL.uniform1f(uTimeLocation, GLFW.getTime().toFloat())

		// https://learnopengl.com/code_viewer_gh.php?code=src/1.getting_started/2.2.hello_triangle_indexed/hello_triangle_indexed.cpp
		triangle.draw()
		triangle2.draw()
		square.draw()

		glfwSwapBuffers(window)
		// This cannot be at the start, or the JVM hits a segmentation fault in libwayland-client.so.0
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

//// Called by way of MethodHandle
//@Suppress("unused")
//fun framebufferSizeCallback(window: MemorySegment, width: Int, height: Int) {
//	println("Called: ($width, $height)")
//	GL.viewport(0, 0, width, height)
//}
//
//val frameBufferSizeCallback__handle: MethodHandle = MethodHandles.lookup().findStatic(
//	implicitClass,
//	"framebufferSizeCallback",
//	MethodType.fromMethodDescriptorString("(Ljava/lang/foreign/MemorySegment;II)V", implicitClass.classLoader)
//)
//
//val frameBufferSizeCallback__cptr: MemorySegment = linker.upcallStub(
//	frameBufferSizeCallback__handle,
//	FunctionDescriptor.ofVoid(C_POINTER, C_INT, C_INT),
//	global
//)