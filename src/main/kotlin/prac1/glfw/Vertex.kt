package io.github.epicvon2468.school.prac1.glfw

import java.lang.foreign.Arena
import java.lang.foreign.MemoryLayout
import java.lang.foreign.MemorySegment
import java.lang.foreign.SequenceLayout
import java.lang.foreign.StructLayout
import java.lang.invoke.VarHandle

data object Vertex {

//	operator fun invoke(pos: MemorySegment, col: MemorySegment) {
//		val instance: MemorySegment = Arena.global().allocate(LAYOUT)
//		instance.pos = pos
//		instance.col = col
//	}

	operator fun invoke(pos: Pair<Float, Float>, col: Triple<Float, Float, Float>) {
		val instance: MemorySegment = Arena.global().allocate(LAYOUT)
		instance.pos2.apply {
			this[0] = pos.first
			this[1] = pos.second
		}
		instance.col2.apply {
			this[0] = col.first
			this[1] = col.second
			this[2] = col.third
		}
		println("Vertex {")
		instance.pos2.apply {
			print("\tpos: [")
			print("${this[0]}, ${this[1]}")
			println("],")
		}
		instance.pos2.apply {
			print("\tcol: [")
			print("${this[0]}, ${this[1]}, ${this[2]}")
			println("]")
		}
		println("}")
	}

	val LAYOUT: StructLayout

	val LAYOUT__POS: SequenceLayout = VEC2_LAYOUT.withName("pos")
	val HANDLE__POS: VarHandle
//	var MemorySegment.pos: MemorySegment
//		get() = HANDLE__POS.get(this, 0L) as MemorySegment
//		set(value) = HANDLE__POS.set(this, 0L, 0L, value)
	val MemorySegment.pos2: NativeArray<Float> get() = NativeArray(this, HANDLE__COL)

	val LAYOUT__COL: SequenceLayout = VEC3_LAYOUT.withName("col")
	val HANDLE__COL: VarHandle
//	var MemorySegment.col: MemorySegment
//		get() = HANDLE__COL.get(this, 0L) as MemorySegment
//		set(value) = HANDLE__COL.set(this, 0L, 0L, value)
	val MemorySegment.col2: NativeArray<Float> get() = NativeArray(this, HANDLE__COL)

	init {
		LAYOUT = MemoryLayout.structLayout(
			LAYOUT__POS,
			LAYOUT__COL
		).withName("Vertex")

		// https://docs.oracle.com/en/java/javase/25/core/memory-layouts-and-structured-access.html
		HANDLE__POS = LAYOUT.varHandle(
			MemoryLayout.PathElement.groupElement("pos"),
			MemoryLayout.PathElement.sequenceElement()
		)
		HANDLE__COL = LAYOUT.varHandle(
			MemoryLayout.PathElement.groupElement("col"),
			MemoryLayout.PathElement.sequenceElement()
		)
	}
}