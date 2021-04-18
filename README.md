$ .\mvnw install && .\mvnw spring-boot:run -pl service

http://localhost:8080/swagger-ui.html

spring.jpa.show-sql=true 

docker build service --tag plasticconsumptionservice

docker run -d -p 8080:8080 plasticconsumptionservice