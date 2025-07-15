import java.nio.charset.StandardCharsets

plugins {
	id("idea")
	id("java-library")
	id("maven-publish")
	id("dev.architectury.loom") version "1.10-SNAPSHOT"
	id("me.modmuss50.mod-publish-plugin") version "0.8.3"
	id("co.uzzu.dotenv.gradle") version "4.0.0"
}

fun prop(key: String) = property(key) as String

version = prop("mod_version")
group = prop("mod_group_id")

repositories {
	mavenLocal()

	maven("https://maven.neoforged.net")
	maven("https://maven.parchmentmc.org")
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
	minecraft("com.mojang:minecraft:${prop("minecraft_version")}")

	@Suppress("UnstableApiUsage")
	mappings(loom.layered {
		officialMojangMappings()
		parchment("org.parchmentmc.data:parchment-${prop("parchment_minecraft_version")}:${prop("parchment_mappings_version")}@zip")
	})

	neoForge("net.neoforged:neoforge:${prop("neo_version")}")
}

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

sourceSets.main.get().resources.srcDir(generateModMetadata)
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

publishMods {
	type = STABLE
	modLoaders.add("neoforge")

	file = tasks.remapJar.get().archiveFile
	additionalFiles.from(tasks.remapSourcesJar.get().archiveFile)

	var changelogText = "# ${prop("version")}\n\n"
	// Read the latest version from the changelog
	changelogText += File(rootDir, "changelog.md")
		.readText(StandardCharsets.UTF_8)
		.split(Regex("^#(?!#).*$", RegexOption.MULTILINE))[1]
		.trim()
	changelog = changelogText

    dryRun = true

	// TODO: Uncomment when I add Lazuli to CurseForge
//	curseforge {
//		projectId = ""
//		projectSlug = "lazuli"
//		accessToken = env.CURSEFORGE_TOKEN.value
//		announcementTitle = "Get from CurseForge"
//
//		minecraftVersions.add("1.21.1")
//		javaVersions.add(JavaVersion.VERSION_21)
//		javaVersions.add(JavaVersion.VERSION_22)
//		clientRequired = true
//		serverRequired = true
//	}

	modrinth {
		projectId = ""
		accessToken = env.MODRINTH_TOKEN.value
		announcementTitle = "Get from Modrinth"

		minecraftVersions.add("1.21.1")
	}

	github {
		repository = "emmathemartian/lazuli"
		// My branches are named by the minecraft version they target
		commitish = prop("minecraft_version")
		accessToken = env.GITHUB_TOKEN.value
		announcementTitle = "Get from GitHub"

		allowEmptyFiles = true
	}

//	discord {
//		val avatar = "https://raw.githubusercontent.com/EmmaTheMartian/lazuli/refs/heads/1.21.1/src/main/resources/assets/lazuli/icon.png"
//
//		webhookUrl = env.DISCORD_WEBHOOK_URL.value
//		username = "Lazuli Release Bot"
//		avatarUrl = avatar
//
//		style {
//			look = "MODERN"
//			thumbnailUrl = avatar
//		}
//	}
}

// IDEA no longer automatically downloads sources/javadoc jars for dependencies, so we need to explicitly enable the behavior.
idea {
	module {
		isDownloadSources = true
		isDownloadJavadoc = true
	}
}
