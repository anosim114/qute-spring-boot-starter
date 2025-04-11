import com.vanniktech.maven.publish.JavaLibrary
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.SonatypeHost

plugins {
  java
  id("org.springframework.boot") version "3.4.4"
//  id("distribution")
  id("maven-publish")
  id("idea")
  id("com.vanniktech.maven.publish") version "0.31.0"
}

group = "org.snemeis"
version = "0.1.6-SNAPSHOT"
description = "Starter of the Quarkus Qute templating engine for Spring Boot"

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(21)
  }
}

repositories {
  mavenCentral()
}

configurations {
  compileOnly {
    extendsFrom(configurations.annotationProcessor.get())
  }
}

dependencies {
  implementation("io.quarkus:quarkus-qute:3.21.1") {
    exclude("io.quarkus", "quarkus-core")
    exclude("io.quarkus", "quarkus-arc")
  }
  annotationProcessor("org.springframework.boot:spring-boot-autoconfigure-processor:3.4.4")
  annotationProcessor("org.projectlombok:lombok:1.18.38")
  compileOnly("org.projectlombok:lombok:1.18.38")
  implementation("commons-io:commons-io:2.18.0")
  implementation("org.slf4j:slf4j-api:2.0.17")
  implementation("org.springframework.boot:spring-boot-starter-web:3.4.4")
  testImplementation("org.springframework.boot:spring-boot-starter-test:3.4.4")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks {
  test {
    useJUnitPlatform()
  }

  jar {
    enabled = true
  }

  bootJar {
    enabled = false
  }
}

mavenPublishing {
  configure(JavaLibrary(
    javadocJar = JavadocJar.Javadoc(),
    sourcesJar = true
  ))

  // TODO: some configuration here is probably needed

  publishToMavenCentral(SonatypeHost.DEFAULT)

  signAllPublications()
}
