# 🌦️ WeatherController 🌦️

**WeatherController** to aplikacja stworzona w języku Java, która umożliwia automatyczne zarządzanie urządzeniami na podstawie danych pogodowych. Dzięki integracji z API pogodowym, aplikacja monitoruje zmiany warunków atmosferycznych (takie jak temperatura, wilgotność, prędkość wiatru).

## 🛠️ Technologie:

- **Java** – Język programowania.
- **Spring Framework** – Framework do budowy aplikacji.
- **Hibernate** – ORM do komunikacji z bazą danych.
- **Maven** – Narzędzie do zarządzania zależnościami.
- **API pogodowe** (wttr.in) – Źródło danych pogodowych.

## 💻 Funkcjonalność

Aplikacja umożliwia:

- **Dodawanie miast** i pobieranie danych pogodowych.
- **Aktualizowanie danych pogodowych** dla istniejących lokalizacji.
- **Usuwanie szczegółów pogodowych** dla miasta.
- **Generowanie statystyk pogodowych** (max, min, średnia temperatura, ciśnienie, wilgotność).
- **Zarządzanie danymi w bazie** za pomocą Hibernate.


## 🖥 API Endpoints

### 1. Dodanie miasta i jego danych pogodowych

**Opis**:
- Dodaje miasto i jego dane pogodowe, takie jak temperatura, wilgotność, ciśnienie, i inne.

**Zapytanie:**
```bash
GET http://localhost:8080/weather/addCity/Barcelona
```
```bash
{
  "city": "Barcelona",
  "country": "Spain",
  "latitude": 41.383,
  "longitude": 2.183,
  "weatherDetailsDTO": [
    {
      "ordinalNumber": 1,
      "feelsLikeC": 0.0,
      "cloudcover": 0.0,
      "humidity": 60.0,
      "localObsDateTime": "2025-04-11 06:08 PM",
      "pressure": 1017,
      "temp_C": 18.0,
      "uvIndex": 1,
      "visibility": 10
    }
  ]
}
```

   
3. **GET `/weather/{city}`**: Pobierz dane pogodowe dla miasta.
4. **PUT `/weather/{city}`**: Aktualizuj dane pogodowe dla miasta.
5. **DELETE `/weather/{weatherId}`**: Usuń dane pogodowe dla konkretnego ID.











## 📂 Struktura projektu

Projekt posiada następującą strukturę katalogów:

```bash
WeatherController/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── BazaDanychHibernate/
│   │   │               ├── controller/
│   │   │               │   └── WeatherController.java
│   │   │               ├── repository/
│   │   │               │   ├── LocationRepository.java
│   │   │               │   └── entity/
│   │   │               │       ├── LocationEntity.java
│   │   │               │       └── WeatherDetailsEntity.java
│   │   │               ├── service/
│   │   │               │   ├── WeatherService.java
│   │   │               │   ├── API/
│   │   │               │   │   └── WeatherInfo.java
│   │   │               │   ├── DTO/
│   │   │               │   │   ├── LocationDTO.java
│   │   │               │   │   ├── WeatherDetailsDTO.java
│   │   │               │   │   └── WeatherStatsDTO.java
│   │   │               │   ├── Errors/
│   │   │               │   │   ├── ClientException.java
│   │   │               │   │   ├── ErrorMessage.java
│   │   │               │   │   └── ServerException.java
│   │   │               │   └── Mapper/
│   │   │               │       └── WeatherMapper.java
│   │   ├── resources/
│   │   │   ├── application.properties
│   │   │   └── static/
│   │   │       └── (pliki statyczne)
├── pom.xml
```
- **controller** zawiera klasy odpowiedzialne za kontrolowanie logiki aplikacji (np. WeatherController).

- **repository** to miejsce, w którym przechowywane są klasy związane z dostępem do bazy danych, takie jak LocationRepository oraz encje (LocationEntity, WeatherDetailsEntity).

- **service** zawiera logikę biznesową (np. WeatherService), w tym klasy obsługujące wyjątki (Errors/), mapowanie danych (Mapper/), oraz obiekty transferu danych (DTO).

- **resources** zawiera konfigurację aplikacji (application.properties) oraz statyczne pliki (np. front-endowe).





