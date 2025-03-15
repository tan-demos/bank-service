plugins {
	java
	id("org.springframework.boot") version "3.4.3"
	id("io.spring.dependency-management") version "1.1.7"
	id("io.freefair.lombok") version "8.10.2"
}

group = "com.homework"
version = "1.0.0"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("com.google.code.gson:gson:2.10.1")
	implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.4")
	implementation("org.postgresql:postgresql:42.7.4")
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-aop")
	implementation("org.springframework.boot:spring-boot-starter-log4j2")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.retry:spring-retry")
	implementation("org.springframework.kafka:spring-kafka")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")
	// Spring Boot Starter for Redis (includes Spring Data Redis and Lettuce by default)
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")


	// Micrometer and Spring Boot Actuator are closely related but serve distinct roles in the monitoring and observability ecosystem. Their relationship is complementary: Actuator provides the infrastructure to expose monitoring data, while Micrometer provides the metrics collection and instrumentation framework that Actuator leverages

	// Spring Boot Starter for Actuator (includes metrics support)
	implementation("org.springframework.boot:spring-boot-starter-actuator")

	// Micrometer Prometheus registry (optional, for Prometheus integration)
	implementation("io.micrometer:micrometer-registry-prometheus")

	implementation(files("libs/bank-service-api-1.0.0.jar"))

	compileOnly("javax.servlet:javax.servlet-api:4.0.1")
	runtimeOnly("org.apache.logging.log4j:log4j-layout-template-json")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

}

configurations.all {
	exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
}

tasks.withType<Test> {
	useJUnitPlatform()
	testLogging {
		events("passed", "skipped", "failed")
	}
}
