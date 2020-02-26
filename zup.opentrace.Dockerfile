FROM adoptopenjdk/maven-openjdk11
WORKDIR /usr/src/zup-opentrace
COPY . /usr/src/zup-opentrace/
RUN mvn package

WORKDIR /usr/src/zup-opentrace
RUN cp /usr/src/zup-opentrace/target/*.jar ./zup-opentrace.jar
EXPOSE 80
CMD ["java", "-jar", "zup-opentrace.jar"]
