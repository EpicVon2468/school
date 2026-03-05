package org.glfw;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import static org.glfw.glfw3_h.stub;

public class GLFWerrorfun {

	private GLFWerrorfun() {}

	public interface Function {

		void apply(int error_code, MemorySegment description);
	}

	public static MemorySegment allocate(GLFWerrorfun.Function fi, Arena arena) { throw stub(); }

	public static void invoke(MemorySegment funcPtr, int error_code, MemorySegment description) { throw stub(); }
}