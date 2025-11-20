plugins {
	java
	id("org.springframework.boot") version "3.5.6"
	id("io.spring.dependency-management") version "1.1.7"
	jacoco
	id("com.diffplug.spotless") version "6.25.0"
	checkstyle
}

group = "ingsis"
version = "0.0.1-SNAPSHOT"
description = "ingsis project for Spring Boot"

java {
	toolchain { languageVersion = JavaLanguageVersion.of(21) }
}

repositories {
	mavenCentral()

	maven {
		name = "GitHubPackagesPrintScript"
		url = uri("https://maven.pkg.github.com/Pedrodeforonda/printScript-ingsis")
		credentials {
			username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
			password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
		}
	}

	maven {
		name = "GitHubPackagesAustral"
		url = uri("https://maven.pkg.github.com/austral-ingsis/class-redis-streams")
		credentials {
			username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
			password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
		}
	}

	maven {
		name = "GitHubPackagesSerializer"
		url = uri("https://maven.pkg.github.com/sonpipe0/spring-serializer")
		credentials {
			username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
			password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
		}
	}
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	implementation("org.springframework.security:spring-security-oauth2-jose")


	implementation("org.springframework.boot:spring-boot-starter-web")        // Web clásico
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")

	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	runtimeOnly("org.postgresql:postgresql")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.testcontainers:junit-jupiter:1.20.1")
	testImplementation("org.testcontainers:postgresql:1.20.1")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation("org.printScript.microservices:serializer:1.0.15")
	implementation("com.github.printSrcript:common:1.1.74")
	implementation("com.github.printSrcript:libs:1.1.74")
	implementation("com.github.printSrcript:factory:1.1.74")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
	implementation("com.auth0:java-jwt:4.4.0")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	// Reactivo (para ReactiveRedisTemplate y la lib de redis-streams que usabas antes)
	implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")

	// Lib de streams de Austral
	implementation("org.austral.ingsis:redis-streams-mvc:0.1.13")

	// Serializer viejo (FormatSerializer, LintSerializer, etc.)
	implementation("org.printScript.microservices:serializer:1.0.15")
}

tasks.withType<Test> { useJUnitPlatform() }

/* -------- Coverage (JaCoCo) -------- */
jacoco { toolVersion = "0.8.12" }
tasks.jacocoTestReport {
	dependsOn(tasks.test)
	reports { xml.required.set(true); html.required.set(true) }
}

/* -------- Formatter (Spotless) -------- */
spotless {
	java {
		googleJavaFormat()
		target("src/**/*.java")
	}
}

/* -------- Linter (Checkstyle) -------- */
checkstyle {
	toolVersion = "10.18.1"
	// Usá 'config' (no configFile) para evitar deprecations en Gradle modernos
	config = resources.text.fromFile("config/checkstyle/checkstyle.xml")
}
dependencies {
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	implementation("org.springframework.security:spring-security-oauth2-jose")
	implementation("org.springframework.security:spring-security-oauth2-core")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}
