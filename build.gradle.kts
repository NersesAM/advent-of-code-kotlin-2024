plugins {
    kotlin("jvm") version "2.1.0"
}

sourceSets {
    main {
        kotlin.srcDir("src")
    }
}

tasks {
    wrapper {
        gradleVersion = "8.11.1"
    }
}

java {
  sourceCompatibility = JavaVersion.VERSION_23
  targetCompatibility = JavaVersion.VERSION_23
}
