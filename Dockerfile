FROM openjdk:15-jre-slim

COPY ./target/trading-game-1.0-SNAPSHOT.jar .

EXPOSE 8080

CMD ["sh","-c","java -XX:InitialRAMPercentage=50 -XX:MaxRAMPercentage=70  -XshowSettings $JAVA_OPTS -jar trading-game-1.0-SNAPSHOT.jar"]
