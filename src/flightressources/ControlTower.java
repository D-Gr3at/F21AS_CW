package flightressources;
import exception.ResourceNotFoundException;

import java.util.List;

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

    public Double distanceBetweenControlTower(ControlTower otherControlTower) throws ResourceNotFoundException {
        GPSCoordinate gpsCoordinate = this.getCoordinates();
        Double latitudeInRadian = gpsCoordinate.getLatitudeInRadian();
        Double longitudeInRadian = gpsCoordinate.getLongitudeInRadian();
        GPSCoordinate otherControlTowerCoordinates = otherControlTower.getCoordinates();
        if (otherControlTowerCoordinates == null){
            throw new ResourceNotFoundException("GPS coordinates not found.");
        }
        Double otherLatitudeInRadian = otherControlTowerCoordinates.getLatitudeInRadian();
        Double otherLongitudeInRadian = otherControlTowerCoordinates.getLongitudeInRadian();
        double deltaLongitude = otherLongitudeInRadian - longitudeInRadian;
        double deltaLatitude = otherLatitudeInRadian - latitudeInRadian;
        double trig = Math.pow(Math.sin(deltaLatitude / 2), 2.0) + Math.cos(latitudeInRadian)
                * Math.cos(otherLatitudeInRadian) * Math.pow(Math.sin(deltaLongitude / 2), 2.0);

        return  2 * 6371.00 * Math.asin(Math.sqrt(trig));
    }
    
    public boolean compareTo(ControlTower otherControlTower) {
    	
    	if(otherControlTower.getCoordinates()
    						.getLatitude()
    						.equals(this.getCoordinates().getLatitude())
    	   &&
    	   otherControlTower.getCoordinates()
    	   					.getLongitude()
    	   					.equals(this.getCoordinates().getLongitude())) {
    			
    		return true;
    	}
    	
    	
    	return false;
    }
}
