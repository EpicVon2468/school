package io.github.epicvon2468.school.prac1.glfw

import org.glfw.glfw3_h.*

import java.lang.foreign.MemorySegment
import kotlin.math.PI

import kotlin.math.cos
import kotlin.math.sin

// Yes. lots of not ideals, I'll fix it up soon™
abstract class Shape : Drawable {

	var batch: Batch? = null
		set(value) {
			field?.shapes?.remove(this)
			value?.shapes?.add(this)
			field = value
		}

	val vertexBuffer: MemorySegment = global.allocate(GLuint)
	val vertexArray: MemorySegment = global.allocate(GLuint)
	abstract val vertices: MemorySegment
	abstract val verticesCount: Long

	fun genVertexBuffer() {
		GL.genBuffers(1, vertexBuffer)
		GL.bindBuffer(GL_ARRAY_BUFFER(), vertexBuffer[GLuint, 0])
		GL.bufferData(GL_ARRAY_BUFFER(), verticesCount * GLfloat.byteSize(), vertices, GL_STATIC_DRAW())
	}

	override var colour: Colour = COLOUR_UNSET
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

	protected open val mode: GLEnum = GL_TRIANGLES()

	override fun draw(colour: Colour?) {
		// Set the value of the 'colourOverride' uniform in shader.frag
		GL.uniform3fv(location = colourOverrideLocation, count = 1, value = colour ?: this.colour)
		GL.bindVertexArray(vertexArray[GLuint, 0])
		GL.drawArrays(mode = mode, first = 0, count = (verticesCount / 3).toInt())
	}
}

// TODO: use Pair or Point class for most constructors here

data class Circle(
	val basePoint: Pair<Float, Float>,
	val radius: Float,
	val numSides: Long = 360L
) : Shape() {

	override val mode: GLEnum = GL_TRIANGLE_FAN()

	// https://stackoverflow.com/questions/20394727/gl-triangle-strip-vs-gl-triangle-fan
	// Thanks to engired for giving me the knowledge of "x = r*cos(θ), y = r*sin(θ)", it's been a while since I've done geometry
	private fun genPoints(): List<Float> {
		val entries: MutableList<Float> = mutableListOf()
		fun MutableList<Float>.add(x: Float, y: Float) {
			add(x)
			add(y)
			add(0.0f)
		}
		entries.add(basePoint.first, basePoint.second)

		for (pos: Long in 0..numSides) {
			// Thanks to 7410 (funny numbers guy) for explaining my mistake and how to fix
			val vertex: Double = pos.toDouble() / numSides.toDouble() * (2 * PI)
			entries.add(
				basePoint.first + (radius * cos(vertex).toFloat()),
				basePoint.second + (radius * sin(vertex).toFloat())
			)
		}
		return entries
	}

	private val points: List<Float> by lazy(::genPoints)

	override val vertices: MemorySegment = global.allocateArray(GLfloat, *points.toTypedArray())
	override val verticesCount: Long = points.size.toLong()
}

data class Triangle(
	override val vertices: MemorySegment,
	override val verticesCount: Long,
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