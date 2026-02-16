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

	GLFW.windowHint(GLFW_CONTEXT_VERSION_MAJOR(), 4)
	GLFW.windowHint(GLFW_CONTEXT_VERSION_MINOR(), 6)
	GLFW.windowHint(GLFW_OPENGL_PROFILE(), GLFW_OPENGL_CORE_PROFILE())
	val window = GLFW.createWindow(600, 600, "My House")
	if (window == NULL()) {
		println("ERROR - GLFWWindow failed to create!")
		glfwTerminate()
		exitProcess(1)
	}

	GLFW.makeContextCurrent(window)
	if (!GL.load()) {
		println("ERROR - Failed to load GL!")
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

	val entries: MutableList<Shape> = mutableListOf()

	// Roof
	Triangle(
		0.0f to 0.8f,
		-0.4f to 0.4f,
		0.4f to 0.4f
	).apply {
		colourOverride = Triple(255.0f, 0.0f, 0.0f)
		entries.add(this)
	}
	// Chimney
	Square(
		-0.3f to 0.75f,
		-0.2f to 0.5f
	).apply {
		colourOverride = Triple(0.0f, 0.0f, 0.0f)
		entries.add(this)
	}
	// Top of building
	Square(
		-0.4f to 0.4f,
		0.4f to 0.0f
	).apply {
		colourOverride = BLUE
		entries.add(this)
	}
	// Bottom left square
	Square(
		-0.4f to 0.0f,
		-0.1f to -0.4f
	).apply {
		colourOverride = BLUE
		entries.add(this)
	}
	// Bottom right square
	Square(
		0.1f to 0.0f,
		0.4f to -0.4f
	).apply {
		colourOverride = BLUE
		entries.add(this)
	}

	for (shape: Shape in entries) {
		shape.genVertexBuffer()
		shape.genVertexArray()
	}

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
	val uColourOverrideLocation: Int = GL.getUniformLocation(program, "colourOverride")
	if (uColourOverrideLocation == -1) error("Couldn't get location of uniform 'colourOverride'!")

	entries.forEach { it.colourOverrideLocation = uColourOverrideLocation }

	// Do a first glViewport to fix alignment.
	Arena.ofShared().use { arena: Arena ->
		val width: MemorySegment = arena.allocate(C_INT)
		val height: MemorySegment = arena.allocate(C_INT)
		GLFW.getFramebufferSize(window, width, height)
		GL.viewport(0, 0, width[C_INT, 0], height[C_INT, 0])
	}

	while (!GLFW.windowShouldClose(window)) {
		GL.clearColour(1f, 1f, 1f, 1f)
		GL.clear(GL_COLOR_BUFFER_BIT() or GL_DEPTH_BUFFER_BIT())

		GL.useProgram(program)
		GL.uniform1f(uTimeLocation, GLFW.getTime().toFloat())

		// https://learnopengl.com/code_viewer_gh.php?code=src/1.getting_started/2.2.hello_triangle_indexed/hello_triangle_indexed.cpp
		entries.forEach(Shape::draw)

		GLFW.swapBuffers(window)
		// This cannot be at the start, or the JVM hits a segmentation fault in libwayland-client.so.0
		GLFW.pollEvents()
	}

	GLFW.destroyWindow(window)
	GLFW.terminate()

	exitProcess(0)
}

val COLOUR_UNSET: Triple<Float, Float, Float> = Triple(-1.0f, -1.0f, -1.0f)
val BLUE: Triple<Float, Float, Float> = Triple(0.0f, 0.0f, 255.0f)

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