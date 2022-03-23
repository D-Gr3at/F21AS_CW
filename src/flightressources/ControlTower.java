package flightressources;

import exception.ResourceNotFoundException;

/**
 * Describe the Control Tower class
 * @author  Carl-Olivier N'Diaye (cn2013@hw.ac.uk)
 * @version v1.0
 * @since   07 Feb 2022
 */

public class ControlTower {
    private GPSCoordinate coordinates;

    public ControlTower(GPSCoordinate newCoordinates) throws NumberFormatException {
    	coordinates = newCoordinates;
    }

    public ControlTower(String longitude, String latitude) throws NumberFormatException {
        coordinates = new GPSCoordinate(longitude, latitude);
    }

    public GPSCoordinate getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(GPSCoordinate newCoordinates) throws NumberFormatException {
        this.coordinates.setLongitude(newCoordinates.getLongitude());
        this.coordinates.setLatitude(newCoordinates.getLatitude());
    }

    public void setCoordinates(String longitude, String latitude) throws NumberFormatException {
    	coordinates.setLongitude(longitude);
	    coordinates.setLatitude(latitude);
    }

    public Double distanceBetweenControlTower(ControlTower otherControlTower) throws ResourceNotFoundException {
        GPSCoordinate otherControlTowerCoordinates = otherControlTower.getCoordinates();
        if (otherControlTowerCoordinates == null){
            throw new ResourceNotFoundException("GPS coordinates not found.");
        }        
        return this.getCoordinates().circleDistance(otherControlTowerCoordinates);
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
