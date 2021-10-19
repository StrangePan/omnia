plugins {
  kotlin("multiplatform") version "1.5.31"
}

project.setBuildDir("out")

kotlin {
  jvm()

  sourceSets {
    val commonMain by getting {
      kotlin.srcDir("src/commonMain")
      dependencies {
        implementation(project(":omnia"))
        implementation(kotlin("test"))
        implementation("com.badoo.reaktive:reaktive:1.2.0")
        implementation("com.badoo.reaktive:reaktive-testing:1.2.0")
      }
    }

    val jvmMain by getting {
      kotlin.srcDir("src/jvmMain")
      dependencies {
        implementation("com.badoo.reaktive:reaktive-jvm:1.2.0")
      }
    }
  }
}