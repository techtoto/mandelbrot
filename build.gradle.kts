plugins {
    java
    application
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(16))
        vendor.set(JvmVendorSpec.ADOPTOPENJDK)
    }
}

application {
    mainClass.set("com.github.techtoto.mandelbrot.Main")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.slf4j:slf4j-api:2.0.2")
    implementation("ch.qos.logback:logback-classic:1.4.1")
    implementation("org.jetbrains:annotations:23.0.0")
}
