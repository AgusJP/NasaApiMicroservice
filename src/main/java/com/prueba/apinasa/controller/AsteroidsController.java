package com.prueba.apinasa.controller;

import com.prueba.apinasa.dto.AsteroidDTO;
import com.prueba.apinasa.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.prueba.apinasa.service.AsteroidService;
import org.springframework.web.util.UriComponentsBuilder;

import javax.json.*;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AsteroidsController {

    @Autowired
    AsteroidService asteroidService;

    @GetMapping("/asteroids")
    public List<AsteroidDTO> getAsteroids(@RequestParam("days") int days) {

        //Json as response to api request
        ResponseEntity<String> json = asteroidService.fetchAsteroidData(days);

        //Read and transform json to jsonObject
        JsonReader reader = Json.createReader(new StringReader(json.getBody()));
        JsonObject jsonObject = reader.readObject();

        //List to store all asteroidsDTO
        List<AsteroidDTO> asteroidsDTOs = new ArrayList<>();

        //The jsonObject is traversed extracting the asteroid transformed data and added to the asteriodsDTO list
        //The first loop goes through the dates and the second the asteroids of each date
        for (int i = 0; i < days; i++) {
            JsonValue asteroidsArrayByDate = jsonObject.getJsonObject(Environment.NEAR_OBJECTS)
                    .getJsonArray(AsteroidService.getEndDate(i));

            for (JsonValue asteroid : asteroidsArrayByDate.asJsonArray()) {
                AsteroidDTO asteroidDTO = asteroidService.extractAsteroidDTO(asteroid);
                if (asteroidDTO != null) {
                    asteroidsDTOs.add(asteroidDTO);
                }
            }
        }

        //Sort by diameter size from largest to smallest
        asteroidsDTOs = asteroidService.sortAsteroidsByDiameter(asteroidsDTOs);
        //Filter the list of asteroids to obtain the first 3, which are the largest and most dangerous
        asteroidsDTOs = asteroidService.getLargestAndMostDangerousAsteroids(asteroidsDTOs);

        return asteroidsDTOs;
    }

}


