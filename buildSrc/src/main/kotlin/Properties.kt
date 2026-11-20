import org.gradle.api.Project
import java.util.Properties

/**
 * Helper function to retrieve configuration variable values
 */
fun Project.getProperty(name: String): String? {
	// sample.var --> SAMPLE_VAR
	val environmentName = name.uppercase().replace(".", "_")

	return findProperty(name)?.toString() ?: System.getenv(environmentName) ?: null
}

/**
 * Reads the bundled default server address from build-time configuration.
 *
 * The value is deliberately NOT stored in version-controlled sources, so
 * publishing this repository never exposes a private server address. Sources,
 * in order of precedence:
 *  1. Gradle property (e.g. -Pdefault.server.address=... or ~/.gradle/gradle.properties)
 *  2. Environment variable DEFAULT_SERVER_ADDRESS
 *  3. Local, gitignored local.properties key default.server.address
 *
 * Returns an empty string when nothing is configured, so a clean checkout still
 * builds and behaves like upstream Jellyfin (no bundled default server).
 */
fun Project.getDefaultServerAddress(): String {
	// 1 + 2: Gradle properties and environment variables
	getProperty("default.server.address")?.takeIf { it.isNotBlank() }?.let { return it.trim() }

	// 3: gitignored local.properties (Android Studio's standard private config file)
	val localProperties = rootProject.file("local.properties")
	if (localProperties.isFile) {
		val props = Properties()
		localProperties.inputStream().use { props.load(it) }
		props.getProperty("default.server.address")?.takeIf { it.isNotBlank() }?.let { return it.trim() }
	}

	return ""
}

/** Escapes a raw value into a Java/Kotlin string literal for buildConfigField. */
fun String.toBuildConfigStringLiteral(): String = buildString {
	append('"')
	for (c in this@toBuildConfigStringLiteral) {
		when (c) {
			'\\' -> append("\\\\")
			'"' -> append("\\\"")
			'\n' -> append("\\n")
			'\r' -> append("\\r")
			else -> append(c)
		}
	}
	append('"')
}
