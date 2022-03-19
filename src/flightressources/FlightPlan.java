package flightressources;
import java.util.LinkedList;
import exception.InvalidFlightPlanException;

public class FlightPlan {

    private LinkedList<ControlTower> controlTowers;
    
    public FlightPlan(LinkedList<ControlTower> controlTowers) throws InvalidFlightPlanException {
    	try {
    		setControlTowers(controlTowers);
    	} catch (InvalidFlightPlanException ifpe) {
    		throw ifpe;
    	}
    }

    public LinkedList<ControlTower> getControlTowers() {
        return controlTowers;
    }

    public void setControlTowers(LinkedList<ControlTower> controlTowers) throws InvalidFlightPlanException {
    	if(controlTowers.size() > 20 || controlTowers.size() < 2) throw new InvalidFlightPlanException("Invalid flight plan");
        this.controlTowers = controlTowers;
    }

}