FROM java:8
MAINTAINER  Yuri Ivanov <yura.ivanov@gmail.com>
EXPOSE 8080
COPY target/game-server-0.0.1-SNAPSHOT.jar /game-server/game-server-0.0.1-SNAPSHOT.jar
CMD java -version
CMD ["java", "-jar", "/game-server/game-server-0.0.1-SNAPSHOT.jar"]
