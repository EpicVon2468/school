package org.glfw;

import java.lang.foreign.AddressLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public class glfw3_h {

	private glfw3_h() { throw stub(); }

	private static LinkageError stub() {
		return new LinkageError("Method was called on stub header!");
	}
	private static <T> T stubVal() { throw stub(); }

	public static MemorySegment NULL() { throw stub(); }
	public static final AddressLayout C_POINTER = stubVal();
	public static final ValueLayout.OfInt C_INT = stubVal();

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
	public static MemorySegment glfwCreateWindow(int width, int height, MemorySegment title, MemorySegment monitor, MemorySegment share) { throw stub(); }
	public static void glfwMakeContextCurrent(MemorySegment window) { throw stub(); }
	public static void glfwDestroyWindow(MemorySegment window) { throw stub(); }
	public static void glfwGetFramebufferSize(MemorySegment window, MemorySegment width, MemorySegment height) { throw stub(); }
	public static void glfwSwapBuffers(MemorySegment window) { throw stub(); }
	public static void glfwPollEvents() { throw stub(); }
	public static MemorySegment glfwSetErrorCallback(MemorySegment callback) { throw stub(); }
	public static MemorySegment glfwSetFramebufferSizeCallback(MemorySegment window, MemorySegment callback) { throw stub(); }

	public static int GL_DEBUG_OUTPUT() { throw stub(); }
	public static int GL_DEBUG_OUTPUT_SYNCHRONOUS() { throw stub(); }
	public static int GL_CULL_FACE() { throw stub(); }
	public static int GL_SHADING_LANGUAGE_VERSION() { throw stub(); }
	public static int GL_VERSION() { throw stub(); }
	public static int GL_VERTEX_SHADER() { throw stub(); }
	public static int GL_FRAGMENT_SHADER() { throw stub(); }
	public static int GL_COLOR_BUFFER_BIT() { throw stub(); }
	public static int GL_DEPTH_BUFFER_BIT() { throw stub(); }
	public static int GL_NO_ERROR() { throw stub(); }
	public static int GL_INVALID_ENUM() { throw stub(); }
	public static int GL_INVALID_VALUE() { throw stub(); }
	public static int GL_INVALID_OPERATION() { throw stub(); }
	public static int GL_INVALID_FRAMEBUFFER_OPERATION() { throw stub(); }
	public static int GL_OUT_OF_MEMORY() { throw stub(); }
	public static int GL_STACK_UNDERFLOW() { throw stub(); }
	public static int GL_STACK_OVERFLOW() { throw stub(); }
	public static int GL_ARRAY_BUFFER() { throw stub(); }
	public static int GL_STATIC_DRAW() { throw stub(); }
	public static int GL_FLOAT() { throw stub(); }
	public static int GL_TRIANGLES() { throw stub(); }
	public static int GL_TRIANGLE_FAN() { throw stub(); }

	public static final ValueLayout.OfInt GLenum = stubVal();
	public static final ValueLayout.OfInt GLint = stubVal();
	public static final ValueLayout.OfInt GLsizei = stubVal();
	public static final ValueLayout.OfInt GLbitfield = stubVal();
	public static final ValueLayout.OfFloat GLfloat = stubVal();
	public static final ValueLayout.OfInt GLuint = stubVal();
	public static final ValueLayout.OfLong GLsizeiptr = stubVal();
	public static final ValueLayout.OfByte GLboolean = stubVal();
}