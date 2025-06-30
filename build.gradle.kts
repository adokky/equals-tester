plugins {
    id("io.github.adokky.quick-mpp") version "0.14"
    id("io.github.adokky.quick-publish") version "0.14"
    id("com.vanniktech.maven.publish") version "0.32.0"
}

group = "io.github.adokky"
version = "0.13"

mavenPublishing {
    pom {
        description = "Kotlin MPP automatic tester for equals(), hashCode(), compareTo(), toString()"
        inceptionYear = "2025"
    }
}