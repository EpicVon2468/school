package io.github.epicvon2468.school.prac1.glfw

data class Batch(override var colour: Colour = COLOUR_UNSET) : Drawable {

	override var zIndex: Int = 0
		set(_) = error("Setting the zIndex of a Batch is redundant and does nothing")

	val shapes: MutableMap<Int, Shape> = mutableMapOf()

	fun add(shape: Shape) {
		shape.batch = this
	}

	override fun draw(colour: Colour?) = this.shapes.forEach { (_, shape: Shape) -> shape.draw(colour) }
}