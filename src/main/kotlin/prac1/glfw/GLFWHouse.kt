package io.github.epicvon2468.school.prac1.glfw

import org.glfw.glfw3_h.*

import java.io.InputStream

import java.lang.foreign.Arena
import java.lang.foreign.MemorySegment

import kotlin.system.exitProcess

val global: Arena = Arena.global()

// https://dev.java/learn/ffm/access-structure/
// https://antongerdelan.net/opengl/hellotriangle.html
// Bash: 'locate libGL'
// https://docs.oracle.com/en/java/javase/25/core/foreign-function-and-memory-api.html
// https://github.com/EpicVon2468/KMP_GE/blob/master/src/nativeMain/kotlin/io/github/epicvon2468/kmp_ge/core/main.kt
// https://docs.oracle.com/en/java/javase/25/core/upcalls-passing-java-code-function-pointer-foreign-function.html
// It's not stealing if it's my code that I'm 1:1 porting ;)
fun main() {
	GLFW.setErrorCallback { errorCode: Int, description: String? ->
		println("ERROR - GLFWErrorFun: (errorCode: '$errorCode', message: '$description')")
	}

	if (!GLFW.init()) {
		println("ERROR - Failed to initialise GLFW!")
		exitProcess(1)
	}

	// Request a minimum OpenGL version & profile from GLFW
	// If the version is greater than the available OpenGL version, the error callback will be invoked & the JVM will crash
	GLFW.windowHint(GLFW_CONTEXT_VERSION_MAJOR(), 4)
	GLFW.windowHint(GLFW_CONTEXT_VERSION_MINOR(), 6)
	GLFW.windowHint(GLFW_OPENGL_PROFILE(), GLFW_OPENGL_CORE_PROFILE())

	// The 'GLFWWindow' type is simply a typealias I made for readability, it resolves to 'MemorySegment' (Foreign Functions & Memory API representation of C pointers & variables)
	val window: GLFWWindow = GLFW.createWindow(600, 600, "My (GLFW) House")
	if (window == NULL()) {
		println("ERROR - GLFWWindow failed to create!")
		glfwTerminate()
		exitProcess(1)
	}

	// I'll be honest, I have no idea what the 'context' is off the top of my head, I'll probably check that out sometime
	GLFW.makeContextCurrent(window)
	// When the window is resized, OpenGL needs to be informed of the new size, or else it'll still try to render compliant to the previous bounds
	GLFW.setFramebufferSizeCallback(window) { _, width: Int, height: Int ->
		println("Called: ($width, $height)")
		GL.viewport(0, 0, width, height)
	}
	// V-Sync. Self-explanatory.
	GLFW.swapInterval(1)

	// OpenGL doesn't actually give info or debug logs by default... I know because I spent three days trying to get it to log what was wrong with my shaders in a previous project
	GL.enable(GL_DEBUG_OUTPUT())
	GL.enable(GL_DEBUG_OUTPUT_SYNCHRONOUS())

	// https://wikis.khronos.org/opengl/Face_Culling
	GL.enable(GL_CULL_FACE())

	println("GLSL version: ${GL.getString(GL_SHADING_LANGUAGE_VERSION())}")
	println("OpenGL version: ${GL.getString(GL_VERSION())}")

	GL.debugMessageCallback { source: Int, type: Int, id: Int, severity: Int, length: Int, message: String?, _ ->
		println("GL debug callback invoked! Debug info:")
		println("DebugProc {\n\tsource = $source,\n\ttype = $type,\n\tid = $id,\n\tseverity = $severity,\n\tlength = $length,\n\tmessage = '$message'\n}")
	}

	val entries = Batch()

	// Roof
	Triangle(
		0.0f to 0.8f,
		-0.4f to 0.4f,
		0.4f to 0.4f
	).apply {
		colour = Colour(255.0f, 0.0f, 0.0f)
		entries.add(this)
	}
	// Chimney
	Square(
		-0.3f to 0.75f,
		-0.2f to 0.5f
	).apply {
		colour = Colour(0.0f, 0.0f, 0.0f)
		entries.add(this)
	}
	// Top of building
	Square(
		-0.4f to 0.4f,
		0.4f to 0.0f
	).apply {
		colour = BLUE
		entries.add(this)
	}
	// Bottom left square
	Square(
		-0.4f to 0.0f,
		-0.1f to -0.4f
	).apply {
		colour = BLUE
		entries.add(this)
	}
	// Bottom right square
	Square(
		0.1f to 0.0f,
		0.4f to -0.4f
	).apply {
		colour = BLUE
		entries.add(this)
	}
	Circle(
		basePoint = 0.6f to 0.8f,
		radius = 0.15f
	).apply(entries::add)

	// Many such cases: Vertex Buffer Object & Vertex Array Object
	for (shape: Shape in entries.shapes) {
		shape.genVertexBuffer()
		shape.genVertexArray()
	}

	// TODO: Check compile status of shaders & program
	val vertexShader: Int = GL.createShader(GL_VERTEX_SHADER())
	if (vertexShader == 0) error("Could not create vertexShader!")
	GL.shaderSource(vertexShader, 1, VERTEX_SHADER)
	GL.compileShader(vertexShader)
	checkError()

	val fragmentShader: Int = GL.createShader(GL_FRAGMENT_SHADER())
	if (fragmentShader == 0) error("Could not create fragmentShader!")
	GL.shaderSource(fragmentShader, 1, FRAGMENT_SHADER)
	GL.compileShader(fragmentShader)
	checkError()

	val program: Int = GL.createProgram()
	GL.attachShader(program, fragmentShader)
	GL.attachShader(program, vertexShader)
	GL.linkProgram(program)
	checkError()

	val uTimeLocation: Int = GL.getUniformLocation(program, "time")
	if (uTimeLocation == -1) error("Couldn't get location of uniform 'time'!")
	val uColourOverrideLocation: Int = GL.getUniformLocation(program, "colourOverride")
	if (uColourOverrideLocation == -1) error("Couldn't get location of uniform 'colourOverride'!")

	// Horrific sin, needs to be fixed
	entries.shapes.forEach { shape: Shape -> shape.colourOverrideLocation = uColourOverrideLocation }

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
		// Set the value of the 'time' uniform in shader.frag
		GL.uniform1f(uTimeLocation, GLFW.getTime().toFloat())

		// https://learnopengl.com/code_viewer_gh.php?code=src/1.getting_started/2.2.hello_triangle_indexed/hello_triangle_indexed.cpp
		entries.draw()

		// Swap out the new content onto the screen
		GLFW.swapBuffers(window)
		// This cannot be at the start, or the JVM hits a segmentation fault in libwayland-client.so.0
		GLFW.pollEvents()
	}

	GLFW.destroyWindow(window)
	GLFW.terminate()

	exitProcess(0)
}

fun checkError() {
	val message: String = when (val error: GLEnum = GL.getError()) {
		GL_NO_ERROR() -> return
		GL_INVALID_ENUM() -> "INVALID ENUM"
		GL_INVALID_VALUE() -> "INVALID VALUE"
		GL_INVALID_OPERATION() -> "INVALID OPERATION"
		GL_INVALID_FRAMEBUFFER_OPERATION() -> "INVALID FRAMEBUFFER OPERATION"
		GL_OUT_OF_MEMORY() -> "OUT OF MEMORY"
		GL_STACK_UNDERFLOW() -> "STACK UNDERFLOW"
		GL_STACK_OVERFLOW() -> "STACK OVERFLOW"
		else -> error("Unknown OpenGL error code: '$error'!")
	}
	error("OpenGL errored: $message")
}

val BLUE: Colour = Colour(0.0f, 0.0f, 255.0f)

// Can't use 'this::class.java', there is no explicit 'this' here
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