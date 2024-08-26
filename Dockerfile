FROM openjdk:17

ADD target/invoice-tracker-management.jar invoice-tracker-management.jar

COPY target/invoice-tracker-management.jar invoice-tracker-management.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","invoice-tracker-management.jar"]