# Utilise l'image Docker openjdk version 17 avec une distribution slim comme image de base.
FROM openjdk:17-jdk-slim

# Expose le port 8080 pour que votre application soit accessible à l'extérieur du conteneur Docker.
EXPOSE 6652
# Déclare une variable d'argument de construction nommée JAR_FILE qui pointe vers votre fichier JAR dans le répertoire target.
ARG JAR_FILE=target/*.jar

# Copie le fichier JAR de votre hôte vers le conteneur Docker et le renomme en app.jar.
COPY ${JAR_FILE} app.jar

# Définit la commande à exécuter lorsque le conteneur Docker est lancé.
ENTRYPOINT ["java", "-jar", "/app.jar"]
