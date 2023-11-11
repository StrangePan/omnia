plugins {
  kotlin("multiplatform")
  id("com.android.library")
}

kotlin {
  jvmToolchain(17)
  jvm()
  androidTarget()
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
  namespace = "me.strangepan.omnia"
  compileSdk = 33
  sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
  defaultConfig {
    minSdk = 26
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
}
