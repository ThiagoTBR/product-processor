FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY target/product-processor-0.0.1-SNAPSHOT.jar product-processor.jar
ENTRYPOINT ["java","-jar","/product-processor.jar"]
