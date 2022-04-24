plugins {
    kotlin("jvm")
}

kotlinProject()

dependencies {
    implementation(project(":pleo-antaeus-data"))
    implementation ("io.jooby:jooby-quartz:2.13.0")
    api(project(":pleo-antaeus-models"))
}