package com.prueba.apinasa.controller;

import com.prueba.apinasa.dto.AsteroidDTO;
import com.prueba.apinasa.env.Environment;
import com.prueba.apinasa.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.prueba.apinasa.service.AsteroidService;

import javax.json.*;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AsteroidsController {

    @Autowired
    AsteroidService asteroidService;

    public AsteroidsController(AsteroidService asteroidService) {
        this.asteroidService = asteroidService;
    }

    @GetMapping("/asteroids")
    public List<AsteroidDTO> getAsteroids(@RequestParam("days") int days) {

       if (days < 1 || days > 7) {
            throw new BadRequestException("The value of days parameter must be between 1 and 7");
        }
        //Json as response to api request
        ResponseEntity<String> json = asteroidService.fetchAsteroidData(days);

        //Read and transform json to jsonObject
        JsonReader reader = Json.createReader(new StringReader(json.getBody()));
        JsonObject jsonObject = reader.readObject();

        //List to store all asteroidsDTO
        List<AsteroidDTO> asteroidsDTOs = new ArrayList<>();

        //The jsonObject is traversed extracting the asteroid transformed data and added to the asteriodsDTO list
        //The first loop goes through the dates (days) that contain the list of asteroids and the second loop extract the asteroids of each date
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


