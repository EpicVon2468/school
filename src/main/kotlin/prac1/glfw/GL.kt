package io.github.epicvon2468.school.prac1.glfw

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
		linker.downcallHandle(
			glad_glEnable(),
			FunctionDescriptor.ofVoid(GLenum)
		)
	}

	/**
	 * `void glViewport(GLint x, GLint y, GLsizei width, GLsizei height);`
	 */
	fun viewport(x: Int, y: Int, width: Int, height: Int): Unit = glViewport.invokeExact(x, y, width, height) as Unit
	// https://registry.khronos.org/OpenGL-Refpages/gl4/html/glViewport.xhtml
	private val glViewport: MethodHandle by lazy {
		linker.downcallHandle(
			glad_glViewport(),
			FunctionDescriptor.ofVoid(
				GLint,
				GLint,
				GLsizei,
				GLsizei
			)
		)
	}

	/**
	 * `const GLubyte *glGetString(GLenum name);`
	 */
	fun getString(name: Int): String = (glGetString.invokeExact(name) as MemorySegment).getString(0)
	// https://registry.khronos.org/OpenGL-Refpages/gl4/html/glGetString.xhtml
	private val glGetString: MethodHandle by lazy {
		linker.downcallHandle(
			glad_glGetString(),
			FunctionDescriptor.of(
				C_POINTER,
				GLenum
			)
		)
	}
}