plugins {
    id ("java")
    id ("org.springframework.boot") version "4.0.5"
}

apply(plugin = "io.spring.dependency-management")

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation ("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly ("org.postgresql:postgresql")

    implementation ("org.springframework.boot:spring-boot-starter-security")

    implementation("io.jsonwebtoken:jjwt-api:0.13.0")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.13.0")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.13.0")
}