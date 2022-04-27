## Antaeus

Antaeus (/ænˈtiːəs/), in Greek mythology, a giant of Libya, the son of the sea god Poseidon and the Earth goddess Gaia. He compelled all strangers who were passing through the country to wrestle with him. Whenever Antaeus touched the Earth (his mother), his strength was renewed, so that even if thrown to the ground, he was invincible. Heracles, in combat with him, discovered the source of his strength and, lifting him up from Earth, crushed him to death.

Welcome to our challenge.

## The challenge

As most "Software as a Service" (SaaS) companies, Pleo needs to charge a subscription fee every month. Our database contains a few invoices for the different markets in which we operate. Your task is to build the logic that will schedule payment of those invoices on the first of the month. While this may seem simple, there is space for some decisions to be taken and you will be expected to justify them.

## Instructions

Fork this repo with your solution. Ideally, we'd like to see your progression through commits, and don't forget to update the README.md to explain your thought process.

Please let us know how long the challenge takes you. We're not looking for how speedy or lengthy you are. It's just really to give us a clearer idea of what you've produced in the time you decided to take. Feel free to go as big or as small as you want.

## Developing

Requirements:
- \>= Java 11 environment

Open the project using your favorite text editor. If you are using IntelliJ, you can open the `build.gradle.kts` file, and it is gonna setup the project in the IDE for you.

### Building

```
./gradlew build
```

### Running

There are 2 options for running Anteus. You either need libsqlite3 or docker. Docker is easier but requires some docker knowledge. We do recommend docker though.

*Running Natively*

Native java with sqlite (requires libsqlite3):

If you use homebrew on MacOS `brew install sqlite`.

```
./gradlew run
```

*Running through docker*

Install docker for your platform

```
docker build -t antaeus
docker run antaeus
```

### App Structure
The code given is structured as follows. Feel free however to modify the structure to fit your needs.
```
├── buildSrc
|  | gradle build scripts and project wide dependency declarations
|  └ src/main/kotlin/utils.kt 
|      Dependencies
|
├── pleo-antaeus-app
|       main() & initialization
|
├── pleo-antaeus-core
|       This is probably where you will introduce most of your new code.
|       Pay attention to the PaymentProvider and BillingService class.
|
├── pleo-antaeus-data
|       Module interfacing with the database. Contains the database 
|       models, mappings and access layer.
|
├── pleo-antaeus-models
|       Definition of the Internal and API models used throughout the
|       application.
|
└── pleo-antaeus-rest
        Entry point for HTTP REST API. This is where the routes are defined.
```

### Main Libraries and dependencies
* [Exposed](https://github.com/JetBrains/Exposed) - DSL for type-safe SQL
* [Javalin](https://javalin.io/) - Simple web framework (for REST)
* [kotlin-logging](https://github.com/MicroUtils/kotlin-logging) - Simple logging framework for Kotlin
* [JUnit 5](https://junit.org/junit5/) - Testing framework
* [Mockk](https://mockk.io/) - Mocking library
* [Sqlite3](https://sqlite.org/index.html) - Database storage engine

Happy hacking 😁!

### Development Documentation Ride 🤓

##Billing Scheduler

* BillingSchedulerService is an independent scheduler for billing which is initiated or initialized by the MainSchedulerConfig.
* MainSchedulerConfig handles all scheduler service in the application but at this point we only have the BillingSchedulerService,
this integration has :
  * Job – Represents the actual job to be executed
  * JobDetail – Conveys the detail properties of a given Job instance
  * 
    Trigger – Triggers are the mechanism by which Jobs are scheduled

##Billing Scheduler Suggestion
In most use cases, we would want to disallow the execution of more than one instances of the same job at the same time, to prevent race conditions on saved data. This might occur when the jobs take too long to finish or are triggered too often.

In order to properly diagnose and trace issues in applications that use Quartz well Any code that gets executed inside jobs must be logged.Quartz has its own logs when an event occurs i.e. a scheduler gets created, a job gets executed etc.

Scheduler monitor/manager like [QuartzDesk](https://www.quartzdesk.com/) is important when the application is deployed to live quartz scheduler GUI helps us manage and monitor Quartz schedulers, jobs and triggers in all types of Java applications.


##Billing Service
* billPendingInvoice method: This method fetches all pending invoice from the database in list then adds each of the pending invoice into the billService method to be charged.
* billInvoices method: This calls the payment provider that will charge the invoice when the payment provider service response is true the pending invoices get charged <br> and get updated to paid using the invoice status enum class. <br> Also when charging the invoice some exceptions are caught which are important to the system such as network exception which I implemented a retry mechanism with 2 maximum numbers of retries and also after every 10secs. 

##Billing Service Suggestion
- The invoiceStatus enum class can also have failed status because if there are some exceptions are caught when charging the invoice it is better to update the invoice to failed in other to keep track of failed records and having a good monitoring system like bugsnag or sentry will help in monitoring our exceptions better.
- This will lead us to create a fetchAllFailedInvoice method and then add it to the billInvoice method to charge maybe after 3 - 5 days of charging the pending request invoice to charge failed invoice.


##EXTRAS
Additional Dependencies :-
* Quartz scheduler dependencies - quartz scheduler for scheduling.
* junit.jupiter dependencies - for running unit test.

Additional Methods to Existing class <br>

  AntaeusDal and InvoiceService  class : <br> 
* fetchAllPendingInvoice -> Fetches all pending invoices.
* updateInvoiceStatus -> Updates invoice status.