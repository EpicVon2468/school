package io.github.epicvon2468.school.prac1.glfw

import java.lang.foreign.MemoryLayout
import java.lang.foreign.SequenceLayout
import java.lang.foreign.StructLayout
import java.lang.invoke.VarHandle

data object Vertex {

	val LAYOUT: StructLayout

	val LAYOUT__POS: SequenceLayout = VEC2_LAYOUT.withName("pos")
	val HANDLE__POS: VarHandle

	val LAYOUT__COL: SequenceLayout = VEC3_LAYOUT.withName("col")
	val HANDLE__COL: VarHandle

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