@file:JvmName("CalculatorLookAndFeel")
package io.github.epicvon2468.school.prac5.extension

import io.github.epicvon2468.school.fixText

import java.awt.Font

import javax.swing.UIDefaults
import javax.swing.UIManager
import javax.swing.plaf.FontUIResource
import javax.swing.plaf.metal.MetalLookAndFeel
import javax.swing.plaf.nimbus.NimbusLookAndFeel

// Hold my beer: https://stackoverflow.com/a/7435514
class CalculatorLookAndFeel : NimbusLookAndFeel() {

	override fun getName(): String = "Mavity's Calculator"

	init {
		// Everything about Nimbus is great, except for the scrollbar/pane.  Java's default scrollbar/pane fits the ideal theme much better.
		val uiDefaults = this.defaults
		val metalDefaults: UIDefaults = MetalLookAndFeel().defaults
		uiDefaults.putAll(
			metalDefaults.filter { it.key.toString().startsWith("Scroll") }
		)
		this.fixText()
		// Overrides the font to be JetBrains Mono Light :)
		val size: Int = (uiDefaults[FONT_KEY] as Font).size
		var font = Font(FONT, Font.PLAIN, size)
		// Font wasn't available by default (non-JBR?)
		if (font.family != FONT) font = Font(Font.MONOSPACED, Font.PLAIN, size)
		uiDefaults[FONT_KEY] = FontUIResource(font)
	}

	companion object {

		private const val FONT_KEY = "defaultFont"
		const val FONT = "JetBrains Mono Light"

		// I've set the default L&F to be CalculatorLookAndFeel in my Gradle script, but if run out of a JAR or if the property is ever changed it may not remain set
		// Some pracs may want the Calc L&F to always be on, and a short one-liner is preferable to doing this check individually many times
		// Any prac which wants to explicitly use this L&F no matter what should call this to make sure the L&F is correct
		// As an additional benefit, the reassignment of the L&F is only done if it is not already the L&F, meaning no re-running the code in `init`
		@JvmStatic
		fun enable() {
			if (UIManager.getLookAndFeel() !is CalculatorLookAndFeel) {
				UIManager.setLookAndFeel(CalculatorLookAndFeel())
			}
		}
	}
}