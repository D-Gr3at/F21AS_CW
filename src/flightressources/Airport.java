package flightressources;
public class Airport {

    private String name;
    private String code;
    private ControlTower controlTower;
    
    public Airport(String name, String code, ControlTower controlTower) {
    	setName(name);
    	setCode(code);
    	setControlTower(controlTower);
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
