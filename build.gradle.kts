plugins {
	id("java-library")
	id("maven-publish")
	id("idea")
	id("dev.architectury.loom") version "1.7-SNAPSHOT"
}

fun prop(key: String) = property(key) as String

version = prop("mod_version")
group = prop("mod_group_id")

repositories {
	mavenLocal()

	maven("https://maven.neoforged.net")
	maven("https://maven.parchmentmc.org")

	// EMI
	maven("https://maven.terraformersmc.com/")

	// Modrinth Maven
	exclusiveContent {
		forRepository {
			maven("https://api.modrinth.com/maven")
		}
		filter {
			includeGroup("maven.modrinth")
		}
	}
}

base {
	archivesName = prop("mod_id")
}

java {
	toolchain.languageVersion = JavaLanguageVersion.of(21)
	withSourcesJar()
	withJavadocJar()
}

loom {
	runs {
		all {
			vmArg("-XX:+AllowEnhancedClassRedefinition")
		}
	}
}

dependencies {
	// Shorthands for Modrinth maven dependencies
	fun modrinth(id: String) = "maven.modrinth:$id:${prop("${id.replace('-', '_')}_version")}"
	fun enabled(id: String) = prop("enable_$id") == "true"

	minecraft("com.mojang:minecraft:${prop("minecraft_version")}")

	@Suppress("UnstableApiUsage")
	mappings(loom.layered {
		officialMojangMappings()
		parchment("org.parchmentmc.data:parchment-${prop("parchment_minecraft_version")}:${prop("parchment_mappings_version")}@zip")
	})

	neoForge("net.neoforged:neoforge:${prop("neo_version")}")

	compileOnly("dev.emi:emi-neoforge:${prop("emi_version")}:api")
	runtimeOnly("dev.emi:emi-neoforge:${prop("emi_version")}")
}

// This block of code expands all declared replace properties in the specified resource targets.
// A missing property will result in an error. Properties are expanded using ${} Groovy notation.
val replaceProperties = mapOf(
	"minecraft_version"       to prop("minecraft_version"),
	"minecraft_version_range" to prop("minecraft_version_range"),
	"neo_version"             to prop("neo_version"),
	"neo_version_range"       to prop("neo_version_range"),
	"loader_version_range"    to prop("loader_version_range"),
	"mod_id"                  to prop("mod_id"),
	"mod_name"                to prop("mod_name"),
	"mod_license"             to prop("mod_license"),
	"mod_version"             to prop("mod_version"),
	"mod_authors"             to prop("mod_authors"),
	"mod_description"         to prop("mod_description"),
)

val generateModMetadata = tasks.register<ProcessResources>("generateModMetadata") {
	inputs.properties(replaceProperties)
	expand(replaceProperties)
	from("src/main/templates")
	into("build/generated/sources/modMetadata")
}

// Include the output of "generateModMetadata" as an input directory for the build
// this works with both building through Gradle and the IDE.
sourceSets.main.get().resources.srcDir(generateModMetadata)
// To avoid having to run "generateModMetadata" manually, make it run on every project reload
tasks.ideaSyncTask.configure {
	dependsOn(generateModMetadata)
}

publishing {
	publications {
		register<MavenPublication>("mavenJava") {
			from(components["java"])
		}
	}

	repositories {
		maven("file://${project.projectDir}/repo")
	}
}

// IDEA no longer automatically downloads sources/javadoc jars for dependencies, so we need to explicitly enable the behavior.
idea {
	module {
		isDownloadSources = true
		isDownloadJavadoc = true
	}
}
