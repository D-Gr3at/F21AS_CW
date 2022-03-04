import flightressources.GPSCoordinate;
import org.junit.jupiter.api.Test;
public class TestJunit {
    @Test

    public void testGPSCoordinate() {
        GPSCoordinate coordinates = new GPSCoordinate("164°57'12\"E", "77°30'36\"S");
        assertEquals(getLatitudeInDegree(), -77.51f);
        assertEquals(getLongitudeInRadian(), 164.97f);
        assertEquals(getLatitudeInRadian(), 1.35f);
        assertEquals(getLongitudeInRadian(), 2.88f);
    }
}