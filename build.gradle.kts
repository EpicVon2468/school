@file:OptIn(ExperimentalKotlinGradlePluginApi::class)
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

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

tasks.withType<JavaCompile> {
	options.apply {
		encoding = "UTF-8"
		isIncremental = true
	}
}

tasks.withType<JavaExec> {
	jvmArgs("-XX:+UseCompactObjectHeaders", "--enable-native-access=ALL-UNNAMED")
	systemProperty("awt.toolkit.name", "WLToolkit")
	environment("LD_LIBRARY_PATH", "/usr/lib/x86_64-linux-gnu:${projectDir.absolutePath}/generated/glad/src")
}

kotlin {
	kotlinDaemonJvmArgs = listOf("-XX:+UseCompactObjectHeaders", "--enable-native-access=ALL-UNNAMED")
	jvmToolchain {
		vendor.set(JvmVendorSpec.JETBRAINS)
		languageVersion.set(JavaLanguageVersion.of(25))
	}
}