plugins {
    application
    java
    war

    // wasm plugin
    id("org.teavm") version "0.9.2"    
}

configurations {
    create("war")
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    // Use JUnit Jupiter for testing.
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.3")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // This dependency is used by the application.
    implementation("com.google.guava:guava:32.1.1-jre")

    // JSON parsing library
    implementation("org.json:json:20171018")

    // war plugin (for stdout-helper)
    "war"(project(":stdout-helper", "war"))
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(20))
    }
}

application {
    mainClass.set("parssist.App")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

teavm {
    js {
        addedToWebApp = true
    }
    wasm {
        addedToWebApp = true
    }
    wasi {
        outputDir = layout.buildDirectory.dir("libs/wasi").get().asFile
        relativePathInOutputDir = ""
    }
    all {
        mainClass = "parssist.App"
    }
}

tasks.war {
    dependsOn(configurations["war"])
    from(provider { configurations["war"].map { zipTree(it) } })
}

tasks.assemble {
    dependsOn(tasks.generateWasi,)
}

tasks.register("copyWasm", Copy::class) {
    dependsOn("generateWasm")
    from(tasks["generateWasm"].outputs)
    into("../pub")
}

tasks.named("build") {
    dependsOn("copyWasm")
}