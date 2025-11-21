plugins {
	java
	id("org.springframework.boot") version "3.3.4"
	id("io.spring.dependency-management") version "1.1.7"
	id("com.diffplug.spotless") version "6.25.0"
	id("checkstyle")
	id("jacoco")
}

group = "ingsis"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(21))
	}
}

configurations.all {
	exclude(group = "org.springframework.boot", module = "spring-boot-reactor")
	exclude(group = "org.springframework.boot", module = "spring-boot-starter-reactor")
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

	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	implementation("org.springframework.security:spring-security-oauth2-jose")
	implementation("org.springframework.security:spring-security-oauth2-core")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")

	runtimeOnly("org.postgresql:postgresql")
	testImplementation("com.h2database:h2")

	implementation("org.printScript.microservices:serializer:1.0.15")
	implementation("com.github.printSrcript:common:1.1.74")
	implementation("com.github.printSrcript:libs:1.1.74")
	implementation("com.github.printSrcript:factory:1.1.74")

	implementation("org.austral.ingsis:redis-streams-mvc:0.1.13")

	implementation("com.auth0:java-jwt:4.4.0")


	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")

	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.mockito:mockito-core:5.12.0")
	testImplementation("org.mockito:mockito-junit-jupiter:5.12.0")
	testImplementation("org.testcontainers:junit-jupiter:1.20.1")
	testImplementation("org.testcontainers:postgresql:1.20.1")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive:3.3.4")
	implementation("org.austral.ingsis:redis-streams-mvc:0.1.13") {
		exclude(group = "org.springframework.boot", module = "spring-boot-starter-data-redis-reactive")
		exclude(group = "org.springframework.boot", module = "spring-boot-starter")
	}
	implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive:3.3.4")
}

spotless {
	java {
		googleJavaFormat("1.23.0")
		importOrder("java", "javax", "org", "com", "")
		removeUnusedImports()
		target("src/**/*.java")
	}
}

/* ------------ Checkstyle ------------ */
checkstyle {
	toolVersion = "10.18.2"
	configFile = file("config/checkstyle/checkstyle.xml")
}

/* ------------ Jacoco ------------ */
jacoco {
	toolVersion = "0.8.12"
}

tasks.test {
	useJUnitPlatform()
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)
	reports {
		xml.required.set(true)
		html.required.set(true)
	}

	classDirectories.setFrom(
		files(
			classDirectories.files.map {
				fileTree(it) {
					include("**/services/**", "**/controllers/**")
				}
			}
		)
	)
}

tasks.jacocoTestCoverageVerification {
	dependsOn(tasks.jacocoTestReport)
	violationRules {
		rule {
			limit {
				minimum = "0.80".toBigDecimal()
			}
		}
	}
	classDirectories.setFrom(
		files(
			classDirectories.files.map {
				fileTree(it) {
					include("**/services/**", "**/controllers/**")
				}
			}
		)
	)
}

/* ------------ Integración de tasks ------------ */
tasks.check {
	dependsOn(
		"checkstyleMain",
		"checkstyleTest",
		"spotlessCheck",
		"jacocoTestCoverageVerification"
	)
}

tasks.build {
	dependsOn("spotlessApply")
}
