package flightressources;

import exception.InvalidAirlineException;
public class Airline {

    private String name;
    private String code;
    
    public Airline(String name, String code) throws InvalidAirlineException {
    	try {
	    	setName(name);
	    	setCode(code);
    	} catch (InvalidAirlineException iae) {
    		throw iae;
    	}
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws InvalidAirlineException{
    	if(name.length() == 0) throw new InvalidAirlineException("Invalid Airline name");
    	this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) throws InvalidAirlineException{
    	if(code.length() > 3 || code.length() == 0) throw new InvalidAirlineException("Invalid airline code");
    	this.code = code;
    }
    
    @Override
    public String toString() {
    	return this.getName();
    }

}
