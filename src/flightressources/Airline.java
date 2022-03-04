package flightressources;
public class Airline {

    private String name;
    private String code;
    
    public Airline(String name, String code) {
    	setName(name);
    	setCode(code);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    
    @Override
    public String toString() {
    	return this.getName();
    }

}
