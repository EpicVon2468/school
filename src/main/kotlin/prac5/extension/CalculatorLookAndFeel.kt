package io.github.epicvon2468.school.prac5.extension

import javax.swing.plaf.metal.MetalLookAndFeel
import javax.swing.plaf.nimbus.NimbusLookAndFeel

// TODO: Font and/or font size
class CalculatorLookAndFeel : NimbusLookAndFeel() {

	init {
		// Everything about Nimbus is great, except for the scrollbar/pane.  Java's default scrollbar/pane fits the ideal theme much better.
		this.defaults.putAll(
			MetalLookAndFeel().defaults.filter { it.key.toString().startsWith("Scroll") }
		)
	}
}