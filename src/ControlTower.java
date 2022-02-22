/**
 * Describe the Control Tower class
 * @author  Carl-Olivier N'Diaye (cn2013@hw.ac.uk)
 * @version v1.0
 * @since   07 Feb 2022
 */

public final class ControlTower {
    private GPSCoordinate coordinates;

    public ControlTower(GPSCoordinate newCoordinates) {
        coordinates.setLongitude(newCoordinates.getLongitude());
        coordinates.setLatitude(newCoordinates.getLatitude());
    }

    public ControlTower(String longitude, String latitude) {
        coordinates = new GPSCoordinate(longitude, latitude);
    }

    public GPSCoordinate getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(GPSCoordinate newCoordinates) {
        coordinates.setLongitude(newCoordinates.getLongitude());
        coordinates.setLatitude(newCoordinates.getLatitude());
    }

    public void setCoordinates(String longitude, String latitude) {
        coordinates.setLongitude(longitude);
        coordinates.setLatitude(latitude);
    }
}
