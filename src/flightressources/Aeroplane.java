package flightressources;
public class Aeroplane {

    private String model;
    private Double speed;
    private String Manufacturer;
    private Double fuelConsumption;

    
    public Aeroplane(String model, Double speed, String Manufacturer, Double fuelConsumption) {
    	setModel(model);
    	setSpeed(speed);
    	setManufacturer(Manufacturer);
    	setFuelConsumption(fuelConsumption);
    }
    
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public String getManufacturer() {
        return Manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        Manufacturer = manufacturer;
    }

    public Double getFuelConsumption() {
        return fuelConsumption;
    }

    public void setFuelConsumption(Double fuelConsumption) {
        this.fuelConsumption = fuelConsumption;
    }

}
