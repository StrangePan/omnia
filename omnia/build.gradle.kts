plugins {
  kotlin("multiplatform")
}

project.setBuildDir("out")

kotlin {
  jvm()
  iosX64()

  sourceSets {
    val commonMain by getting {
      kotlin.srcDir("src/commonMain")
      dependencies {
        implementation("com.badoo.reaktive:reaktive:1.2.0")
      }
    }

    val commonTest by getting {
      kotlin.srcDir("src/commonTest")
      dependencies {
        implementation(kotlin("test"))
        implementation(project(":omnia-test"))
        implementation("com.badoo.reaktive:reaktive-testing:1.2.0")
      }
    }

    val jvmMain by getting {
      kotlin.srcDir("src/jvmMain")
      dependencies {
        implementation("com.badoo.reaktive:reaktive-jvm:1.2.0")
      }
    }

    val jvmTest by getting {
      kotlin.srcDir("src/jvmTest")
      dependencies {
        implementation(kotlin("test-junit"))
      }
    }

    val iosX64Main by getting {
      kotlin.srcDir("src/iosX64Main")
      dependencies {
        implementation("com.badoo.reaktive:reaktive-iossim:1.2.0")
      }
    }

    iosX64Main.dependsOn(commonMain)
  }
}
