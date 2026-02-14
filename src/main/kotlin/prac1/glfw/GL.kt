package io.github.epicvon2468.school.prac1.glfw

import glad.GLDEBUGPROC
import glad.gl_h.*

import java.lang.foreign.FunctionDescriptor
import java.lang.foreign.Linker
import java.lang.foreign.MemorySegment
import java.lang.invoke.MethodHandle

// TODO: Technically I could load the stuff out of libGL.so myself, thus removing the need for glad.  If I have time, switch to that instead.
data object GL {

	// Must be called first.
	/**
	 * `extern int gladLoadGL(GLADloadfunc load);`
	 */
	fun loadGL(load: MemorySegment): Int = gladLoadGL(load)

	private val linker: Linker = Linker.nativeLinker()

	/**
	 * `void glEnable(GLenum cap);`
	 */
	fun enable(cap: Int): Unit = glEnable.invokeExact(cap) as Unit
	// https://registry.khronos.org/OpenGL-Refpages/gl4/html/glEnable.xhtml
	private val glEnable: MethodHandle by lazy {
		linker.downcallHandle(glad_glEnable(), glEnable__descriptor)
	}
	val glEnable__descriptor: FunctionDescriptor = FunctionDescriptor.ofVoid(GLenum)

	/**
	 * `void glViewport(GLint x, GLint y, GLsizei width, GLsizei height);`
	 */
	fun viewport(x: Int, y: Int, width: Int, height: Int): Unit = glViewport.invokeExact(x, y, width, height) as Unit
	// https://registry.khronos.org/OpenGL-Refpages/gl4/html/glViewport.xhtml
	private val glViewport: MethodHandle by lazy {
		linker.downcallHandle(glad_glViewport(), glViewport__descriptor)
	}
	val glViewport__descriptor: FunctionDescriptor = FunctionDescriptor.ofVoid(
		GLint,
		GLint,
		GLsizei,
		GLsizei
	)

	/**
	 * `const GLubyte *glGetString(GLenum name);`
	 */
	fun getString(name: Int): String = (glGetString.invokeExact(name) as MemorySegment).getString(0)
	// https://registry.khronos.org/OpenGL-Refpages/gl4/html/glGetString.xhtml
	private val glGetString: MethodHandle by lazy {
		linker.downcallHandle(glad_glGetString(), glGetString__descriptor)
	}
	val glGetString__descriptor: FunctionDescriptor = FunctionDescriptor.of(
		C_POINTER,
		GLenum
	)

	// Parameter overloading doesn't work (like I want) here, since the last parameter isn't the lambda type.
	/**
	 * `void glDebugMessageCallback(DEBUGPROC callback, const void *userParam);`
	 */
	fun debugMessageCallback(callback: GLDebugProc) = debugMessageCallback(callback, MemorySegment.NULL)
	/**
	 * `void glDebugMessageCallback(DEBUGPROC callback, const void *userParam);`
	 */
	fun debugMessageCallback(callback: GLDebugProc, userParam: MemorySegment): Unit = glDebugMessageCallback.invokeExact(
		GLDEBUGPROC.allocate(
			/*fi =*/ { source: Int, type: Int, id: Int, severity: Int, length: Int, message: MemorySegment, userParam: MemorySegment ->
				callback(source, type, id, severity, length, message.jvmNull()?.getString(0), userParam)
			},
			/*arena =*/ global
		),
		userParam
	) as Unit
	// https://registry.khronos.org/OpenGL-Refpages/gl4/html/glDebugMessageCallback.xhtml
	private val glDebugMessageCallback: MethodHandle by lazy {
		linker.downcallHandle(glad_glDebugMessageCallback(), glDebugMessageCallback__descriptor)
	}
	val glDebugMessageCallback__descriptor: FunctionDescriptor = FunctionDescriptor.ofVoid(
		C_POINTER,
		C_POINTER
	)
	typealias GLDebugProc = (source: Int, type: Int, id: Int, severity: Int, length: Int, message: String?, userParam: MemorySegment) -> Unit

	/**
	 * `void glClear(GLbitfield mask);`
	 */
	fun clear(mask: Int): Unit = glClear.invokeExact(mask) as Unit
	// https://registry.khronos.org/OpenGL-Refpages/gl4/html/glClear.xhtml
	private val glClear: MethodHandle by lazy {
		linker.downcallHandle(glad_glClear(), glClear__descriptor)
	}
	val glClear__descriptor: FunctionDescriptor = FunctionDescriptor.ofVoid(
		GLbitfield
	)

	/**
	 * `void glClearColor(GLfloat red, GLfloat green, GLfloat blue, GLfloat alpha);`
	 */
	fun clearColour(red: Float, green: Float, blue: Float, alpha: Float): Unit = glClearColour.invokeExact(red, green, blue, alpha) as Unit
	// https://registry.khronos.org/OpenGL-Refpages/gl4/html/glClearColor.xhtml
	private val glClearColour: MethodHandle by lazy {
		linker.downcallHandle(glad_glClearColor(), glClearColour__descriptor)
	}
	val glClearColour__descriptor: FunctionDescriptor = FunctionDescriptor.ofVoid(
		GLfloat,
		GLfloat,
		GLfloat,
		GLfloat
	)

	/**
	 * `void glGenBuffers(GLsizei n, GLuint *buffers);`
	 */
	fun genBuffers(n: Int, buffers: MemorySegment): Unit = glGenBuffers.invokeExact(n, buffers) as Unit
	// https://registry.khronos.org/OpenGL-Refpages/gl4/html/glGenBuffers.xhtml
	private val glGenBuffers: MethodHandle by lazy {
		linker.downcallHandle(glad_glGenBuffers(), glGenBuffers__descriptor)
	}
	val glGenBuffers__descriptor: FunctionDescriptor = FunctionDescriptor.ofVoid(
		GLsizei,
		C_POINTER
	)

	/**
	 * `void glBindBuffer(GLenum target, GLuint buffer);`
	 */
	fun bindBuffer(target: Int, buffer: Int): Unit = glBindBuffer.invokeExact(target, buffer) as Unit
	// https://registry.khronos.org/OpenGL-Refpages/gl4/html/glBindBuffer.xhtml
	private val glBindBuffer: MethodHandle by lazy {
		linker.downcallHandle(glad_glBindBuffer(), glBindBuffer__descriptor)
	}
	val glBindBuffer__descriptor: FunctionDescriptor = FunctionDescriptor.ofVoid(
		GLenum,
		GLuint
	)
}