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
    implementation("org.slf4j:slf4j-api:1.7.32")
    implementation("ch.qos.logback:logback-classic:1.2.5")
    implementation("org.jetbrains:annotations:21.0.1")
}
