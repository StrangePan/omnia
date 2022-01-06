plugins {
  kotlin("multiplatform")
  id("com.android.library")
}

project.setBuildDir("out")

kotlin {
  jvm()
  iosX64()
  iosArm64()
  android()

  sourceSets {
    val commonMain by getting {
      kotlin.srcDir("src/commonMain")
      dependencies {
        implementation(project(":omnia"))
        implementation(kotlin("test"))
        implementation("com.badoo.reaktive:reaktive:1.2.1")
        implementation("com.badoo.reaktive:reaktive-testing:1.2.1")
      }
    }

    val jvmMain by getting {
      kotlin.srcDir("src/jvmMain")
      dependencies {
        implementation("com.badoo.reaktive:reaktive-jvm:1.2.1")
      }
    }

    val iosX64Main by getting {
      kotlin.srcDir("src/iosX64Main")
      dependencies {
        implementation("com.badoo.reaktive:reaktive-iossim:1.2.0")
      }
    }

    val iosArm64Main by getting {
      kotlin.srcDir("src/iosArm64Main")
      dependencies {
        implementation("com.badoo.reaktive:reaktive-iosarm64:1.2.1")
      }
    }

    val androidMain by getting {
      kotlin.srcDir("src/androidMain")
      dependencies {
        implementation("com.badoo.reaktive:reaktive-android:1.2.1")
      }
    }
  }
}

android {
  compileSdk = 30
  sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
  defaultConfig {
    minSdk = 26
    targetSdk = 30
  }
}
