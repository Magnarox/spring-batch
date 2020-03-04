# spring-batch

Launch Spring Batch Job with remote partitioning with Kafka as middleware to ingest some csv files in a PostgreSQL database.

## Inspired by

* [Spring Batch Integration](https://docs.spring.io/spring-batch/docs/current-SNAPSHOT/reference/html/spring-batch-integration.html)

## Prepare environment

Modify `manager/src/main/docker/docker-compose.dependencies.yml` with your IP in Kafka config then launch this docker-compose file to run PostgreSQL and Kafka.

Apply `manager/src/main/resources/gen.sql` to the PostgreSQL database.

Update `manager/src/main/resources/application.yml` and `worker/src/main/resources/application.yml` with your environment values.

## Run Application

Launch 1 Manager and up to 3 Worker.

Start Batch process by entering `http://localhost:8090/api/start` in your favorite web browser. 

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.2.5.RELEASE/maven-plugin/)
* [Spring Batch](https://docs.spring.io/spring-boot/docs/2.2.5.RELEASE/reference/htmlsingle/#howto-batch-applications)
* [Spring for Apache Kafka](https://docs.spring.io/spring-boot/docs/2.2.5.RELEASE/reference/htmlsingle/#boot-features-kafka)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.2.5.RELEASE/reference/htmlsingle/#boot-features-developing-web-applications)

### Guides
The following guides illustrate how to use some features concretely:

* [Creating a Batch Service](https://spring.io/guides/gs/batch-processing/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)
