package io.github.epicvon2468.school.prac1.glfw

import glad.gl_h

import org.glfw.glfw3_h.`glfwGetProcAddress$address`
import org.glfw.glfw3_h.glfwTerminate

import java.lang.foreign.FunctionDescriptor
import java.lang.foreign.Linker
import java.lang.foreign.ValueLayout
import java.lang.invoke.MethodHandle

import kotlin.system.exitProcess

data object GL {

	init {
		if (gl_h.gladLoadGL(`glfwGetProcAddress$address`()) == 0) {
			println("ERROR - Glad failed to load GL!")
			glfwTerminate()
			exitProcess(1)
		}
	}

	private val linker: Linker = Linker.nativeLinker()

	private val glEnable: MethodHandle = linker.downcallHandle(
		gl_h.glad_glEnable(),
		FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT)
	)
	fun enable(p0: Int) = glEnable.invokeExact(p0) as Unit
}