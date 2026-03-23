package io.github.epicvon2468.school.prac5.extension

import javax.swing.UIManager
import javax.swing.plaf.nimbus.NimbusLookAndFeel

// TODO: Font and/or font size
class CalculatorLookAndFeel : NimbusLookAndFeel() {

	init {
		// Everything about Nimbus is great, except for the scrollbar/pane.  Java's default scrollbar/pane fits the ideal theme much better.
		defaults.putDefaults(
			arrayOf(
				"ScrollBarUI", "javax.swing.plaf.metal.MetalScrollBarUI",
				"ScrollPaneUI", "javax.swing.plaf.metal.MetalScrollPaneUI",
				"ScrollBar.width", UIManager.getLookAndFeel().defaults["ScrollBar.width"]
			)
		)
	}
}