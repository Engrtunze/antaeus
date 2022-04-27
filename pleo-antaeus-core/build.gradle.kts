plugins {
    kotlin("jvm")
}

kotlinProject()

dependencies {
    implementation(project(":pleo-antaeus-data"))
    implementation("org.quartz-scheduler:quartz:2.3.2")
    api(project(":pleo-antaeus-models"))

    testImplementation ("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}