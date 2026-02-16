package io.github.epicvon2468.school.prac1.glfw

import org.glfw.GLDEBUGPROC
import org.glfw.glfw3_h.*

import java.lang.foreign.FunctionDescriptor
import java.lang.foreign.Linker
import java.lang.foreign.MemorySegment
import java.lang.invoke.MethodHandle

data object GL {

	private fun addressOf(name: String): GLFWGLProc = GLFW.getProcAddress(name)

	private val linker: Linker = Linker.nativeLinker()

	/**
	 * `void glEnable(GLenum cap);`
	 */
	fun enable(cap: GLEnum): Unit = glEnable.invokeExact(cap) as Unit
	// https://registry.khronos.org/OpenGL-Refpages/gl4/html/glEnable.xhtml
	private val glEnable: MethodHandle by lazy {
		linker.downcallHandle(addressOf("glEnable"), glEnable__descriptor)
	}
	val glEnable__descriptor: FunctionDescriptor = FunctionDescriptor.ofVoid(
		GLenum
	)

	/**
	 * `void glViewport(GLint x, GLint y, GLsizei width, GLsizei height);`
	 */
	fun viewport(x: Int, y: Int, width: Int, height: Int): Unit = glViewport.invokeExact(x, y, width, height) as Unit
	// https://registry.khronos.org/OpenGL-Refpages/gl4/html/glViewport.xhtml
	private val glViewport: MethodHandle by lazy {
		linker.downcallHandle(addressOf("glViewport"), glViewport__descriptor)
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
	fun getString(name: GLEnum): String = (glGetString.invokeExact(name) as MemorySegment).getString(0)
	// https://registry.khronos.org/OpenGL-Refpages/gl4/html/glGetString.xhtml
	private val glGetString: MethodHandle by lazy {
		linker.downcallHandle(addressOf("glGetString"), glGetString__descriptor)
	}
	val glGetString__descriptor: FunctionDescriptor = FunctionDescriptor.of(
		C_POINTER,
		GLenum
	)

	// Parameter overloading doesn't work (like I want) here, since the last parameter isn't the lambda type.
	/**
	 * `void glDebugMessageCallback(DEBUGPROC callback, const void *userParam);`
	 */
	fun debugMessageCallback(callback: GLDebugProc) = debugMessageCallback(callback, NULL())
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
		linker.downcallHandle(addressOf("glDebugMessageCallback"), glDebugMessageCallback__descriptor)
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
		linker.downcallHandle(addressOf("glClear"), glClear__descriptor)
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
		linker.downcallHandle(addressOf("glClearColor"), glClearColour__descriptor)
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
		linker.downcallHandle(addressOf("glGenBuffers"), glGenBuffers__descriptor)
	}
	val glGenBuffers__descriptor: FunctionDescriptor = FunctionDescriptor.ofVoid(
		GLsizei,
		C_POINTER
	)

	/**
	 * `void glBindBuffer(GLenum target, GLuint buffer);`
	 */
	fun bindBuffer(target: GLEnum, buffer: Int): Unit = glBindBuffer.invokeExact(target, buffer) as Unit
	// https://registry.khronos.org/OpenGL-Refpages/gl4/html/glBindBuffer.xhtml
	private val glBindBuffer: MethodHandle by lazy {
		linker.downcallHandle(addressOf("glBindBuffer"), glBindBuffer__descriptor)
	}
	val glBindBuffer__descriptor: FunctionDescriptor = FunctionDescriptor.ofVoid(
		GLenum,
		GLuint
	)

	/**
	 * `void glBufferData(GLenum target, GLsizeiptr size, const void *data, GLenum usage);`
	 */
	fun bufferData(target: GLEnum, size: Long, data: MemorySegment, usage: GLEnum): Unit = glBufferData.invokeExact(target, size, data, usage) as Unit
	// https://registry.khronos.org/OpenGL-Refpages/gl4/html/glBufferData.xhtml
	private val glBufferData: MethodHandle by lazy {
		linker.downcallHandle(addressOf("glBufferData"), glBufferData__descriptor)
	}
	val glBufferData__descriptor: FunctionDescriptor = FunctionDescriptor.ofVoid(
		GLuint,
		GLsizeiptr,
		C_POINTER,
		GLenum
	)

	/**
	 * `void glGenVertexArrays(GLsizei n, GLuint *arrays);`
	 */
	fun genVertexArrays(n: Int, arrays: MemorySegment): Unit = glGenVertexArrays.invokeExact(n, arrays) as Unit
	// https://registry.khronos.org/OpenGL-Refpages/gl4/html/glGenVertexArrays.xhtml
	private val glGenVertexArrays: MethodHandle by lazy {
		linker.downcallHandle(addressOf("glGenVertexArrays"), glGenVertexArrays__descriptor)
	}
	val glGenVertexArrays__descriptor: FunctionDescriptor = FunctionDescriptor.ofVoid(
		GLsizei,
		C_POINTER
	)

	/**
	 * `void glBindVertexArray(array: GLuint);`
	 */
	fun bindVertexArray(array: Int): Unit = glBindVertexArray.invokeExact(array) as Unit
	// https://registry.khronos.org/OpenGL-Refpages/gl4/html/glBindVertexArray.xhtml
	private val glBindVertexArray: MethodHandle by lazy {
		linker.downcallHandle(addressOf("glBindVertexArray"), glBindVertexArray__descriptor)
	}
	val glBindVertexArray__descriptor: FunctionDescriptor = FunctionDescriptor.ofVoid(
		GLuint
	)

	/**
	 * `void glEnableVertexAttribArray(GLuint index);`
	 */
	fun enableVertexAttribArray(index: Int): Unit = glEnableVertexAttribArray.invokeExact(index) as Unit
	// https://registry.khronos.org/OpenGL-Refpages/gl4/html/glEnableVertexAttribArray.xhtml
	private val glEnableVertexAttribArray: MethodHandle by lazy {
		linker.downcallHandle(addressOf("glEnableVertexAttribArray"), glEnableVertexAttribArray__descriptor)
	}
	val glEnableVertexAttribArray__descriptor: FunctionDescriptor = FunctionDescriptor.ofVoid(
		GLuint
	)

	/**
	 * `void glVertexAttribPointer(GLuint index, GLint size, GLenum type, GLboolean normalized, GLsizei stride, const void *pointer);`
	 */
	fun vertexAttribPointer(
		index: Int,
		size: Int,
		type: GLEnum,
		normalised: Boolean,
		stride: Int,
		pointer: MemorySegment
	): Unit = glVertexAttribPointer.invokeExact(
		index,
		size,
		type,
		(if (normalised) GL_TRUE() else GL_FALSE()).toByte(),
		stride,
		pointer
	) as Unit
	// https://registry.khronos.org/OpenGL-Refpages/gl4/html/glVertexAttribPointer.xhtml
	private val glVertexAttribPointer: MethodHandle by lazy {
		linker.downcallHandle(addressOf("glVertexAttribPointer"), glVertexAttribPointer__descriptor)
	}
	val glVertexAttribPointer__descriptor: FunctionDescriptor = FunctionDescriptor.ofVoid(
		GLuint,
		GLint,
		GLenum,
		GLboolean,
		GLsizei,
		C_POINTER
	)

	/**
	 * `GLuint glCreateShader(GLenum shaderType);`
	 */
	fun createShader(shaderType: GLEnum): Int = glCreateShader.invokeExact(shaderType) as Int
	// https://registry.khronos.org/OpenGL-Refpages/gl4/html/glCreateShader.xhtml
	private val glCreateShader: MethodHandle by lazy {
		linker.downcallHandle(addressOf("glCreateShader"), glCreateShader__descriptor)
	}
	val glCreateShader__descriptor: FunctionDescriptor = FunctionDescriptor.of(
		GLuint,
		GLenum
	)

	/**
	 * `void glShaderSource(GLuint shader, GLsizei count, const GLchar **string, const GLint *length);`
	 */
	fun shaderSource(shader: Int, count: Int, string: String, length: Int? = null) = shaderSource(shader, count, listOf(string), length?.let(::listOf))
	/**
	 * `void glShaderSource(GLuint shader, GLsizei count, const GLchar **string, const GLint *length);`
	 */
	fun shaderSource(shader: Int, count: Int, strings: List<String>, lengths: List<Int>?): Unit = glShaderSource.invokeExact(
		shader,
		count,
		global.allocateArray(C_POINTER, *strings.map { it.cstr(global) }.toTypedArray()),
		lengths?.toTypedArray()?.let {
			global.allocateArray(GLint, *it)
		} ?: NULL()
	) as Unit
	// https://registry.khronos.org/OpenGL-Refpages/gl4/html/glShaderSource.xhtml
	private val glShaderSource: MethodHandle by lazy {
		linker.downcallHandle(addressOf("glShaderSource"), glShaderSource__descriptor)
	}
	val glShaderSource__descriptor: FunctionDescriptor = FunctionDescriptor.ofVoid(
		GLuint,
		GLsizei,
		C_POINTER,
		C_POINTER
	)

	/**
	 * `void glCompileShader(GLuint shader);`
	 */
	fun compileShader(shader: Int): Unit = glCompileShader.invokeExact(shader) as Unit
	// https://registry.khronos.org/OpenGL-Refpages/gl4/html/glCompileShader.xhtml
	private val glCompileShader: MethodHandle by lazy {
		linker.downcallHandle(addressOf("glCompileShader"), glCompileShader__descriptor)
	}
	val glCompileShader__descriptor: FunctionDescriptor = FunctionDescriptor.ofVoid(
		GLuint
	)

	/**
	 * `GLuint glCreateProgram(void);`
	 */
	fun createProgram(): Int = glCreateProgram.invokeExact() as Int
	// https://registry.khronos.org/OpenGL-Refpages/gl4/html/glCreateProgram.xhtml
	private val glCreateProgram: MethodHandle by lazy {
		linker.downcallHandle(addressOf("glCreateProgram"), glCreateProgram__descriptor)
	}
	val glCreateProgram__descriptor: FunctionDescriptor = FunctionDescriptor.of(
		GLuint
	)

	/**
	 * `void glAttachShader(GLuint program, GLuint shader);`
	 */
	fun attachShader(program: Int, shader: Int): Unit = glAttachShader.invokeExact(program, shader) as Unit
	// https://registry.khronos.org/OpenGL-Refpages/gl4/html/glAttachShader.xhtml
	private val glAttachShader: MethodHandle by lazy {
		linker.downcallHandle(addressOf("glAttachShader"), glAttachShader__descriptor)
	}
	val glAttachShader__descriptor: FunctionDescriptor = FunctionDescriptor.ofVoid(
		GLuint,
		GLuint
	)

	/**
	 * `void glLinkProgram(GLuint program);`
	 */
	fun linkProgram(program: Int): Unit = glLinkProgram.invokeExact(program) as Unit
	// https://registry.khronos.org/OpenGL-Refpages/gl4/html/glLinkProgram.xhtml
	private val glLinkProgram: MethodHandle by lazy {
		linker.downcallHandle(addressOf("glLinkProgram"), glLinkProgram__descriptor)
	}
	val glLinkProgram__descriptor: FunctionDescriptor = FunctionDescriptor.ofVoid(
		GLuint
	)

	/**
	 * `GLint glGetUniformLocation(GLuint program, const GLchar *name);`
	 */
	fun getUniformLocation(program: Int, name: String): Int = glGetUniformLocation.invokeExact(program, name.cstr(global)) as Int
	// https://registry.khronos.org/OpenGL-Refpages/gl4/html/glGetUniformLocation.xhtml
	private val glGetUniformLocation: MethodHandle by lazy {
		linker.downcallHandle(addressOf("glGetUniformLocation"), glGetUniformLocation__descriptor)
	}
	val glGetUniformLocation__descriptor: FunctionDescriptor = FunctionDescriptor.of(
		GLint,
		GLuint,
		C_POINTER
	)

	/**
	 * `void glUseProgram(GLuint program);`
	 */
	fun useProgram(program: Int): Unit = glUseProgram.invokeExact(program) as Unit
	// https://registry.khronos.org/OpenGL-Refpages/gl4/html/glUseProgram.xhtml
	private val glUseProgram: MethodHandle by lazy {
		linker.downcallHandle(addressOf("glUseProgram"), glUseProgram__descriptor)
	}
	val glUseProgram__descriptor: FunctionDescriptor = FunctionDescriptor.ofVoid(
		GLuint
	)

	/**
	 * `void glUniform1f(GLint location, GLfloat v0);`
	 */
	fun uniform1f(location: Int, v0: Float): Unit = glUniform1f.invokeExact(location, v0) as Unit
	// https://registry.khronos.org/OpenGL-Refpages/gl4/html/glUniform.xhtml
	private val glUniform1f: MethodHandle by lazy {
		linker.downcallHandle(addressOf("glUniform1f"), glUniform1f__descriptor)
	}
	val glUniform1f__descriptor: FunctionDescriptor = FunctionDescriptor.ofVoid(
		GLint,
		GLfloat
	)

	/**
	 * `void glUniform3fv(GLint location, GLsize count, const GLfloat *value);`
	 */
	fun uniform3fv(location: Int, count: Int, value: Triple<Float, Float, Float>): Unit = uniform3fv(location, count, value.toList())
	/**
	 * `void glUniform3fv(GLint location, GLsize count, const GLfloat *value);`
	 */
	fun uniform3fv(location: Int, count: Int, value: List<Float>): Unit = glUniform3fv.invokeExact(
		location,
		count,
		global.allocateArray(GLfloat, *value.toTypedArray())
	) as Unit
	// https://registry.khronos.org/OpenGL-Refpages/gl4/html/glUniform.xhtml
	private val glUniform3fv: MethodHandle by lazy {
		linker.downcallHandle(addressOf("glUniform3fv"), glUniform3fv__descriptor)
	}
	val glUniform3fv__descriptor: FunctionDescriptor = FunctionDescriptor.ofVoid(
		GLint,
		GLsizei,
		C_POINTER
	)

	/**
	 * `void glDrawArrays(GLenum mode, GLint first, GLsizei count);`
	 */
	fun drawArrays(mode: GLEnum, first: Int, count: Int): Unit = glDrawArrays.invokeExact(mode, first, count) as Unit
	// https://registry.khronos.org/OpenGL-Refpages/gl4/html/glDrawArrays.xhtml
	private val glDrawArrays: MethodHandle by lazy {
		linker.downcallHandle(addressOf("glDrawArrays"), glDrawArrays__descriptor)
	}
	val glDrawArrays__descriptor: FunctionDescriptor = FunctionDescriptor.ofVoid(
		GLenum,
		GLint,
		GLsizei
	)
}