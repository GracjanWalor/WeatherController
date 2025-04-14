# 🌦️ WeatherController 🌦️

**WeatherController** is an application created in Java that allows automatic management of devices based on weather data. By integrating with a weather API, the application monitors changes in atmospheric conditions (such as temperature, humidity, wind speed).

## 🛠️ Technologies:

- **Java** – Programming language.
- **Spring Framework** – Framework for building applications.
- **Hibernate** – ORM for database communication.
- **Maven** – Dependency management tool.
- **Weather API** (wttr.in) – Weather data source.

## 💻 Functionality

The application allows for:

- ✅ Adding cities and retrieving weather data for them from the API.
- 🔁 Updating weather data in the database.
- 🗑️ Deleting weather data for a specific city or entry.
- 📈 Generating weather statistics (min/max/average).
- 🔍 Searching for locations and weather data.
- 🗃️ Managing the database using Hibernate (JPA).

## 🔒 Security and Data Filtering

- **Data Mapping:** The application processes data through mapping (e.g., in the methods `mapperToDTO` and `mapToWeatherDetailsDTO`) to ensure that only relevant information (e.g., temperature, humidity) is provided to the frontend, avoiding sensitive data.
  
- **Data Filtering:** In the case of missing or incomplete data (e.g., empty values), the application throws a `NoFoundDataException`, preventing the display of unwanted information.
  
- **Secure Access:** Thanks to the mapping layer (DTO), data is properly processed and provided to the user, ensuring that only allowed data is visible.



## Installation 🚀

To install and set up the project on your computer, follow these steps:

1. **Clone the repository to your computer** 📂:
    ```bash
    git clone https://github.com/your-repo/weather-api.git
    ```

2. **Go to the project folder** 📁:
    ```bash
    cd weather-api
    ```

3. **Install required dependencies** 🔧:
    Using Node.js and npm:
    ```bash
    npm install
    ```

4. **Run the application** ⚡:
    To run the app locally, use the following command:
    ```bash
    npm start
    ```
    The app should be available at `http://localhost:8080`. 🌍

### 🧪 Note: Postman or another REST API client is required for testing the application.

## 🖥 API Endpoints

### 1. Retrieve weather data from the API for a given city

**Description:** Retrieves weather data from an external API for the given city (e.g., temperature, humidity, pressure). The data is saved in the database and associated with a specific location.

**Request:**
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



### 2. Manually add a city and its weather data

**Description:** Adds a new city with manually entered weather data. If an entry with the same date and time already exists, it gets updated.



**Request:**
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


### 3. Weather Data Update
**Description:** The application allows for updating weather data for a given city, but when the same city is added again (e.g., with new weather data), a new entry is created in the database. The system treats this as a new update, not a modification of the existing entry.

**For example, if the user adds the city "Barcelona" again, a new entry will appear with the same data but with a new ordinalNumber, along with a new date and time, allowing for tracking changes in the weather data over time.**

### 🔍 Request Before Update:

**Request:**
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


### ✅ After Update:

**Request:**

**Description:** In this operation, we are only updating the second entry for the city of Barcelona because the database does not have the "feelsLikeC" (feels-like temperature) value previously defined. We want to add the missing "feelsLikeC" value for this entry.

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

### 4. Weather Statistics for a City
**Description:** Returns weather statistics for a given city by calculating the average, maximum, and minimum values of temperature, pressure, and humidity based on the recorded data.

**Request:**

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

### 5. Weather Data Search by City Name

**Description:** Returns all recorded weather data for the selected city, along with the details of each observation.

**Request:**
```bash
GET http://localhost:8080/weather/show/Barcelona
```
  
### 6. Deleting Weather Data by ID

**Description:** Deletes selected weather data based on the provided identifier (weatherId) from the database.

**Request:**

```bash
DELETE http://localhost:8080/weather/delete/1
```
### 🔍 Before Removal
**MySQL:**
```sql
select * from location_entity;
```
![Zrzut ekranu 2025-04-12 123732](https://github.com/user-attachments/assets/e136db2a-88e4-4584-9ff8-11d635776774)

```sql
select * from weather_details_entity;
```
![Zrzut ekranu 2025-04-12 134707](https://github.com/user-attachments/assets/3202c2ff-55d4-4e33-82b2-e93ab81e4c04)

### ✅ After Removal


**MySQL:**
```sql
select * from location_entity;
```

![Zrzut ekranu 2025-04-12 134811](https://github.com/user-attachments/assets/9b66a191-0751-4566-80c1-c5de21a9ecb6)

```sql
select * from weather_details_entity;
```

![Zrzut ekranu 2025-04-12 134844](https://github.com/user-attachments/assets/d89952f1-93dd-4be3-998f-5c2f50cbb2ec)



## 📂 Project Structure

The project has the following directory structure:


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
### ⚠️ API Comment

The current API may not provide complete **accuracy of weather data**, as some information (e.g., feels-like temperature) is entered manually. To improve precision and consistency, it is recommended to **integrate with external weather data sources**.



