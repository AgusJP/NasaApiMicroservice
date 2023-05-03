package com.prueba.apinasa.service;

import com.prueba.apinasa.dto.AsteroidDTO;
import com.prueba.apinasa.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.json.JsonValue;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AsteroidService {
    @Autowired
    RestTemplate restTemplate;

    /**
     * The api url is built with the indicated information and the request is made
     * @param days
     * @return request response (json)
     */
    public ResponseEntity<String> fetchAsteroidData(int days) {
        // Builder a Uri with the API url
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(Environment.API_BASE_URL)
                .queryParam("start_date", AsteroidService.getStartDate())
                .queryParam("end_date", AsteroidService.getEndDate(days))
                .queryParam("api_key", Environment.API_KEY);

        // Fetching response data (json) by restTemplate
        ResponseEntity<String> json = restTemplate.getForEntity(builder.toUriString(), String.class);
        return json;
    }

    /**
     * Sort by diameter size from largest to smallest
     * @param asteroidsDTOs
     * @return sorted asteroids list
     */
    public static List<AsteroidDTO> sortAsteroidsByDiameter(List<AsteroidDTO> asteroidsDTOs) {
        return asteroidsDTOs.stream()
                .sorted((asteroidDTO1, asteroidDTO2) -> Double.compare(asteroidDTO2.getAverageDiameter(), asteroidDTO1.getAverageDiameter()))
                .collect(Collectors.toList());
    }

    /**
     * Filter the list of asteroids to obtain the first 3, which are the largest and most dangerous
     * @param asteroidsDTOs
     * @return filtered asteroids list
     */
    public static List<AsteroidDTO> getLargestAndMostDangerousAsteroids(List<AsteroidDTO> asteroidsDTOs) {
        int numAsteroids = asteroidsDTOs.size() < 3 ? asteroidsDTOs.size() : 3;
        List<AsteroidDTO> asteroidDTOsFiltered = new ArrayList<>(asteroidsDTOs.subList(0, numAsteroids));
        return asteroidDTOsFiltered;
    }

    /**
     * @return formatted current date
     */
    public static String getCurrentDate() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formatDate = currentDate.format(format);

        return formatDate;
    }

    /**
     * @return formatted current date
     */
    public static String getStartDate() {
        return AsteroidService.getCurrentDate();
    }

    /**
     * @param days
     * @return the end date depending on the number of days
     */
    public static String getEndDate(int days) {
        LocalDate currentDate = LocalDate.parse(AsteroidService.getCurrentDate());
        LocalDate endDate = currentDate.plusDays(days);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String endFormatDate = endDate.format(format);

        return endFormatDate;
    }

    /**
     * Get required parameters from Api, then transform to asteroidDTO
     * @param asteroid
     * @return asteroidDTO
     */
    public AsteroidDTO extractAsteroidDTO(JsonValue asteroid) {
        boolean potentially_hazardous = asteroid.asJsonObject().getBoolean(Environment.POTENTIALLY_HAZARDOUS);
        //Filter asteroids by potentially hazardous
        if (!potentially_hazardous) {
            return null;
        }
        //Get required parameters from Api
        String name = asteroid.asJsonObject().getJsonString(Environment.NAME).getString();
        String speed = asteroid.asJsonObject().getJsonArray(Environment.APPROACH_DATA)
                .getJsonObject(0).getJsonObject(Environment.RELATIVE_VELOCITY).getJsonString(Environment.KILOMETER_HOUR).getString();
        String date = asteroid.asJsonObject().getJsonArray(Environment.APPROACH_DATA)
                .getJsonObject(0).getJsonString(Environment.APPROACH_DATE).getString();
        String orbitingPlanet = asteroid.asJsonObject().getJsonArray(Environment.APPROACH_DATA)
                .getJsonObject(0).getJsonString(Environment.ORBITING_BODY).getString();

        Double diameterMin = Double.valueOf(asteroid.asJsonObject().getJsonObject(Environment.ESTIMATED_DIAMETER)
                .getJsonObject(Environment.KILOMETERS).getJsonNumber(Environment.DIAMETER_MIN).toString());
        Double diameterMax = Double.valueOf(asteroid.asJsonObject().getJsonObject(Environment.ESTIMATED_DIAMETER)
                .getJsonObject(Environment.KILOMETERS).getJsonNumber(Environment.DIAMETER_MAX).toString());

        //Calculate the average diameter
        double averageDiameter = (diameterMin + diameterMax) / 2;

        //Transform api response to asteroidDTO
        return new AsteroidDTO(name, averageDiameter, speed, date, orbitingPlanet);
    }




}
