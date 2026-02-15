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

// Below this line is not included and should not be run, kept in case I need future reference.

// Removed the linearmaths package since it didn't actually expand any macros
// jextract --include-dir . --output ./src/main/java --target-package linearmaths --use-system-load-library $PWD/linearmaths.h
// Removed the glad package since it isn't needed
// jextract --include-dir ../../../glad/include/glad --output . --target-package glad --library glad --use-system-load-library ../../../glad/include/glad/gl.h