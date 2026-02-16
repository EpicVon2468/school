package io.github.epicvon2468.school.prac1.glfw

import org.glfw.GLFWerrorfun
import org.glfw.GLFWframebuffersizefun
import org.glfw.glfw3_h.*

import java.lang.foreign.MemorySegment

data object GLFW {

	/**
	 * `int glfwInit(void);`
	 */
	fun init(): Boolean = glfwInit() != 0

	/**
	 * `void glfwTerminate(void);`
	 */
	fun terminate(): Unit = glfwTerminate()

	/**
	 * `GLFWglproc glfwGetProcAddress(const char *procname);`
	 */
	fun getProcAddress(procname: String): GLFWGLProc = glfwGetProcAddress(procname.cstr(global))

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
	fun windowShouldClose(window: GLFWWindow): Boolean = glfwWindowShouldClose(window) != 0

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
		monitor: GLFWMonitor = NULL(),
		share: GLFWWindow = NULL()
	): GLFWWindow = glfwCreateWindow(width, height, title.cstr(global), monitor, share)

	/**
	 * `void glfwMakeContextCurrent(GLFWwindow *window);`
	 */
	fun makeContextCurrent(window: GLFWWindow): Unit = glfwMakeContextCurrent(window)

	/**
	 * `void glfwDestroyWindow(GLFWwindow *window);`
	 */
	fun destroyWindow(window: GLFWWindow): Unit = glfwDestroyWindow(window)

	/**
	 * `void glfwGetFramebufferSize(GLFWwindow *window, int *width, int *height);`
	 */
	fun getFramebufferSize(window: GLFWWindow, width: MemorySegment, height: MemorySegment): Unit = glfwGetFramebufferSize(window, width, height)

	/**
	 * `void glfwSwapBuffers(GLFWwindow *window);`
	 */
	fun swapBuffers(window: GLFWWindow): Unit = glfwSwapBuffers(window)

	/**
	 * `void glfwPollEvents(void);`
	 */
	fun pollEvents(): Unit = glfwPollEvents()

	/**
	 * `GLFWerrorfun glfwSetErrorCallback(GLFWerrorfun callback);`
	 */
	fun setErrorCallback(callback: GLFWErrorFun?): GLFWErrorFun? {
		fun MemorySegment.asFunction(): GLFWErrorFun? = if (this == NULL()) null else { errorCode: Int, description: String? ->
			GLFWerrorfun.invoke(this, errorCode, description.cstr(global))
		}
		callback ?: return glfwSetErrorCallback(NULL()).asFunction()
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
	 * `GLFWframebuffersizefun glfwSetFramebufferSizeCallback(GLFWwindow *window, GLFWframebuffersizefun callback);`
	 */
	fun setFramebufferSizeCallback(window: GLFWWindow, callback: GLFWFramebufferSizeFun?): GLFWFramebufferSizeFun? {
		fun MemorySegment.asFunction(): GLFWFramebufferSizeFun? = if (this == NULL()) null else { window: GLFWWindow, width: Int, height: Int ->
			GLFWframebuffersizefun.invoke(this, window, width, height)
		}
		callback ?: return glfwSetFramebufferSizeCallback(window, NULL()).asFunction()
		return glfwSetFramebufferSizeCallback(
			window,
			GLFWframebuffersizefun.allocate(
				/*fi =*/ { window: GLFWWindow, width: Int, height: Int -> callback(window, width, height) },
				/*arena =*/ global
			)
		).asFunction()
	}
	typealias GLFWFramebufferSizeFun = (window: GLFWWindow, width: Int, height: Int) -> Unit
}