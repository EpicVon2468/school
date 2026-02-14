package io.github.epicvon2468.school.prac1.glfw

// Sourced from: https://github.com/EpicVon2468/bunny/blob/master/src/jvmMain/kotlin/io/github/epicvon2468/bunny/NativeUtils.kt

import java.lang.foreign.Arena
import java.lang.foreign.MemoryLayout
import java.lang.foreign.MemorySegment
import java.lang.foreign.SequenceLayout
import java.lang.invoke.VarHandle

/**
 * Copies the contents of this [Collection] into a native C-style array using [allocateArray].
 *
 * @receiver the [Collection] to copy values from.
 *
 * @param arena the [Arena] to use for allocation.
 * @param elementLayout the layout of the [Collection]'s values.
 *
 * @return a [MemorySegment] pointing to the allocated native C-style array.
 *
 * @author Mavity The Madity
 *
 * @see allocateArray
 * @see Arena
 * @see MemorySegment
 * @see MemoryLayout
 * @see MemoryLayout.sequenceLayout
 * @see MemoryLayout.arrayElementVarHandle
 */
fun Collection<Any?>.toNativeArray(
	arena: Arena,
	elementLayout: MemoryLayout
): MemorySegment = toTypedArray().toNativeArray(arena, elementLayout)

/**
 * Copies the contents of this [Array] into a native C-style array using [allocateArray].
 *
 * @receiver the [Array] to copy values from.
 *
 * @param arena the [Arena] to use for allocation.
 * @param elementLayout the layout of the [Array]'s values.
 *
 * @return a [MemorySegment] pointing to the allocated native C-style array.
 *
 * @author Mavity The Madity
 *
 * @see allocateArray
 * @see Arena
 * @see MemorySegment
 * @see MemoryLayout
 * @see MemoryLayout.sequenceLayout
 * @see MemoryLayout.arrayElementVarHandle
 */
fun Array<Any?>.toNativeArray(
	arena: Arena,
	elementLayout: MemoryLayout
): MemorySegment = arena.allocateArray(elementLayout, *this)

/**
 * Allocates a native C-style array of [values], using [elementLayout] to define the array's layout.
 *
 * @receiver the [Arena] to use for allocation.
 *
 * @param elementLayout the layout of [values].
 * @param values the values of the new array.
 *
 * @return a [MemorySegment] pointing to the allocated native C-style array.
 *
 * @author Mavity The Madity
 *
 * @see Arena
 * @see MemorySegment
 * @see MemoryLayout
 * @see MemoryLayout.sequenceLayout
 * @see MemoryLayout.arrayElementVarHandle
 */
fun Arena.allocateArray(elementLayout: MemoryLayout, vararg values: Any?, is2DArray: Boolean = false): MemorySegment {
	val layout: SequenceLayout = MemoryLayout.sequenceLayout(values.size.toLong(), elementLayout)
	val array: MemorySegment = allocate(layout)
	val vh: VarHandle = if (is2DArray) elementLayout.arrayElementVarHandle(MemoryLayout.PathElement.sequenceElement()) else elementLayout.arrayElementVarHandle()
	for (current: Long in 0..<layout.elementCount()) {
		if (is2DArray) vh.set(array, 0L, current, 0L, values[current.toInt()]) else vh.set(array, 0L, current, values[current.toInt()])
	}
	return array
}

fun MemorySegment.jvmNull(): MemorySegment? = if (this == MemorySegment.NULL) null else this
fun MemorySegment?.nativeNull(): MemorySegment = this ?: MemorySegment.NULL

// This always evaluates 'other' (even if inlined)
infix fun MemorySegment.elvis(other: MemorySegment): MemorySegment = this.jvmNull() ?: other

// This version does not have the same problem, but might not always want braces
infix fun MemorySegment.elvis(other: () -> MemorySegment): MemorySegment = this.jvmNull() ?: other()

@JvmField
val EMPTY_STRING: MemorySegment = Arena.global().allocateFrom("")

/**
 * Allocates a native C-style string from [this], returning [EMPTY_STRING] if [this] is [null or empty][isNullOrEmpty].
 *
 * @param arena the [Arena] to use for allocation (Note: [EMPTY_STRING] is already allocated by [Arena.global]).
 *
 * @return a [MemorySegment] pointing to the allocated native C-style string.
 *
 * @author Mavity The Madity
 *
 * @see String
 * @see Arena
 * @see Arena.allocateFrom
 * @see MemorySegment
 * @see EMPTY_STRING
 * @see isNullOrEmpty
 */
fun String?.cstr(arena: Arena): MemorySegment = if (isNullOrEmpty()) EMPTY_STRING else arena.allocateFrom(this)