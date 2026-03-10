# Instrukcja uruchomienia aplikacji ChatRoom

Ta instrukcja opisuje kroki potrzebne do uruchomienia aplikacji Spring Boot ChatRoom lokalnie.

## Wymagania wstępne
- Java 17+
- Maven (lub użyj `./mvnw` wrapper)
- Docker (do uruchomienia Kafki)

## Krok 1: Zbuduj projekt
Uruchom w głównym katalogu projektu:
```
./mvnw clean package -DskipTests
```
Lub jeśli masz zainstalowanego Mavena:
```
mvn clean package -DskipTests
```

## Krok 2: Uruchom Kafkę (wymagane dla funkcjonalności czatu)
Uruchom Zookeepera:
```
docker run -d --name zookeeper -p 2181:2181 -e ZOOKEEPER_CLIENT_PORT=2181 -e ZOOKEEPER_TICK_TIME=2000 confluentinc/cp-zookeeper:7.4.0
```

Uruchom Kafkę:
```
docker run -d --name kafka --link zookeeper:zookeeper -p 9092:9092 -e KAFKA_BROKER_ID=1 -e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 -e KAFKA_LISTENER_SECURITY_PROTOCOL_MAP=PLAINTEXT:PLAINTEXT -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 confluentinc/cp-kafka:7.4.0
```

Sprawdź status kontenerów:
```
docker ps
```

## Krok 3: Uruchom aplikację
```
java -jar target/PJATK_CHATROOM-0.0.1-SNAPSHOT.jar
```

## Krok 4: Dostęp do aplikacji
Otwórz przeglądarkę i przejdź na: `http://localhost:8080`

Zaloguj się jednym z użytkowników:
- Michal / test
- Agnieszka / test
- Krzysztof / test
- Laura / test
- Kate / test

## Zatrzymanie aplikacji
- Zatrzymaj aplikację: Ctrl+C w terminalu
- Zatrzymaj kontenery Docker: `docker stop kafka zookeeper && docker rm kafka zookeeper`

## Testowanie
- Otwórz aplikację w kilku zakładkach przeglądarki jako różni użytkownicy
- Wysyłaj wiadomości – powinny pojawiać się w czasie rzeczywistym</content>
<parameter name="filePath">c:\Users\x\Documents\GitHub\PJATK\PJATK_CHATROOM\run.md