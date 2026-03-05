package org.glfw;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import static org.glfw.glfw3_h.stub;

public class GLFWframebuffersizefun {

	private GLFWframebuffersizefun() {}

	public interface Function {

		void apply(MemorySegment window, int width, int height);
	}

	public static MemorySegment allocate(GLFWframebuffersizefun.Function fi, Arena arena) { throw stub(); }

	public static void invoke(MemorySegment funcPtr, MemorySegment window, int width, int height) { throw stub(); }
}