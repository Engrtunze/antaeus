plugins {
    kotlin("jvm")
}

kotlinProject()

dependencies {
    implementation(project(":pleo-antaeus-data"))
//    implementation ("io.jooby:jooby-quartz:2.13.0")
    implementation("org.quartz-scheduler:quartz:2.3.2")
    api(project(":pleo-antaeus-models"))
}