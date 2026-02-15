package io.github.epicvon2468.school.prac1.glfw

import java.lang.foreign.Arena
import java.lang.foreign.MemoryLayout
import java.lang.foreign.MemorySegment
import java.lang.foreign.SequenceLayout
import java.lang.foreign.StructLayout
import java.lang.invoke.VarHandle

// Ignore this, from an earlier point in testing, no longer used anywhere
class Vertex(pos: Pair<Float, Float>, col: Triple<Float, Float, Float>) {

	operator fun component1(): StructBackedArray<Float> = this.pos
	operator fun component2(): StructBackedArray<Float> = this.col

	constructor(pos: Array<Float>, col: Array<Float>) : this(Pair(pos[0], pos[1]), Triple(col[0], col[1], col[2])) {
		require(pos.size == 2) { "Pos was of unexpected size '${pos.size}'! Content: ${pos.contentToString()}!" }
		require(col.size == 3) { "Col was of unexpected size '${col.size}'! Content: ${col.contentToString()}!" }
	}

	constructor(pos: List<Float>, col: List<Float>) : this(pos.toTypedArray(), col.toTypedArray())

	val delegate: MemorySegment = Arena.global().allocate(LAYOUT)

	val pos: StructBackedArray<Float> = delegate.pos
	val col: StructBackedArray<Float> = delegate.col

	init {
		this.pos.apply {
			this[0] = pos.first
			this[1] = pos.second
		}
		this.col.apply {
			this[0] = col.first
			this[1] = col.second
			this[2] = col.third
		}
		println(this)
	}

	override fun toString(): String = buildString {
		appendLine("Vertex {")

		append("\tpos: [")
		for (entry: Float in pos) append("$entry, ")
		appendLine("\b\b],")

		append("\tcol: [")
		for (entry: Float in col) append("$entry, ")
		appendLine("\b\b]")

		append('}')
	}

	companion object {

		val LAYOUT: StructLayout

		val LAYOUT__POS: SequenceLayout = VEC2_LAYOUT.withName("pos")
		val HANDLE__POS: VarHandle
		val MemorySegment.pos: StructBackedArray<Float> get() = StructBackedArray(this, HANDLE__POS, 2L)

		val LAYOUT__COL: SequenceLayout = VEC3_LAYOUT.withName("col")
		val HANDLE__COL: VarHandle
		val MemorySegment.col: StructBackedArray<Float> get() = StructBackedArray(this, HANDLE__COL, 3L)

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
}