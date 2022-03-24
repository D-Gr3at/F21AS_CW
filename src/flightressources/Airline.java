package flightressources;

import java.util.Objects;

import exception.InvalidAirlineException;
public class Airline {

    private String name;
    private String code;
    
    public Airline(String name, String code) throws InvalidAirlineException {
    	setName(name);
    	setCode(code);
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
    	if(code.length() > 3 || code.length() < 2) throw new InvalidAirlineException("Invalid airline code");
    	this.code = code;
    }
    
    @Override
    public String toString() {
    	return this.getName();
    }
    
    @Override
    public boolean equals(Object o) {
    	if(!(o instanceof Airline)) return false;
    	if(code.compareTo(((Airline)  o).getCode()) == 0
    			&& 
    			name.compareTo(((Airline) o).getName()) == 0) {
    		return true;
    	}
    	return false;
    }
    
    @Override
    public int hashCode() {
    	return Objects.hash(code, name);
    }

}
