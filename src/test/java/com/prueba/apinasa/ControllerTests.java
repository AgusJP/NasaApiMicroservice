package com.prueba.apinasa;

import com.prueba.apinasa.controller.AsteroidsController;
import com.prueba.apinasa.exceptions.BadRequestException;
import com.prueba.apinasa.service.AsteroidService;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControllerTests {

    @Test
    public void testGetAsteroidsWithInvalidDaysParameter() {
        AsteroidService mockAsteroidService = mock(AsteroidService.class);
        AsteroidsController controller = new AsteroidsController(mockAsteroidService);

        // Mock the response from the AsteroidService
        String invalidDaysJson = "{\"message\":\"The value of days parameter must be between 1 and 7\",\"code\":400}";
        ResponseEntity<String> mockResponseEntity = ResponseEntity.badRequest().body(invalidDaysJson);
        when(mockAsteroidService.fetchAsteroidData(8)).thenReturn(mockResponseEntity);

        // Verify that a BadRequestException is thrown when the days parameter is greater than 7
        assertThrows(BadRequestException.class, () -> {
            controller.getAsteroids(8);
        });
    }

}
