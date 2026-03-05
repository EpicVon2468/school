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