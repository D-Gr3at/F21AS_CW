package model;

import exception.InvalidAirportException;

import java.util.Objects;

public class Airport {

    private String name;
    private String code;
    private ControlTower controlTower;

    /*
     * Constructor of all private fields
     * */
    public Airport(String name, String code, ControlTower controlTower) throws InvalidAirportException {
	    setName(name);
	    setCode(code);
	    setControlTower(controlTower);
    }

    /*
     * Default constructor
     * */
    public Airport() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws InvalidAirportException {
    	if(name.length() == 0) throw new InvalidAirportException("Invalid airport name");
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) throws InvalidAirportException {
    	if(code.length() != 3) throw new InvalidAirportException("Invalid airport code");
        this.code = code;
    }

    public ControlTower getControlTower() {
        return controlTower;
    }

    public void setControlTower(ControlTower controlTower) {
        this.controlTower = controlTower;
    }
    
    @Override
    public String toString() {
    	return this.getCode();
    }
    
    @Override
    public boolean equals(Object o) {
    	if(!(o instanceof Airport)) return false;
    	
    	if(((Airport) o).getCode().equals(code)
    			&& ((Airport) o).getName().equals(name)
    			&& ((Airport) o).getControlTower().equals(controlTower)) {
    		return true;
    	}
    	
    	return false;
    }
    
    @Override
    public int hashCode() {
    	return Objects.hash(this.name, this.code, this.controlTower);
    }

}
