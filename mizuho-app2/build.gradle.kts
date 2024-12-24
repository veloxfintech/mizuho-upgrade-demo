buildscript {
    dependencies {
        classpath("com.velox:velox-gradle-plugin:0.9.0+")
    }
}

plugins {
    java
    application
    idea
}

apply(plugin = "com.velox.velox-gradle-plugin")

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        url = uri(providers.gradleProperty("velox_repo_url").get())
        credentials {
            username = providers.gradleProperty("velox_repo_user").get()
            password = providers.gradleProperty("velox_repo_password").get()
        }
    }
}

dependencies {
    implementation("com.velox:velox-framework:4.1.0+")
    runtimeOnly("ch.qos.logback:logback-classic:1.5.6")
}

application {
    mainClass.set("com.velox.app2.Application")
    applicationDefaultJvmArgs = listOf("-Dfile.encoding=UTF-8")
}

/**
 * Customize the start script generated by the application plugin to shorten
 * the classpath by pointing to the lib folder rather than listing individual
 * jars one by one. The latter usually has an issue on Windows where the
 * command argument length is limited
 */
tasks.startScripts {
    classpath = files("${System.getenv("APP_HOME")}/lib/*")
}

/**
 * Package the src/main/webapp folder into the distribution zip as the folder
 * contains web related resource files.
 */
distributions {
    main {
        contents {
            from("src/main/webapp") {
                into("src/main/webapp")
            }
        }
    }
}

/**
 * Development server setup
 */
tasks.register<JavaExec>("dev") {
    group = "application"
    description = "run application in development mode"

    mainClass.set("com.velox.app2.Application")
    classpath = sourceSets["main"].runtimeClasspath
    jvmArgs = listOf("-Dfile.encoding=UTF-8", "-Dvelox.environment=development")
}

/**
 * Extract the Velox bundled web resources into a local folder.
 * The folder ${project.buildDir}/extracted-included-webapp can then be added to
 * the application as additional web roots, hence the application can disable classpath
 * scanning when serving static resources. This helps Windows user to have faster
 * load time when starting/re-starting the application by avoiding unpacking jars
 * at runtime.
 */
tasks.register<Copy>("extractWebResources") {
    into("${layout.buildDirectory.get()}/extracted-included-webapp")
    into(".") {
        configurations.runtimeClasspath.get().asFileTree.matching {
            include("**/*velox-*.jar")
        }.forEach {
            from(zipTree(it)) {
                include("src/main/webapp/**")
            }
        }
    }
}

tasks.named("classes") {
    dependsOn("extractWebResources")
}

idea {
    module {
        excludeDirs.add(file(".vertx"))
        excludeDirs.add(file(".local"))
        isDownloadJavadoc = true
    }
}