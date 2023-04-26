package com.prueba.apinasa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.prueba.apinasa.service.asteroidService;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;



import java.util.Map;

@RestController
@RequestMapping("/api")
public class asteroidsController {

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/asteroids")
    public void getAsteroids(@RequestParam("days") Long days) {
        ResponseEntity<String> json = restTemplate.getForEntity(
                "https://api.nasa.gov/neo/rest/v1/feed" +
                        "?start_date=" + asteroidService.getStartDate() + "&" +
                        "end_date=" + asteroidService.getEndDate(days) + "&api_key=zdUP8ElJv1cehFM0rsZVSQN7uBVxlDnu4diHlLSb",
                String.class);

        JsonReader reader = Json.createReader(new StringReader(json.getBody()));
        JsonObject jsonObject = reader.readObject();

        Object nearObjects = jsonObject.getJsonObject("near_earth_objects").getJsonArray(asteroidService.getStartDate());
        System.out.println(nearObjects);
        System.out.println(asteroidService.getEndDate(days));
    }

}
