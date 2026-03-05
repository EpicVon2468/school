package org.glfw;

import java.lang.foreign.MemorySegment;

public class glfw3_h {

	private glfw3_h() { throw stub(); }

	private static LinkageError stub() {
		return new LinkageError("Method was called on stub header!");
	}

	public static MemorySegment NULL() { throw stub(); }

	public static int GLFW_CONTEXT_VERSION_MAJOR() { throw stub(); }
	public static int GLFW_CONTEXT_VERSION_MINOR() { throw stub(); }
	public static int GLFW_OPENGL_PROFILE() { throw stub(); }
	public static int GLFW_OPENGL_CORE_PROFILE() { throw stub(); }
	public static int GLFW_FALSE() { throw stub(); }

	public static int glfwInit() { throw stub(); }
	public static void glfwTerminate() { throw stub(); }
	public static MemorySegment glfwGetProcAddress(MemorySegment procname) { throw stub(); }
	public static double glfwGetTime() { throw stub(); }
	public static void swapInterval(int interval) { throw stub(); }
	public static int glfwWindowShouldClose(MemorySegment window) { throw stub(); }
	public static void glfwWindowHint(int hint, int value) { throw stub(); }
	public static MemorySegment glfwCreateWindow(int width, int height, String title, MemorySegment monitor, MemorySegment share) { throw stub(); }
	public static void glfwMakeContextCurrent(MemorySegment window) { throw stub(); }
	public static void glfwDestroyWindow(MemorySegment window) { throw stub(); }
	public static void glfwGetFramebufferSize(MemorySegment window, MemorySegment width, MemorySegment height) { throw stub(); }
	public static void glfwSwapBuffers(MemorySegment window) { throw stub(); }
	public static void glfwPollEvents() { throw stub(); }
	public static MemorySegment glfwSetErrorCallback(MemorySegment callback) { throw stub(); }
	public static MemorySegment glfwSetFramebufferSizeCallback(MemorySegment window, MemorySegment callback) { throw stub(); }
}