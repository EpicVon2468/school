package io.github.epicvon2468.school.prac1.glfw

// TODO: SpriteBatch-like class
interface Drawable {

	var colour: Colour

	fun draw(colour: Colour = this.colour)
}