package com.example.BazaDanychHibernate.service.Mapper;

import com.example.BazaDanychHibernate.repository.entity.LocationEntity;
import com.example.BazaDanychHibernate.repository.entity.WeatherDetailsEntity;
import com.example.BazaDanychHibernate.service.DTO.LocationDTO;
import com.example.BazaDanychHibernate.service.DTO.WeatherDetailsDTO;
import com.example.BazaDanychHibernate.service.API.WeatherInfo;
import com.example.BazaDanychHibernate.service.Errors.NoFoundDataException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WeatherMapper {


    public LocationDTO mapperToDTO(LocationEntity locationEntity) {
        if (locationEntity == null) {
            throw new NoFoundDataException("Brak danych location dto");
        }
        List<WeatherDetailsDTO> list = locationEntity.getWeatherDetailsEntities()
                .stream()
                .filter(weatherDetailsEntity -> weatherDetailsEntity != null)
                .map(weatherDetailsEntity -> mapToWeatherDetailsDTO(weatherDetailsEntity))
                .collect(Collectors.toList());

        LocationDTO location = new LocationDTO(
                locationEntity.getAreaName(),
                locationEntity.getCountry(),
                locationEntity.getLatitude(),
                locationEntity.getLongitude(),
                list
        );
         return  location;
    }

    public LocationEntity mapToLocationEntity(LocationDTO locationDTO) {
        if (locationDTO == null) {
            throw new NoFoundDataException("Brak danych location entity");
        }
        LocationEntity locationEntity = new LocationEntity(
                locationDTO.getCity(),
                locationDTO.getCountry(),
                locationDTO.getLatitude(),
                locationDTO.getLongitude());

        locationDTO.getWeatherDetailsDTO()
                .forEach(weatherDetailsDTO -> {
                    WeatherDetailsEntity weatherDetailsEntity = mapToWeatherDetailsEntity(weatherDetailsDTO);
                    locationEntity.addWeatherDetails(weatherDetailsEntity);
                });

        return locationEntity;
    }
    public WeatherDetailsDTO mapToWeatherDetailsDTO(WeatherDetailsEntity weatherDetailsEntity) {
        if (weatherDetailsEntity == null) {
            throw  new NoFoundDataException("Brak danych weather details");
        }
        return new WeatherDetailsDTO(
                weatherDetailsEntity.getOrdinalNumber(),
                weatherDetailsEntity.getFeelsLikeC(),
                weatherDetailsEntity.getCloudCover(),
                weatherDetailsEntity.getHumidity(),
                weatherDetailsEntity.getLocalObsDateTime(),
                weatherDetailsEntity.getPressure(),
                weatherDetailsEntity.getTemp_C(),
                weatherDetailsEntity.getUvIndex(),
                weatherDetailsEntity.getVisibility());
    }

    public WeatherDetailsEntity mapToWeatherDetailsEntity(WeatherDetailsDTO weatherDetailsDTO) {
        if (weatherDetailsDTO == null) {
            throw new NoFoundDataException("Brak danych weather details");
        }
        return new WeatherDetailsEntity(
                weatherDetailsDTO.getOrdinalNumber(),
                weatherDetailsDTO.getFeelsLikeC(),
                weatherDetailsDTO.getCloudcover(),
                weatherDetailsDTO.getHumidity(),
                weatherDetailsDTO.getLocalObsDateTime(),
                weatherDetailsDTO.getPressure(),
                weatherDetailsDTO.getTemp_C(),
                weatherDetailsDTO.getUvIndex(),
                weatherDetailsDTO.getVisibility());
    }

    public LocationEntity mapToLocationEntityFromWeatherInfo(WeatherInfo info) {


        WeatherInfo.CrurrentCondition currentCondition = info.getCurrent_condition().get(0);
        WeatherInfo.NearestArea nearestArea = info.getNearest_area().get(0);
        List<WeatherInfo.NearestArea.AreaName> areaNameList = nearestArea.getAreaName();
        List<WeatherInfo.NearestArea.Country> countryList = nearestArea.getCountry();

        if (nearestArea == null || currentCondition == null) {
            throw new NoFoundDataException("Brak wymaganych danych");
        } else {

            if (areaNameList != null && !areaNameList.isEmpty()
                    && countryList != null && !countryList.isEmpty()) {

                String areaName = areaNameList.get(0).getValue();
                String country = countryList.get(0).getValue();

                if (country.isEmpty() || areaName.isEmpty()){
                    throw new NoFoundDataException("Brak wymaganych danych");
                } else {
                     return mapToLocationEntityFromWeatherInfo(areaName,country,nearestArea,currentCondition);
                }
            }
        }
        throw new NoFoundDataException("Brak wymaganych danych");
    }

    public LocationEntity mapToLocationEntityFromWeatherInfo (String areaName, String country,
      WeatherInfo.NearestArea nearestArea, WeatherInfo.CrurrentCondition currentCondition) {

        LocationEntity locationEntity= new LocationEntity(
                areaName,
                country,
                nearestArea.getLatitude(),
                nearestArea.getLongitude()
        );

        WeatherDetailsEntity weatherDetailsEntity = new WeatherDetailsEntity(
                0,
                currentCondition.getFeelsLikeC(),
                currentCondition.getCloudcover(),
                currentCondition.getHumidity(),
                currentCondition.getLocalObsDateTime(),
                currentCondition.getPressure(),
                currentCondition.getTemp_C(),
                currentCondition.getUvIndex(),
                currentCondition.getVisibility()
        );

        locationEntity.addWeatherDetails(weatherDetailsEntity);
        return locationEntity;


    }

    public  WeatherDetailsEntity mapToWeatherDetailsEntityFromWeatherInfo(WeatherInfo info) {

        WeatherInfo.CrurrentCondition currentCondition = info.getCurrent_condition().get(0);
        if (currentCondition == null) {

            throw new NoFoundDataException("Brak wymaganych danych");

        } else {
            WeatherDetailsEntity weatherDetailsEntity = new WeatherDetailsEntity(
                    0,
                    currentCondition.getFeelsLikeC(),
                    currentCondition.getCloudcover(),
                    currentCondition.getHumidity(),
                    currentCondition.getLocalObsDateTime(),
                    currentCondition.getPressure(),
                    currentCondition.getTemp_C(),
                    currentCondition.getUvIndex(),
                    currentCondition.getVisibility()
            );
            return weatherDetailsEntity;
        }

    }

    public WeatherDetailsEntity mapToWeatherDetailsEntityFromLocationDTO(LocationDTO locationDTO) {
        if (locationDTO == null) {
            throw new NoFoundDataException("Brak danych w loacation dto");
        }
        WeatherDetailsDTO weatherDetailsDTO = locationDTO.getWeatherDetailsDTO().get(0);
        if (weatherDetailsDTO == null) {
            throw  new NoFoundDataException("Brak danych w weather details dto");
        }
        WeatherDetailsEntity weatherDetailsEntity = new WeatherDetailsEntity(
                0,
                weatherDetailsDTO.getFeelsLikeC(),
                weatherDetailsDTO.getCloudcover(),
                weatherDetailsDTO.getHumidity(),
                weatherDetailsDTO.getLocalObsDateTime(),
                weatherDetailsDTO.getPressure(),
                weatherDetailsDTO.getTemp_C(),
                weatherDetailsDTO.getUvIndex(),
                weatherDetailsDTO.getVisibility()
        );

        return weatherDetailsEntity;
    }

}

