plugins {
	id("java")
	id("idea")
}

idea {
	module {
		generatedSourceDirs.add(project.file("src/main/java"))
	}
}

group = "org.glfw"
version = "3.4"

tasks.withType<JavaCompile> {
	options.apply {
		encoding = "UTF-8"
		isIncremental = true
	}
}

// jextract --include-dir /usr/include/GLFW --output . --target-package org.glfw --library glfw --use-system-load-library /usr/include/GLFW/glfw3.h