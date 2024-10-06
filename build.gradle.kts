// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.detekt)
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.jetbrains.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
}

detekt {
    // The directories where detekt looks for source files.
    source.setFrom(files(projectDir))
    // Define the detekt configuration(s) you want to use.
    config.setFrom(files("$rootDir/detekt/detekt-config.yml"))
    // Applies the config files on top of detekt's default config file. `false` by default.
    buildUponDefaultConfig = true
    // If set to `true` the build does not fail when the
    // maxIssues count was reached. Defaults to `false`.
    ignoreFailures = false
    // Specify the base path for file paths in the formatted reports.
    // If not set, all file paths reported will be absolute file path.
    basePath = projectDir.path
}

dependencies {
    detektPlugins(libs.detekt.rules.compose)
}