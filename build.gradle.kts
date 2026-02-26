@file:OptIn(ExperimentalKotlinGradlePluginApi::class)
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.gradle.kotlin.dsl.register

plugins {
	alias(libs.plugins.kotlin.jvm)
}

group = "io.github.epicvon2468.school"
version = libs.versions.self.get()

repositories {
	mavenCentral()
}

dependencies {
	compileOnly(libs.jetBrains.annotations)
	implementation(project(":generated"))
}

private fun createExec(
	name: String,
	entrypoint: String
): TaskProvider<JavaExec> = tasks.register<JavaExec>(name) {
	classpath(sourceSets.main.get().compileClasspath)
	classpath(sourceSets.main.get().runtimeClasspath)
	mainClass = "io.github.epicvon2468.school.${entrypoint}Kt"
}

createExec("runHouse", "prac1.House")
createExec("runPaintDemo", "prac1.PaintDemo")
createExec("runGLFWHouse", "prac1.glfw.GLFWHouse")
createExec("runIntegersAndReals", "prac2.IntegersAndReals")
createExec("runDate", "prac2.Date")
createExec("runTime", "prac2.Time")
createExec("runCurrentTime", "prac2.CurrentTime")
createExec("runCustomDataType", "prac2.extension.CustomDataType")

tasks.withType<JavaCompile> {
	options.apply {
		encoding = "UTF-8"
		isIncremental = true
	}
}

tasks.withType<JavaExec> {
	jvmArgs("-XX:+UseCompactObjectHeaders", "--enable-native-access=ALL-UNNAMED")
	if (System.getenv("XDG_SESSION_TYPE") == "wayland") systemProperty("awt.toolkit.name", "WLToolkit")
	environment("LD_LIBRARY_PATH", "/usr/lib/x86_64-linux-gnu:${projectDir.absolutePath}/generated/glad/src")
}

kotlin {
	kotlinDaemonJvmArgs = listOf("-XX:+UseCompactObjectHeaders", "--enable-native-access=ALL-UNNAMED")
	jvmToolchain {
		vendor.set(JvmVendorSpec.JETBRAINS)
		languageVersion.set(JavaLanguageVersion.of(25))
	}
}