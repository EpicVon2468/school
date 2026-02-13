package io.github.epicvon2468.school.prac1.glfw

import java.lang.foreign.MemorySegment
import java.lang.invoke.VarHandle

data class NativeArray<T>(val segment: MemorySegment, val handle: VarHandle) {

	@Suppress("UNCHECKED_CAST")
	operator fun get(index: Long): T? = handle.get(segment, 0L, index) as T?

	operator fun set(index: Long, value: T?) = handle.set(segment, 0L, index, value)
}