# Spring Cloud Lab (Eureka + Gateway + OpenFeign)

Java 17, Spring Boot 3.3.5, Spring Cloud 2023.0.3. Projet Maven multi-module.

## Ouvrir dans IntelliJ
File > Open > sélectionner le dossier `spring-cloud-lab` (le `pom.xml` racine). Attendre l'import Maven.

## Ordre de démarrage (4 configurations Run)
1. `EurekaServerApplication`  -> http://localhost:8761
2. `ClientApplication`        -> 8088
3. `VoitureApplication`       -> 8089
4. `GatewayApplication`      -> 8888

Astuce : View > Tool Windows > Services pour lancer/arrêter les 4 applications.
Attendre ~30 s après le démarrage pour que les services apparaissent dans Eureka.

## Tests
Voir `requests.http` (exécutable directement dans IntelliJ).

| URL | Résultat |
|---|---|
| :8888/clients, :8888/client/1 | via Gateway -> SERVICE-CLIENT |
| :8888/voitures | via Gateway -> SERVICE-VOITURE |
| :8888/SERVICE-CLIENT/clients | routage dynamique (discovery locator) |
| :8089/voitures/1 | voiture + client récupéré via OpenFeign |

Consoles H2 : http://localhost:8088/h2-console et http://localhost:8089/h2-console
(JDBC URL : `jdbc:h2:mem:clientdb` / `jdbc:h2:mem:voituredb`, user `sa`, mot de passe vide).

## Routage statique (étape 5)
Voir `gateway/src/main/resources/static-routes/application-static.yml.example`.
