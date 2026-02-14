package io.github.epicvon2468.school.prac1.glfw

import glad.gl_h.*

import java.lang.foreign.FunctionDescriptor
import java.lang.foreign.Linker
import java.lang.foreign.MemorySegment
import java.lang.foreign.ValueLayout
import java.lang.invoke.MethodHandle

// TODO: Technically I could load the stuff out of libGL.so myself, thus removing the need for glad.  If I have time, switch to that instead.
data object GL {

	// Must be called first.
	fun loadGL(p0: MemorySegment): Int = gladLoadGL(p0)

	private val linker: Linker = Linker.nativeLinker()

	private val glEnable: MethodHandle = linker.downcallHandle(
		glad_glEnable(),
		FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT)
	)
	fun enable(p0: Int) = glEnable.invokeExact(p0) as Unit
}