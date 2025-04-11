package com.example.BazaDanychHibernate.service;

import com.example.BazaDanychHibernate.repository.LocationRepository;
import com.example.BazaDanychHibernate.repository.entity.LocationEntity;
import com.example.BazaDanychHibernate.repository.entity.WeatherDetailsEntity;
import com.example.BazaDanychHibernate.service.API.WeatherInfo;
import com.example.BazaDanychHibernate.service.DTO.LocationDTO;
import com.example.BazaDanychHibernate.service.DTO.WeatherDetailsDTO;
import com.example.BazaDanychHibernate.service.DTO.WeatherStatsDTO;
import com.example.BazaDanychHibernate.service.Errors.ClientException;
import com.example.BazaDanychHibernate.service.Errors.ErrorMessage;
import com.example.BazaDanychHibernate.service.Errors.ServerException;
import com.example.BazaDanychHibernate.service.Mapper.WeatherMapper;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WeatherService {

    private final RestTemplate restTemplate ;
    private final LocationRepository locationRepository;
    private final WeatherMapper weatherMapper;

    public WeatherService(RestTemplate restTemplate, LocationRepository locationRepository, WeatherMapper weatherMapper) {
        this.restTemplate = restTemplate;
        this.locationRepository = locationRepository;
        this.weatherMapper = weatherMapper;
    }

    public LocationDTO addCity(String city) {
        String url = "https://wttr.in/" + city + "?format=j1";
        WeatherInfo weatherInfo = null;
        try {
           weatherInfo = restTemplate.getForObject(url, WeatherInfo.class);

        } catch (RestClientException rest) {
            throw new ClientException("Nie ma takiego miasta", 404);
        } catch (Exception exception) {
            throw new ServerException("Inny blad", 500);
        }

        LocationEntity findLocation = locationRepository.findByAreaName(city);

        if (findLocation == null) {

            LocationEntity newLocationEntity = weatherMapper.mapToLocationEntityFromWeatherInfo(weatherInfo);
            int listSize = newLocationEntity.getWeatherDetailsEntities().size();
            if (listSize > 0) {
                newLocationEntity.getWeatherDetailsEntities().get(listSize - 1).setOrdinalNumber(listSize);
            }

            LocationEntity saved = locationRepository.save(newLocationEntity);
            return weatherMapper.mapperToDTO(saved);


        } else  {
            WeatherDetailsEntity findUpdate = weatherMapper.mapToWeatherDetailsEntityFromWeatherInfo(weatherInfo);
            findLocation.addWeatherDetails(findUpdate);
            int listSize = findLocation.getWeatherDetailsEntities().size();
            System.out.println(listSize);
            if (listSize > 0) {
                findLocation.getWeatherDetailsEntities().get(listSize - 1).setOrdinalNumber(listSize);

            }

            LocationEntity saved = locationRepository.saveAndFlush(findLocation);
            return weatherMapper.mapperToDTO(saved);
        }
    }
    public LocationDTO createWeather(LocationDTO locationDTO) {
        try {
            LocationEntity findLocation = locationRepository.findByAreaName(locationDTO.getCity());

            if (findLocation == null) {
                LocationEntity locationEntity = weatherMapper.mapToLocationEntity(locationDTO);
                int listSize = locationEntity.getWeatherDetailsEntities().size();
                if (listSize > 0) {
                    locationEntity.getWeatherDetailsEntities().get(listSize - 1).setOrdinalNumber(listSize);
                }
                LocationEntity saved = locationRepository.saveAndFlush(locationEntity);
                return weatherMapper.mapperToDTO(saved);
            } else {
                WeatherDetailsEntity newWeatherDetails = weatherMapper.mapToWeatherDetailsEntityFromLocationDTO(locationDTO);
                findLocation.addWeatherDetails(newWeatherDetails);
                int listSize = findLocation.getWeatherDetailsEntities().size();
                if (listSize > 0) {
                    findLocation.getWeatherDetailsEntities().get(listSize - 1).setOrdinalNumber(listSize);
                }
                LocationEntity saved = locationRepository.saveAndFlush(findLocation);
                return weatherMapper.mapperToDTO(saved);
            }
        } catch (Exception e) {
            throw new ServerException("Blad podczas tworzenia pogody", 500);
        }
    }

    public LocationDTO deleteWeatherDetails(Long weatherId) {
        LocationEntity locationEntity = locationRepository.findById(weatherId).orElse(null);

        if (locationEntity == null ){
            throw new ClientException("Nie ma takiego ID w bazie danych", 404);
        } else {
            locationRepository.deleteById(weatherId);
            return weatherMapper.mapperToDTO(locationEntity);
        }

    }

    public LocationDTO findByCity(String city) {
        try {
            LocationEntity findLocation = locationRepository.findByAreaName(city);
            if (findLocation == null) {
                throw new ClientException("Nie znaleziono miasta", 404);
            }
            return weatherMapper.mapperToDTO(findLocation);
        } catch (Exception e) {
            throw new ServerException("Blad podczas wyszukiwania miasta", 500);
        }
    }

    public LocationDTO updateDetails(String city, int ordinalNumber, WeatherDetailsDTO updateWeatherDetails) {
        try {
            LocationEntity locationEntity = locationRepository.findByAreaName(city);
            if (locationEntity == null) {
                throw new ClientException("Nie znaleziono miasta", 404);
            }

            WeatherDetailsEntity weatherDetailsEntity = locationEntity.getWeatherDetailsEntities()
                    .stream()
                    .filter(w -> w.getOrdinalNumber() == ordinalNumber)
                    .findFirst()
                    .orElse(null);

            if (weatherDetailsEntity == null) {
                throw new ClientException("Nie znaleziono szczegołow pogody", 404);
            }

            weatherDetailsEntity.setFeelsLikeC(updateWeatherDetails.getFeelsLikeC());
            weatherDetailsEntity.setCloudCover(updateWeatherDetails.getCloudcover());
            weatherDetailsEntity.setHumidity(updateWeatherDetails.getHumidity());
            weatherDetailsEntity.setLocalObsDateTime(updateWeatherDetails.getLocalObsDateTime());
            weatherDetailsEntity.setPressure(updateWeatherDetails.getPressure());
            weatherDetailsEntity.setTemp_C(updateWeatherDetails.getTemp_C());
            weatherDetailsEntity.setUvIndex(updateWeatherDetails.getUvIndex());
            weatherDetailsEntity.setVisibility(updateWeatherDetails.getVisibility());

            LocationEntity updatedEntity = locationRepository.save(locationEntity);
            return weatherMapper.mapperToDTO(updatedEntity);
        } catch (ClientException e) {
            throw e;
        } catch (Exception e) {
            throw new ServerException("Blad podczas aktualizacji szczegułow pogody", 500);
        }
    }

    public WeatherStatsDTO getTemperaturesStats(String city) {
        LocationEntity locationEntity = locationRepository.findByAreaName(city);

        if (locationEntity == null) {
            return null;

        } else {
            List<WeatherDetailsEntity> weatherDetailsEntities = locationEntity.getWeatherDetailsEntities();
            WeatherStatsDTO weatherStatsDTO = collectStats(weatherDetailsEntities);
            return weatherStatsDTO;
        }
    }

    private WeatherStatsDTO collectStats(List<WeatherDetailsEntity> weatherDetailsEntities) {
        try {
            if (weatherDetailsEntities == null || weatherDetailsEntities.isEmpty()) {
                throw new ClientException("Brak danych pogodowych do analizy", 404);
            }

            double maxTemp = weatherDetailsEntities.stream()
                    .mapToDouble(WeatherDetailsEntity::getTemp_C).max().orElse(0.0);

            double minTemp = weatherDetailsEntities.stream()
                    .mapToDouble(WeatherDetailsEntity::getTemp_C).min().orElse(0.0);

            double averageTemp = weatherDetailsEntities.stream()
                    .mapToDouble(WeatherDetailsEntity::getTemp_C).average().orElse(0.0);

            double maxPressure = weatherDetailsEntities.stream()
                    .mapToDouble(WeatherDetailsEntity::getPressure).max().orElse(0.0);

            double minPressure = weatherDetailsEntities.stream()
                    .mapToDouble(WeatherDetailsEntity::getPressure).min().orElse(0.0);

            double averagePressure = weatherDetailsEntities.stream()
                    .mapToDouble(WeatherDetailsEntity::getPressure).average().orElse(0.0);

            double maxHumidity = weatherDetailsEntities.stream()
                    .mapToDouble(WeatherDetailsEntity::getHumidity).max().orElse(0.0);

            double minHumidity = weatherDetailsEntities.stream()
                    .mapToDouble(WeatherDetailsEntity::getHumidity).min().orElse(0.0);

            double averageHumidity = weatherDetailsEntities.stream()
                    .mapToDouble(WeatherDetailsEntity::getHumidity).average().orElse(0.0);

            return new WeatherStatsDTO(
                    maxTemp,
                    minTemp,
                    averageTemp,
                    maxPressure,
                    minPressure,
                    averagePressure,
                    maxHumidity,
                    minHumidity,
                    averageHumidity
            );
        } catch (Exception e) {
            throw new ServerException("Bldd podczas obliczania statystyk pogodowych", 500);
        }
    }

}



