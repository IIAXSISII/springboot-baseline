# Maven build container 

FROM maven:3.6.3-openjdk-11 AS maven_build

COPY pom.xml /tmp/

COPY src /tmp/src/

WORKDIR /tmp/

RUN mvn package -Dmaven.test.skip=true 

#pull base image

FROM openjdk

#expose port 8080
EXPOSE 8080

#default command
CMD java -jar /data/springboot-baseline-0.1.jar

#copy hello world to docker image from builder image

COPY --from=maven_build /tmp/target/springboot-baseline-0.1.jar /data/springboot-baseline-0.1.jar
