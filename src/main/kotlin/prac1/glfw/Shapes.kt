package io.github.epicvon2468.school.prac1.glfw

import org.glfw.glfw3_h.*

import java.lang.foreign.MemorySegment

interface Shape {

	val vertexBuffer: MemorySegment
	val vertexArray: MemorySegment
	val vertices: MemorySegment
	val verticesCount: Long

	fun genVertexBuffer() {
		GL.genBuffers(1, vertexBuffer)
		GL.bindBuffer(GL_ARRAY_BUFFER(), vertexBuffer[GLuint, 0])
		GL.bufferData(GL_ARRAY_BUFFER(), verticesCount * GLfloat.byteSize(), vertices, GL_STATIC_DRAW())
	}

	fun genVertexArray() {
		GL.genVertexArrays(1, vertexArray)
		GL.bindVertexArray(vertexArray[GLuint, 0])
		GL.enableVertexAttribArray(0)
		GL.bindBuffer(GL_ARRAY_BUFFER(), vertexArray[GLuint, 0])
		GL.vertexAttribPointer(
			index = 0,
			size = 3,
			type = GL_FLOAT(),
			normalised = false,
			stride = 0,
			pointer = MemorySegment.NULL
		)
		GL.bindVertexArray(0)
	}

	fun draw() {
		GL.bindVertexArray(vertexArray[GLuint, 0])
		GL.drawArrays(mode = GL_TRIANGLES(), first = 0, count = (verticesCount / 3).toInt())
	}
}

data class Triangle(
	override val vertices: MemorySegment,
	override val verticesCount: Long
) : Shape {

	override val vertexBuffer: MemorySegment = global.allocate(GLuint)
	override val vertexArray: MemorySegment = global.allocate(GLuint)

	constructor(vararg vertices: Float) : this(global.allocateArray(GLfloat, *vertices.toTypedArray()), vertices.size.toLong())
}

// (tlx, tly)--(trx, try)
// |					|
// |					|
// |					|
// |					|
// |					|
// |					|
// (blx, bly)--(brx, bry)
open class Quadrilateral(override val vertices: MemorySegment) : Shape {

	override val vertexBuffer: MemorySegment = global.allocate(GLuint)
	override val vertexArray: MemorySegment = global.allocate(GLuint)
	override val verticesCount: Long = 18

	constructor(
		tlx: Float, tly: Float,
		blx: Float, bly: Float,
		trx: Float, `try`: Float,
		brx: Float, bry: Float,
		tlxo: Float = 0.0f, tlyo: Float = 0.0f,
		blxo: Float = 0.0f, blyo: Float = 0.0f,
		trxo: Float = 0.0f, tryo: Float = 0.0f,
		brxo: Float = 0.0f, bryo: Float = 0.0f,
	) : this(
		global.allocateArray(
			GLfloat,

			tlx + tlxo, tly + tlyo, 0.0f,
			blx + blxo, bly + blyo, 0.0f,
			trx + trxo, `try` + tryo, 0.0f,

			trx + trxo, `try` + tryo, 0.0f,
			blx + blxo, bly + blyo, 0.0f,
			brx + brxo, bry + bryo, 0.0f,
		)
	)

	constructor(
		tlx: Float, tly: Float,
		blx: Float, bly: Float,
		trx: Float, `try`: Float,
		brx: Float, bry: Float,
		offsetX: Float = 0.0f,
		offsetY: Float = 0.0f
	) : this(
		tlx = tlx, tly = tly,
		blx = blx, bly = bly,
		trx = trx, `try` = `try`,
		brx = brx, bry = bry,
		tlxo = offsetX, tlyo = offsetY,
		blxo = offsetX, blyo = offsetY,
		trxo = offsetX, tryo = offsetY,
		brxo = offsetX, bryo = offsetY,
	)
}

// (x1, y1)--(x2, y1)
// |				|
// |				|
// |				|
// |				|
// (x1, y2)--(x2, y2)
data class Square(
	val x1: Float, val y1: Float,
	val x2: Float, val y2: Float,
	val offsetX: Float = 0.0f,
	val offsetY: Float = 0.0f
) : Quadrilateral(
//	global.allocateArray(
//		GLfloat,
//
//		x1 + offsetX, y1 + offsetY, 0.0f, // top left
//		x1 + offsetX, y2 + offsetY, 0.0f, // bottom left
//		x2 + offsetX, y1 + offsetY, 0.0f, // top right
//
//		x2 + offsetX, y1 + offsetY, 0.0f, // top right
//		x1 + offsetX, y2 + offsetY, 0.0f, // bottom left
//		x2 + offsetX, y2 + offsetY, 0.0f, // bottom right
//	)
	tlx = x1, tly = y1,
	blx = x1, bly = y2,
	trx = x2, `try` = y1,
	brx = x2, bry = y2,
	offsetX, offsetY
)