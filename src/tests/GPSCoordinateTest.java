package tests;

import model.GPSCoordinate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GPSCoordinateTest {
    @Test
    public void testGPSCoordinate() {
        GPSCoordinate coordinates = new GPSCoordinate("164°57'12\"E", "77°30'36\"S");
        assertEquals(coordinates.getLatitudeInDegree(), -77.51f);
        assertEquals(coordinates.getLongitudeInRadian(), 164.97f);
        assertEquals(coordinates.getLatitudeInRadian(), 1.35f);
        assertEquals(coordinates.getLongitudeInRadian(), 2.88f);
    }
}