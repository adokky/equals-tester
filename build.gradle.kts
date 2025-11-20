plugins {
    id("io.github.adokky.quick-mpp") version "0.21"
    id("io.github.adokky.quick-publish") version "0.21"
    id("com.vanniktech.maven.publish") version "0.34.0"
}

group = "io.github.adokky"
version = "1.0"

mavenPublishing {
    pom {
        description = "Automatic tester for equals(), hashCode(), compareTo(), toString()"
        inceptionYear = "2025"
    }
}