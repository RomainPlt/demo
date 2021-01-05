# secretVault

secretVault will allow you to push and get secret to an H2 database.

localhost:8080/secretVault/pushsecrets
localhost:8080/secretVault/getsecrets

Linked to a scone cas/las and a session, you will be able to read secrets pushed only from the session:
localhost:8080/secretVault/secret

In the Dockerfile-trusted are all the fspf files needed to protect the H2 database. If you don't want to use the scone stack, change the dockerfile used in build to Dockerfile-untrusted. This way the app will still boot but you won't be able to read session's secrets.


build with gradle  : ./gradlew build
build with docker  : docker build --file Dockerfile-(un)trusted --build-arg JAR_FILE=build/libs/*.jar -t romainplt/secretVault .
run docker-compose : docker-compose up secretVault

