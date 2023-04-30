# Projet Sportser Kafka 

Binome : Ayoub BERHILI, Mohamed AHNICH

On a repris le projet Sportser d'Anas, Mehdi et Cédric pour l'introduction du pattern Event Sourcing en s'appuyant sur 
Kafka. 

Notre architecture

![image](https://user-images.githubusercontent.com/114408910/235380170-47321922-38a5-4499-88be-b46252c7f82b.png)

On s'appuie plus sur l'application mobile pour les évenemnts de souscription, on s'est focalisé sur l'architecture EDA.

### Pour l'envoie des données de batements de coeur

```
curl --location 'http://localhost:9004/epi-sport/api/sensor/hr' \
--header 'Content-Type: application/json' \
--data-raw '{
    "userEmail" : "duane.mariet@gmail.com",
    "heartRate" : 250,
    "time" : "2023-04-23T15:51:23"
}'

```

### Pour l'envoie des données de présence de prof

```
curl --location 'http://localhost:3030/api/v1/auth/authenticate' \
--header 'Content-Type: application/json' \
--data '{
    "username" : "alexandre.brenner",
    "password" : "alexandre.brenner"
}'

```

On lance les script python sous le répertoire mqtt-subscriber pour écouter les évenements envoyés à un prof

![image](https://user-images.githubusercontent.com/114408910/235380325-91be8d75-87f6-4245-bd85-5456d3ded3ec.png)
