package flightressources;

import exception.InvalidAirportException;

public class Airport {

    private String name;
    private String code;
    private ControlTower controlTower;
    
    public Airport(String name, String code, ControlTower controlTower) throws InvalidAirportException {
    	try {
	    	setName(name);
	    	setCode(code);
	    	setControlTower(controlTower);
    	} catch (InvalidAirportException iae) {
    		throw iae;
    	}
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

}
