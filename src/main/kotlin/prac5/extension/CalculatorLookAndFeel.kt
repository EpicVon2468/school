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
	override fun getID(): String = "MCalc"
	override fun getDescription(): String = "Tweak of ${super.description}"

	init {
		// Everything about Nimbus is great, except for the scrollbar/pane.  Java's default scrollbar/pane fits the ideal theme much better.
		val uiDefaults = this.defaults
		val metalDefaults: UIDefaults = MetalLookAndFeel().defaults
		metalDefaults.filterTo(uiDefaults) { it.key.toString().startsWith("Scroll") }
		this.fixText()
		uiDefaults[FONT_KEY] = FONT
	}

	companion object {

		private const val FONT_KEY: String = "defaultFont"
		private const val FONT_NAME: String = "JetBrains Mono Light"
		// Lazy to avoid eager initialisation if someone is trying to set the fontSize variable
		@JvmStatic
		private val FONT: FontUIResource by lazy {
			val size: Int = fontSize
			var font = Font(FONT_NAME, Font.PLAIN, size)
			// Fallback
			if (font.family != FONT_NAME) font = Font(Font.MONOSPACED, Font.PLAIN, size)
			FontUIResource(font)
		}

		@JvmStatic
		inline var fontSize: Int
			get() = System.getProperty("mcalc.fontsize", "13").toInt()
			set(value) { System.setProperty("mcalc.fontsize", value.toString()) }

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