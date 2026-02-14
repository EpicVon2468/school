package io.github.epicvon2468.school.prac1.glfw

import org.glfw.GLFWerrorfun
import org.glfw.GLFWframebuffersizefun
import org.glfw.glfw3_h.*

import java.lang.foreign.MemorySegment

data object GLFW {

	/**
	 * `int glfwInit(void);`
	 */
	fun init(): Boolean = glfwInit() != GLFW_FALSE()

	/**
	 * `double glfwGetTime(void);`
	 */
	fun getTime(): Double = glfwGetTime()

	/**
	 * `int glfwWindowShouldClose(GLFWwindow *window);`
	 */
	fun windowShouldClose(window: MemorySegment): Boolean {
		if (window == MemorySegment.NULL) throw IllegalArgumentException("NULL was passed to GLFW.windowShouldClose()!")
		return glfwWindowShouldClose(window) != GLFW_FALSE()
	}

	/**
	 * `GLFWerrorfun glfwSetErrorCallback(GLFWerrorfun callback);`
	 */
	fun setErrorCallback(callback: GLFWErrorFun?): GLFWErrorFun? {
		fun MemorySegment.asFunction(): GLFWErrorFun? = if (this == MemorySegment.NULL) null else { errorCode: Int, description: String? ->
			GLFWerrorfun.invoke(this, errorCode, description.cstr(global))
		}
		callback ?: return glfwSetErrorCallback(MemorySegment.NULL).asFunction()
		return glfwSetErrorCallback(
			GLFWerrorfun.allocate(
				/*fi =*/ { errorCode: Int, description: MemorySegment ->
					callback(errorCode, description.jvmNull()?.getString(0))
				},
				/*arena =*/ global
			)
		).asFunction()
	}
	typealias GLFWErrorFun = (errorCode: Int, description: String?) -> Unit

	/**
	 * `GLFWframebuffersizefun glfwSetFramebufferSizeCallback(GLFWwindow* window, GLFWframebuffersizefun callback);`
	 */
	fun setFramebufferSizeCallback(window: MemorySegment, callback: GLFWFramebufferSizeFun?): GLFWFramebufferSizeFun? {
		fun MemorySegment.asFunction(): GLFWFramebufferSizeFun? = if (this == MemorySegment.NULL) null else { window: MemorySegment, width: Int, height: Int ->
			GLFWframebuffersizefun.invoke(this, window, width, height)
		}
		callback ?: return glfwSetFramebufferSizeCallback(window, MemorySegment.NULL).asFunction()
		return glfwSetFramebufferSizeCallback(
			window,
			GLFWframebuffersizefun.allocate(
				/*fi =*/ { window: MemorySegment, width: Int, height: Int -> callback(window, width, height) },
				/*arena =*/ global
			)
		).asFunction()
	}
	typealias GLFWFramebufferSizeFun = (window: MemorySegment, width: Int, height: Int) -> Unit
}