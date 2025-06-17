plugins {
    id("io.github.adokky.quick-mpp") version "0.11"
    signing
    `maven-publish`
    id("com.vanniktech.maven.publish") version "0.32.0"
}

group = "io.github.adokky"
version = "0.11"

signing {
    useGpgCmd()
}

mavenPublishing {
    publishToMavenCentral(com.vanniktech.maven.publish.SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()

    coordinates(
        groupId = group.toString(),
        artifactId = rootProject.name,
        version = version.toString()
    )

    pom {
        name = "equals-tester"
        description = "Kotlin MPP automatic tester for equals(), hashCode(), compareTo(), toString()"
        inceptionYear = "2025"
        url = "https://github.com/adokky/equals-tester"
        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                distribution = "https://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }
        developers {
            developer {
                id = "adokky"
                name = "Alexander Dokuchaev"
                url = "https://dokky.github.io"
            }
        }
        scm {
            url = "https://github.com/adokky/equals-tester"
            connection = "scm:git:git://github.com/adokky/equals-tester.git"
            developerConnection = "scm:git:ssh://git@github.com/adokky/equals-tester.git"
        }
    }
}