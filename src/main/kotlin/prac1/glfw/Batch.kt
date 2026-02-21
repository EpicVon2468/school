package io.github.epicvon2468.school.prac1.glfw

data class Batch(override var colour: Colour = COLOUR_UNSET) : Drawable {

	val shapes: MutableList<Shape> = mutableListOf()

	fun add(shape: Shape): Boolean = this.shapes.add(shape)

	override fun draw(colour: Colour?) = this.shapes.forEach { shape: Shape -> shape.draw(colour) }
}