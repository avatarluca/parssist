plugins {
    // Apply the foojay-resolver plugin to allow automatic download of JDKs
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.4.0"
}

rootProject.name = "parssist"

// Apply stdout-helper plugin (for wasm println output)
include("stdout-helper")

include("app")