package io.github.epicvon2468.school.prac1.glfw

import org.glfw.glfw3_h.*

import java.lang.foreign.MemorySegment

abstract class Shape {

	val vertexBuffer: MemorySegment = global.allocate(GLuint)
	val vertexArray: MemorySegment = global.allocate(GLuint)
	abstract val vertices: MemorySegment
	abstract val verticesCount: Long

	fun genVertexBuffer() {
		GL.genBuffers(1, vertexBuffer)
		GL.bindBuffer(GL_ARRAY_BUFFER(), vertexBuffer[GLuint, 0])
		GL.bufferData(GL_ARRAY_BUFFER(), verticesCount * GLfloat.byteSize(), vertices, GL_STATIC_DRAW())
	}

	@JvmField
	var colourOverride: Triple<Float, Float, Float>? = null
	// FIXME: Not ideal
	var colourOverrideLocation: Int = -1

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
			pointer = NULL()
		)
		GL.bindVertexArray(0)
	}

	fun draw() {
		GL.uniform3fv(colourOverrideLocation, 1, colourOverride ?: COLOUR_UNSET)
		GL.bindVertexArray(vertexArray[GLuint, 0])
		GL.drawArrays(mode = GL_TRIANGLES(), first = 0, count = (verticesCount / 3).toInt())
	}
}

// TODO: use Pair or Point class for most constructors here

data class Triangle(
	override val vertices: MemorySegment,
	override val verticesCount: Long
) : Shape() {

	constructor(vararg vertices: Float) : this(global.allocateArray(GLfloat, *vertices.toTypedArray()), vertices.size.toLong())

	constructor(
		top: Pair<Float, Float>,
		left: Pair<Float, Float>,
		right: Pair<Float, Float>
	) : this(
		top.first, top.second, 0.0f,
		left.first, left.second, 0.0f,
		right.first, right.second, 0.0f
	)
}

// (tlx, tly)--(trx, try)
// |					|
// |					|
// |					|
// |					|
// |					|
// |					|
// (blx, bly)--(brx, bry)
open class Quadrilateral(override val vertices: MemorySegment) : Shape() {

	override val verticesCount: Long = 18

	constructor(
		upperLeftX: Float, upperLeftY: Float,
		lowerLeftX: Float, lowerLeftY: Float,
		upperRightX: Float, upperRightY: Float,
		lowerRightX: Float, lowerRightY: Float,
		tlxOffset: Float = 0.0f, tlyOffset: Float = 0.0f,
		blxOffset: Float = 0.0f, blyOffset: Float = 0.0f,
		trxOffset: Float = 0.0f, tryOffset: Float = 0.0f,
		brxOffset: Float = 0.0f, bryOffset: Float = 0.0f
	) : this(
		global.allocateArray(
			GLfloat,

			// ul - ur
			// |
			// ll
			upperLeftX + tlxOffset, upperLeftY + tlyOffset, 0.0f,
			lowerLeftX + blxOffset, lowerLeftY + blyOffset, 0.0f,
			upperRightX + trxOffset, upperRightY + tryOffset, 0.0f,

			//      ur
			//       |
			// ll - lr
			upperRightX + trxOffset, upperRightY + tryOffset, 0.0f,
			lowerLeftX + blxOffset, lowerLeftY + blyOffset, 0.0f,
			lowerRightX + brxOffset, lowerRightY + bryOffset, 0.0f,
		)
	)

	constructor(
		upperLeftX: Float, upperLeftY: Float,
		lowerLeftX: Float, lowerLeftY: Float,
		upperRightX: Float, upperRightY: Float,
		lowerRightX: Float, lowerRightY: Float,
		offsetX: Float = 0.0f,
		offsetY: Float = 0.0f
	) : this(
		upperLeftX = upperLeftX, upperLeftY = upperLeftY,
		lowerLeftX = lowerLeftX, lowerLeftY = lowerLeftY,
		upperRightX = upperRightX, upperRightY = upperRightY,
		lowerRightX = lowerRightX, lowerRightY = lowerRightY,
		tlxOffset = offsetX, tlyOffset = offsetY,
		blxOffset = offsetX, blyOffset = offsetY,
		trxOffset = offsetX, tryOffset = offsetY,
		brxOffset = offsetX, bryOffset = offsetY
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
	upperLeftX = x1, upperLeftY = y1,
	lowerLeftX = x1, lowerLeftY = y2,
	upperRightX = x2, upperRightY = y1,
	lowerRightX = x2, lowerRightY = y2,
	offsetX = offsetX, offsetY = offsetY
) {

	constructor(
		upperLeft: Pair<Float, Float>,
		lowerRight: Pair<Float, Float>,
		offsetX: Float = 0.0f,
		offsetY: Float = 0.0f
	) : this(
		x1 = upperLeft.first,
		y1 = upperLeft.second,
		x2 = lowerRight.first,
		y2 = lowerRight.second,
		offsetX = offsetX,
		offsetY = offsetY
	)
}