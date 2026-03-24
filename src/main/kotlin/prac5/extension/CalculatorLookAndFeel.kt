@file:JvmName("CalculatorLookAndFeel")
package io.github.epicvon2468.school.prac5.extension

import io.github.epicvon2468.school.fixText

import javax.swing.plaf.metal.MetalLookAndFeel
import javax.swing.plaf.nimbus.NimbusLookAndFeel

// TODO: Font and/or font size
// Hold my beer: https://stackoverflow.com/a/7435514
class CalculatorLookAndFeel : NimbusLookAndFeel() {

	init {
		// Everything about Nimbus is great, except for the scrollbar/pane.  Java's default scrollbar/pane fits the ideal theme much better.
		this.defaults.putAll(
			MetalLookAndFeel().defaults.filter { it.key.toString().startsWith("Scroll") }
		)
		this.fixText()
	}
}