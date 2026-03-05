package org.glfw;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import static org.glfw.glfw3_h.stub;

public final class GLDEBUGPROC {

	private GLDEBUGPROC() {}

	public interface Function {

		void apply(int source, int type, int id, int severity, int length, MemorySegment message, MemorySegment userParam);
	}

	public static MemorySegment allocate(GLDEBUGPROC.Function fi, Arena arena) { throw stub(); }
}