plugins {
  kotlin("multiplatform") version "1.5.31"
}

project.setBuildDir("out")

kotlin {
  jvm()

  sourceSets {
    val jvmMain by getting {
      kotlin.srcDir("src/jvmMain")
      dependencies {
        implementation(project(":lib:omnia:omnia-test"))
        implementation("com.badoo.reaktive:reaktive:1.2.0")
        implementation("com.badoo.reaktive:reaktive-jvm:1.2.0")
      }
    }

    val jvmTest by getting {
      kotlin.srcDir("src/jvmTest")
      dependencies {
        implementation(kotlin("test"))
        implementation(kotlin("test-junit"))
        implementation("com.google.truth:truth:1.0")
        implementation("org.mockito:mockito-core:3.2.0")
        implementation("org.mockito.kotlin:mockito-kotlin:3.2.0")
        implementation("com.badoo.reaktive:reaktive-testing:1.2.0")
        implementation("com.google.truth.extensions:truth-java8-extension:1.0")
      }
    }
  }
}
