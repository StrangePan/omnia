plugins {
  kotlin("multiplatform")
  id("com.android.library")
}

project.setBuildDir("out")

kotlin {
  jvm()
  android()
  iosX64()
  iosArm64()
  iosSimulatorArm64()

  sourceSets {
    val commonMain by getting {
      kotlin.srcDir("src/commonMain")
      dependencies {
        implementation(kotlin("test"))
        implementation(project(":omnia"))
        implementation("com.badoo.reaktive:reaktive:1.2.1")
        implementation("com.badoo.reaktive:reaktive-testing:1.2.1")
      }
    }

    val jvmMain by getting {
      kotlin.srcDir("src/jvmMain")
    }

    val androidMain by getting {
      kotlin.srcDir("src/androidMain")
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

android {
  compileSdk = 33
  sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
  defaultConfig {
    minSdk = 26
    targetSdk = 33
  }
}
