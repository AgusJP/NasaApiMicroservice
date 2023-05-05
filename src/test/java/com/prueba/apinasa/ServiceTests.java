package com.prueba.apinasa;

import com.prueba.apinasa.dto.AsteroidDTO;
import com.prueba.apinasa.env.Environment;
import com.prueba.apinasa.service.AsteroidService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import javax.json.Json;
import javax.json.JsonObject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class ServiceTests {

    @Autowired
    AsteroidService asteroidService;

    @Test
    public void testGetCurrentDate() {
        String currentDate = asteroidService.getCurrentDate();
        // Verificar que el resultado es una cadena con formato yyyy-MM-dd
        Assertions.assertTrue(currentDate.matches("\\d{4}-\\d{2}-\\d{2}"));
    }

    @Test
    public void testGetEndDate() {
        int daysToAdd = 7;
        LocalDate currentDate = LocalDate.parse(AsteroidService.getCurrentDate());
        LocalDate endDate = currentDate.plusDays(daysToAdd);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String actualEndDate = endDate.format(format);

        String expectedEndDate = asteroidService.getEndDate(daysToAdd);

        Assertions.assertEquals(expectedEndDate, actualEndDate);
    }

    @Test
    public void testSortAsteroidsByDiameter() {
        AsteroidDTO asteroid1 = new AsteroidDTO("Asteroid1", 5, "7.5", "2023-05-04", "Earth");
        AsteroidDTO asteroid2 = new AsteroidDTO("Asteroid2", 6, "7.5", "2023-05-04", "Earth");
        AsteroidDTO asteroid3 = new AsteroidDTO("Asteroid3", 7, "7.5", "2023-05-04", "Earth");

        List<AsteroidDTO> asteroids = new ArrayList<>();
        asteroids.add(asteroid1);
        asteroids.add(asteroid2);
        asteroids.add(asteroid3);

        List<AsteroidDTO> sortedAsteroids = asteroidService.sortAsteroidsByDiameter(asteroids);

        Assertions.assertEquals(asteroid3, sortedAsteroids.get(0));
        Assertions.assertEquals(asteroid2, sortedAsteroids.get(1));
        Assertions.assertEquals(asteroid1, sortedAsteroids.get(2));
    }

    @Test
    public void testGetLargestAndMostDangerousAsteroids() {
        AsteroidDTO asteroid1 = new AsteroidDTO("Asteroid1", 9.0, "7.5", "2023-05-04", "Earth");
        AsteroidDTO asteroid2 = new AsteroidDTO("Asteroid2", 6.0, "7.5", "2023-05-04", "Earth");
        AsteroidDTO asteroid3 = new AsteroidDTO("Asteroid3", 8.0, "7.5", "2023-05-04", "Earth");
        AsteroidDTO asteroid4 = new AsteroidDTO("Asteroid4", 7.0, "7.5", "2023-05-04", "Earth");

        List<AsteroidDTO> asteroids = new ArrayList<>();
        asteroids.add(asteroid1);
        asteroids.add(asteroid2);
        asteroids.add(asteroid3);
        asteroids.add(asteroid4);

        List<AsteroidDTO> sortedAsteroids = asteroidService.sortAsteroidsByDiameter(asteroids);
        List<AsteroidDTO> filteredAsteroids = asteroidService.getLargestAndMostDangerousAsteroids(sortedAsteroids);

        Assertions.assertEquals(3, filteredAsteroids.size());
        Assertions.assertTrue(filteredAsteroids.contains(asteroid1));
        Assertions.assertFalse(filteredAsteroids.contains(asteroid2));
        Assertions.assertTrue(filteredAsteroids.contains(asteroid3));
        Assertions.assertTrue(filteredAsteroids.contains(asteroid4));
    }

    @Test
    public void testGetLargestAndMostDangerousAsteroidsWhenListIsLessThanThree() {
        AsteroidDTO asteroid1 = new AsteroidDTO("Asteroid1", 9.0, "7.5", "2023-05-04", "Earth");
        AsteroidDTO asteroid2 = new AsteroidDTO("Asteroid2", 6.0, "7.5", "2023-05-04", "Earth");

        List<AsteroidDTO> asteroids = new ArrayList<>();
        asteroids.add(asteroid1);
        asteroids.add(asteroid2);

        List<AsteroidDTO> sortedAsteroids = asteroidService.sortAsteroidsByDiameter(asteroids);
        List<AsteroidDTO> filteredAsteroids = asteroidService.getLargestAndMostDangerousAsteroids(sortedAsteroids);

        Assertions.assertTrue(filteredAsteroids.size() < 3);
        Assertions.assertTrue(filteredAsteroids.contains(asteroid1));
        Assertions.assertTrue(filteredAsteroids.contains(asteroid2));
    }

    @Test
    public void testFetchAsteroidData() {
        ResponseEntity<String> response = asteroidService.fetchAsteroidData(7);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.getBody().contains(Environment.NEAR_OBJECTS));
    }


     /** This test creates a JSON object simulating the API response,
     calls the extractAsteroidDTO function, and asserts the values of the resulting
     AsteroidDTO object to ensure that the values are correct. */
    @Test
    public void testExtractAsteroidDTO() {
        //Create a JSON object to simulate the response from the API
        JsonObject approachData = Json.createObjectBuilder()
                .add(Environment.RELATIVE_VELOCITY, Json.createObjectBuilder()
                        .add(Environment.KILOMETER_HOUR, "10000"))
                .add(Environment.APPROACH_DATE, "2023-05-10")
                .add(Environment.ORBITING_BODY, "Earth")
                .build();

        JsonObject estimatedDiameter = Json.createObjectBuilder()
                .add(Environment.KILOMETERS, Json.createObjectBuilder()
                        .add(Environment.DIAMETER_MIN, 100)
                        .add(Environment.DIAMETER_MAX, 200))
                .build();

        JsonObject asteroidObject = Json.createObjectBuilder()
                .add(Environment.POTENTIALLY_HAZARDOUS, true)
                .add(Environment.NAME, "Asteroid Test")
                .add(Environment.APPROACH_DATA, Json.createArrayBuilder()
                        .add(approachData))
                .add(Environment.ESTIMATED_DIAMETER, estimatedDiameter)
                .build();

        //Call the function to extract the DTO
        AsteroidDTO asteroidDTO = asteroidService.extractAsteroidDTO(asteroidObject);

        //Assert that the DTO is not null and has the correct values
        assertNotNull(asteroidDTO);
        assertEquals("Asteroid Test", asteroidDTO.getName());
        assertEquals(150.0, asteroidDTO.getAverageDiameter());
        assertEquals("10000", asteroidDTO.getSpeed());
        assertEquals("2023-05-10", asteroidDTO.getDate());
        assertEquals("Earth", asteroidDTO.getOrbitingPlanet());
    }

    @Test
    public void testExtractAsteroidDTOWhenNotPotentiallyHazardous() {
        //Create a JSON object to simulate the response from the API
        JsonObject asteroidObject = Json.createObjectBuilder()
                .add(Environment.POTENTIALLY_HAZARDOUS, false)
                .build();

        //Call the function to extract the DTO
        AsteroidDTO asteroidDTO = asteroidService.extractAsteroidDTO(asteroidObject);

        //Assert that the DTO is null
        assertNull(asteroidDTO);
    }

}
