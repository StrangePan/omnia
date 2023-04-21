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

  @Suppress("UNUSED_VARIABLE")
  sourceSets {
    val commonMain by getting {
      kotlin.srcDir("src/commonMain")
      dependencies {
        api(libraries.reaktive.core)
      }
    }
    val commonTest by getting {
      kotlin.srcDir("src/commonTest")
      dependencies {
        implementation(kotlin("test"))
        implementation(project(":omnia-test"))
        api(libraries.reaktive.testing)
      }
    }
    val javaMain by creating {
      kotlin.srcDir("src/javaMain")
      dependsOn(commonMain)
    }
    val jvmMain by getting {
      kotlin.srcDir("src/jvmMain")
      dependsOn(javaMain)
      dependencies {
        api(libraries.reaktive.jvm)
      }
    }
    val jvmTest by getting {
      kotlin.srcDir("src/jvmTest")
      dependencies {
        implementation(kotlin("test-junit"))
      }
    }
    val androidMain by getting {
      kotlin.srcDir("src/androidMain")
      dependsOn(javaMain)
    }
    val iosMain by creating {
      kotlin.srcDir("src/iosMain")
      dependsOn(commonMain)
    }
    val iosX64Main by getting {
      kotlin.srcDir("src/iosX64Main")
      dependsOn(iosMain)
      dependencies {
        api(libraries.reaktive.iosx64)
      }
    }
    val iosArm64Main by getting {
      kotlin.srcDir("src/iosArm64Main")
      dependsOn(iosMain)
      dependencies {
        api(libraries.reaktive.iosarm64)
      }
    }
    val iosSimulatorArm64Main by getting {
      kotlin.srcDir("src/iosSimulatorArm64Main")
      dependsOn(iosMain)
      dependencies {
        api(libraries.reaktive.iossimulatorarm64)
      }
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
