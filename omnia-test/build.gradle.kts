plugins {
  kotlin("multiplatform")
}

kotlin {
  jvmToolchain(22)
  jvm()
  iosX64()
  iosArm64()
  iosSimulatorArm64()

  sourceSets {
    val commonMain by getting {
      kotlin.srcDir("src/commonMain")
      dependencies {
        implementation(kotlin("test"))
        implementation(project(":omnia"))
        implementation(libraries.reaktive.core)
        implementation(libraries.reaktive.testing)
      }
    }
    val jvmMain by getting {
      kotlin.srcDir("src/jvmMain")
    }
    val iosX64Main by getting {
      kotlin.srcDir("src/iosX64Main")
    }
    val iosArm64Main by getting {
      kotlin.srcDir("src/iosArm64Main")
    }
    val iosSimulatorArm64Main by getting {
      kotlin.srcDir("src/iosSimulatorArm64Main")
    }
  }
}
