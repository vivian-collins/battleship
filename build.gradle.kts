plugins {
    java
    application
    id("com.diffplug.spotless") version "6.25.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(22) // Replace with your desired version
        vendor = JvmVendorSpec.AMAZON
    }
    withJavadocJar()
    withSourcesJar()
}
repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-core:5.14.2")
    implementation("org.jline:jline:3.27.1")
}

tasks.withType(Test::class) {
    useJUnitPlatform()
}

application {
    mainClass = "Main"
    applicationDefaultJvmArgs += "--enable-native-access=ALL-UNNAMED"
}

tasks.named<JavaExec>("run") {
    mainClass = "Main"
    standardInput = System.`in`
    jvmArgs("--enable-native-access=ALL-UNNAMED")
}

spotless {
    java {
        importOrder()
        removeUnusedImports()
        cleanthat()
        googleJavaFormat().aosp().reflowLongStrings()
    }
}
