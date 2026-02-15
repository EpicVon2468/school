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
	 * `void glfwTerminate(void);`
	 */
	fun terminate(): Unit = glfwTerminate()

	/**
	 * `double glfwGetTime(void);`
	 */
	fun getTime(): Double = glfwGetTime()

	/**
	 * `void swapInterval(int interval);`
	 */
	fun swapInterval(interval: Int): Unit = glfwSwapInterval(interval)

	/**
	 * `int glfwWindowShouldClose(GLFWwindow *window);`
	 */
	fun windowShouldClose(window: MemorySegment): Boolean {
		if (window == MemorySegment.NULL) throw IllegalArgumentException("NULL was passed to GLFW.windowShouldClose()!")
		return glfwWindowShouldClose(window) != GLFW_FALSE()
	}

	/**
	 * `void glfwWindowHint(int hint, int value);`
	 */
	fun windowHint(hint: Int, value: Int): Unit = glfwWindowHint(hint, value)

	/**
	 * `GLFWwindow *glfwCreateWindow(int width, int height, const char *title, GLFWmonitor *monitor, GLFWwindow *share);`
	 */
	fun createWindow(
		width: Int,
		height: Int,
		title: String,
		monitor: MemorySegment,
		share: MemorySegment
	): MemorySegment = glfwCreateWindow(width, height, title.cstr(global), monitor, share)

	/**
	 * `void glfwMakeContextCurrent(GLFWwindow *window);`
	 */
	fun makeContextCurrent(window: MemorySegment): Unit = glfwMakeContextCurrent(window)

	/**
	 * `void glfwDestroyWindow(GLFWwindow *window);`
	 */
	fun destroyWindow(window: MemorySegment): Unit = glfwDestroyWindow(window)

	/**
	 * `void glfwGetFramebufferSize(GLFWwindow *window, int *width, int *height);`
	 */
	fun getFramebufferSize(window: MemorySegment, width: MemorySegment, height: MemorySegment): Unit = glfwGetFramebufferSize(window, width, height)

	/**
	 * `void glfwSwapBuffers(GLFWwindow *window);`
	 */
	fun swapBuffers(window: MemorySegment): Unit = glfwSwapBuffers(window)

	/**
	 * `void glfwPollEvents(void);`
	 */
	fun pollEvents(): Unit = glfwPollEvents()

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