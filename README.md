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

- ✅ Dodawanie miast i pobieranie dla nich danych pogodowych z API.

- 🔁 Aktualizacja danych pogodowych w bazie.

- 🗑️ Usuwanie danych pogodowych dla danego miasta lub wpisu.

- 📈 Generowanie statystyk pogodowych (min/max/średnia).

- 🔍 Wyszukiwanie lokalizacji i danych pogodowych.

- 🗃️ Zarządzanie bazą danych z wykorzystaniem Hibernate (JPA).

## 🔒 Bezpieczeństwo i filtrowanie danych
- **Mapowanie danych:** Aplikacja przetwarza dane za pomocą mapowania, np. w metodach mapperToDTO i mapToWeatherDetailsDTO, aby tylko odpowiednie informacje (np. temperatura, wilgotność) były udostępniane frontendowi, unikając wrażliwych danych.

- **Filtrowanie danych:** W przypadku brakujących lub niekompletnych danych (np. pustych wartości) aplikacja rzuca wyjątek NoFoundDataException, zapobiegając wyświetlaniu niepożądanych informacji.

- **Bezpieczny dostęp:** Dzięki warstwie mapowania (DTO), dane są odpowiednio przetwarzane i udostępniane użytkownikowi, co zapewnia, że tylko dozwolone dane są widoczne.






## Instalacja 🚀

Aby zainstalować i skonfigurować projekt na swoim komputerze, wykonaj następujące kroki:

1. **Skopiuj repozytorium na swój komputer** 📂:
    ```bash
    git clone https://github.com/your-repo/weather-api.git
    ```

2. **Przejdź do folderu projektu** 📁:
    ```bash
    cd weather-api
    ```

3. **Zainstaluj wymagane zależności** 🔧:
    Używając Node.js i npm:
    ```bash
    npm install
    ```

4. **Uruchom aplikację** ⚡:
    Aby uruchomić lokalnie aplikację, wykonaj polecenie:
    ```bash
    npm start
    ```
    Aplikacja powinna być dostępna pod adresem `http://localhost:8080`. 🌍

### 🧪 Uwaga: Do testowania aplikacji wymagany jest Postman lub inny klient REST API.

## 🖥 API Endpoints

### 1.Pobranie danych pogodowych z API dla danego miasta

**Opis: Pobiera dane pogodowe z zewnętrznego API dla podanego miasta (np. temperatura, wilgotność, ciśnienie). Dane są zapisywane w bazie danych i przypisane do konkretnej lokalizacji.**

**Zapytanie:**
```bash
GET http://localhost:8080/weather/addCity/Barcelona
```
**Body:**
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
      "humidity": 88.0,
      "localObsDateTime": "2025-04-12 06:21 PM",
      "pressure": 1013,
      "temp_C": 18.0,
      "uvIndex": 1,
      "visibility": 10
    }
  ]
}
```
**MySQL:**
```sql
select * from location_entity;
```
![Zrzut ekranu 2025-04-12 121808](https://github.com/user-attachments/assets/aff416d1-a8cd-4b52-9c35-3f80153ac75e)

```sql
select * from weather_details_entity;
```
![Zrzut ekranu 2025-04-12 121841](https://github.com/user-attachments/assets/78b9d82f-f9f1-41bc-830c-070cc9152cc5)



### 2. Ręczne dodanie miasta i jego danych pogodowych

**Opis: Dodaje nowe miasto wraz z ręcznie wprowadzonymi danymi pogodowymi. Jeśli wpis z tą samą datą i godziną już istnieje, zostaje zaktualizowany.**

**Zapytanie:**
```bash
GET http://localhost:8080/weather/create
```

**Body:**
```bash
{
    "city": "Dziwnów",
    "country": "Poland",
    "latitude": 54.01,
    "longitude": 14.44,
    "weatherDetailsDTO": [
        {
            "ordinalNumber": 1,
            "feelsLikeC": 9.0,
            "cloudcover": 48.0,
            "humidity": 88.0,
            "localObsDateTime": "2025-04-12 11:40 AM",
            "pressure": 1015,
            "temp_C": 12.0,
            "uvIndex": 0,
            "visibility": 10
        }
    ]
}
```
**MySQL:**
```sql
select * from location_entity;
```
![Zrzut ekranu 2025-04-12 121937](https://github.com/user-attachments/assets/77b7c710-dde3-4c18-ad23-75d2de1af1b9)
```sql
select * from weather_details_entity;
```
![Zrzut ekranu 2025-04-12 122023](https://github.com/user-attachments/assets/37b34d74-5437-44b7-b891-6aaf3ee44be5)





















### 3. Aktualizacja danych pogodowych
**Opis: Aplikacja pozwala na aktualizację danych pogodowych dla danego miasta, ale w przypadku ponownego dodania tego samego miasta (np. z nowymi danymi pogodowymi), zostaje utworzony nowy wpis w bazie danych. System traktuje to jako nową aktualizację, a nie modyfikację istniejącego wpisu.**

**Przykładowo, jeśli użytkownik doda miasto "Barcelona" ponownie, pojawi się nowy wpis z tymi samymi danymi, ale z nowym ordinalNumber oraz datą i godziną, co pozwala na śledzenie historii zmian w danych pogodowych.**


### 🔍 Przed aktualizacją:

**Zapytanie:**
```bash
GET http://localhost:8080/weather/addCity/Barcelona
```

**Body:**
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
            "humidity": 88.0,
            "localObsDateTime": "2025-04-12 06:21 PM",
            "pressure": 1013,
            "temp_C": 18.0,
            "uvIndex": 1,
            "visibility": 10
        },
        {
            "ordinalNumber": 2,
            "feelsLikeC": 0.0,
            "cloudcover": 75.0,
            "humidity": 77.0,
            "localObsDateTime": "2025-04-12 12:30 PM",
            "pressure": 1013,
            "temp_C": 17.0,
            "uvIndex": 4,
            "visibility": 10
        }
    ]
}
```


**MySQL:**
```sql
select * from location_entity;
```
![Zrzut ekranu 2025-04-12 121808](https://github.com/user-attachments/assets/aff416d1-a8cd-4b52-9c35-3f80153ac75e)

```sql
select * from weather_details_entity;
```
![Zrzut ekranu 2025-04-12 131738](https://github.com/user-attachments/assets/68052270-b580-4dd4-9e1f-093b7d1ce9fa)


### ✅ Po aktualizacji:

**Zapytanie:**

**Opis: W tej operacji aktualizujemy tylko drugi wpis dla miasta Barcelona, ponieważ w bazie danych nie mamy wcześniej zdefiniowanej temperatury odczuwalnej (feelsLikeC). Chcemy dodać brakującą wartość temperatury odczuwalnej dla tego wpisu.**

```bash
GET http://localhost:8080/weather/addCity/Barcelona/2
```

**Body:**
```bash
{
      "ordinalNumber": 2,
      "feelsLikeC": 15.0,  <--  wpisujemy temperature odczuwalną
      "cloudcover": 75.0,
      "humidity": 77.0,
      "localObsDateTime": "2025-04-12 12:30 PM",
      "pressure": 1013,
      "temp_C": 17.0,
      "uvIndex": 4,
      "visibility": 10
}
```


**MySQL:**
```sql
select * from location_entity;
```
![Zrzut ekranu 2025-04-12 121937](https://github.com/user-attachments/assets/77b7c710-dde3-4c18-ad23-75d2de1af1b9)
```sql
select * from weather_details_entity;
```
![Zrzut ekranu 2025-04-12 132153](https://github.com/user-attachments/assets/81a31bb9-c675-41a8-a3d4-3100e59c3015)


### 4. Statystyki pogodowe dla miasta
**Opis: Zwraca statystyki pogodowe dla danego miasta, obliczając średnie, maksymalne i minimalne wartości temperatury, ciśnienia i wilgotności na podstawie zapisanych danych.**

**Zapytanie:**

```bash
GET http://localhost:8080/weather/stats/Barcelona
```

**Body:**
```bash
{
  {
    "maxTemp": 18.0,
    "minTemp": 17.0,
    "averageTemp": 17.5,
    "maxPressure": 1013.0,
    "minPressure": 1013.0,
    "averagePressure": 1013.0,
    "maxHumidity": 88.0,
    "minHumidity": 77.0,
    "averageHumidity": 82.5
}
}
```

### 5.Wyszukiwanie danych pogodowych po nazwie miasta

**Opis: Zwraca wszystkie zapisane dane pogodowe dla wybranego miasta, wraz ze szczegółami każdej obserwacji.**


**Zapytanie:**
```bash
GET http://localhost:8080/weather/show/Barcelona
```
  
### 6. Usuwanie danych pogodowych po ID
**Opis: Usuwa wybrane dane pogodowe na podstawie podanego identyfikatora (weatherId) z bazy danych.**


**Zapytanie:**
```bash
DELETE http://localhost:8080/weather/delete/1
```
### 🔍 Przed usunieciem .

**MySQL:**
```sql
select * from location_entity;
```
![Zrzut ekranu 2025-04-12 123732](https://github.com/user-attachments/assets/e136db2a-88e4-4584-9ff8-11d635776774)

```sql
select * from weather_details_entity;
```
![Zrzut ekranu 2025-04-12 134707](https://github.com/user-attachments/assets/3202c2ff-55d4-4e33-82b2-e93ab81e4c04)

### ✅ Po usunieciu 


**MySQL:**
```sql
select * from location_entity;
```

![Zrzut ekranu 2025-04-12 134811](https://github.com/user-attachments/assets/9b66a191-0751-4566-80c1-c5de21a9ecb6)

```sql
select * from weather_details_entity;
```

![Zrzut ekranu 2025-04-12 134844](https://github.com/user-attachments/assets/d89952f1-93dd-4be3-998f-5c2f50cbb2ec)



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
### ⚠️ Komentarz do API

Obecne API może nie zapewniać pełnej **dokładności danych** pogodowych, ponieważ część danych (np. temperatura odczuwalna) jest wprowadzana ręcznie. Aby zwiększyć precyzję i spójność, warto rozważyć **integrację z zewnętrznymi źródłami danych pogodowych**.



