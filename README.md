# secretVault

docker-compose up secretVault




Builder l'appli : ./gradlew build 
Lancer l'appli : java -jar build/libs/demo-0.0.1-SNAPSHOT.jar
Lancer l'appli depuis docker:
docker build --build-arg JAR_FILE=build/libs/*.jar -t springio/gs-spring-boot-docker .
docker run -p 8080:8080 springio/gs-spring-boot-docker


