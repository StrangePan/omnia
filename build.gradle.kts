plugins {
  kotlin("jvm") version "1.5.31"
}

project.setBuildDir("out")

dependencies {
  api("io.reactivex.rxjava3:rxjava:3.0.8")
  api("com.badoo.reaktive:reaktive:1.2.0")
  api("com.badoo.reaktive:reaktive-jvm:1.2.0")
  api("com.badoo.reaktive:rxjava3-interop:1.2.0")
}

sourceSets {
  main {
    java.srcDir("src")
  }
  test {
    java.srcDir("tests")
    dependencies {
      implementation(kotlin("test"))
      implementation(kotlin("test-junit"))
      implementation("com.google.truth:truth:1.0")
      implementation("org.mockito:mockito-core:3.2.0")
      implementation("org.mockito.kotlin:mockito-kotlin:3.2.0")
      implementation("com.google.truth.extensions:truth-java8-extension:1.0")
      implementation("org.jetbrains.kotlin:kotlin-test:1.5.31")
      implementation("org.jetbrains.kotlin:kotlin-test-junit:1.5.31")
      implementation("com.badoo.reaktive:reaktive-testing:1.2.0")
    }
  }
}
