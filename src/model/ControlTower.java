package model;

import exception.InvalidAirportException;
import exception.ResourceNotFoundException;
import io.FileManager;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

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

    /*
    * This method calculates the distance between this control tower and another
    * @throws ResourceNotFoundException
    * */
    public Double distanceBetweenControlTower(ControlTower otherControlTower) throws ResourceNotFoundException {
        GPSCoordinate otherControlTowerCoordinates = otherControlTower.getCoordinates();
        if (otherControlTowerCoordinates == null){
            throw new ResourceNotFoundException("GPS coordinates not found.");
        }        
        return this.getCoordinates().circleDistance(otherControlTowerCoordinates);
    }

    /*
     * Custom equals() implementation
     * */
    @Override
    public boolean equals(Object o) {
    	if(!(o instanceof ControlTower)) return false;
    	
    	if(((ControlTower) o).getCoordinates().equals(coordinates)) {
    		return true;
    	}
    	
    	return false;
    }
    
    @Override
    public int hashCode() {
    	return Objects.hash(this.coordinates);
    }

    public Airport getCorrespondingAirport() throws IOException, InvalidAirportException {
    	List<Airport> airports = FileManager.loadAirports();
    	for(Airport airport: airports) {
    		if(airport.getControlTower().equals(this)) {
    			return airport;
    		}
    	}
		return null;
    }
}
