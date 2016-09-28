FROM anapsix/alpine-java:8
VOLUME /tmp
ADD webapp-0.0.1-SNAPSHOT.jar spring-boot-app.jar
RUN bash -c 'touch /spring-boot-app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/spring-boot-app.jar"]