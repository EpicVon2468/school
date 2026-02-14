package io.github.epicvon2468.school.prac1.glfw

import glad.gl_h.*

import java.lang.foreign.FunctionDescriptor
import java.lang.foreign.Linker
import java.lang.foreign.MemorySegment
import java.lang.invoke.MethodHandle

// TODO: Technically I could load the stuff out of libGL.so myself, thus removing the need for glad.  If I have time, switch to that instead.
data object GL {

	// Must be called first.
	fun loadGL(p0: MemorySegment): Int = gladLoadGL(p0)

	private val linker: Linker = Linker.nativeLinker()

	// https://registry.khronos.org/OpenGL-Refpages/gl4/html/glEnable.xhtml
	private val glEnable: MethodHandle by lazy {
		linker.downcallHandle(
			glad_glEnable(),
			FunctionDescriptor.ofVoid(GLenum)
		)
	}
	fun enable(p0: Int) = glEnable.invokeExact(p0) as Unit

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
	fun viewport(p0: Int, p1: Int, p2: Int, p3: Int) = glViewport.invokeExact(p0, p1, p2, p3) as Unit
}