package flightressources;

import exception.InvalidPlaneException;

public class Aeroplane {

    private String model;
    private Double speed;
    private String Manufacturer;
    private Double fuelConsumption;

    
    public Aeroplane(String model, Double speed, String Manufacturer, Double fuelConsumption) throws InvalidPlaneException {
    	try {
			setModel(model);
	    	setSpeed(speed);
	    	setManufacturer(Manufacturer);
	    	setFuelConsumption(fuelConsumption);
    	} catch (InvalidPlaneException ipe) {
    		throw ipe;
    	}
    }
    
    public String getModel() {
        return model;
    }

    public void setModel(String model) throws InvalidPlaneException{
    	if(model.length() == 0) throw new InvalidPlaneException("Invalid plane model");
        this.model = model;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) throws InvalidPlaneException{
    	if(speed < 250.0) throw new InvalidPlaneException("Invalid speed");
        this.speed = speed;
    }

    public String getManufacturer() {
        return Manufacturer;
    }

    public void setManufacturer(String manufacturer) throws InvalidPlaneException{
    	if(manufacturer.length() == 0) throw new InvalidPlaneException("Invalid manufacturer");
        Manufacturer = manufacturer;
    }

    public Double getFuelConsumption() {
        return fuelConsumption;
    }

    public void setFuelConsumption(Double fuelConsumption) throws InvalidPlaneException{
    	if(fuelConsumption < 500.0) throw new InvalidPlaneException("Invalid fuel consumption");
        this.fuelConsumption = fuelConsumption;
    }
    
    @Override
    public String toString() {
    	return this.getModel();
    }

}
