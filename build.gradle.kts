plugins {
    application
}

repositories {
    mavenCentral()
}

dependencies {}


tasks {
  named<JavaExec>("run") {
    standardInput = System.`in`
  }
}

application {
    mainClass.set("miniserver.App")
}
