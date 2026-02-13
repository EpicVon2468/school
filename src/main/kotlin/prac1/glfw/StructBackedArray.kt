package io.github.epicvon2468.school.prac1.glfw

import java.lang.foreign.MemorySegment
import java.lang.invoke.VarHandle

data class StructBackedArray<T>(
	private val segment: MemorySegment,
	private val handle: VarHandle,
	val elementCount: Long
) : Iterable<T> {

	// You better be pretty damn sure there's something at your index & that it's within size, because this _will_ murder the JVM if not.
	@Suppress("UNCHECKED_CAST")
	operator fun get(index: Long): T = handle.get(segment, 0L, index) as T

	operator fun set(index: Long, value: T?) = handle.set(segment, 0L, index, value)

	override fun iterator(): Iterator<T> {
		class NativeArrayIterator : ListIterator<T> {

			private var index: Long = -1

			override fun next(): T = get(++index)

			override fun hasNext(): Boolean = index + 1 < elementCount

			override fun hasPrevious(): Boolean = index - 1 >= 0

			override fun previous(): T = get(--index)

			override fun nextIndex(): Int = (index + 1).toInt()

			override fun previousIndex(): Int = (index - 1).toInt()
		}
		return NativeArrayIterator()
	}
}