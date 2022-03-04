package flightressources;
import java.util.LinkedList;

public class FlightPlan {

    private LinkedList<ControlTower> controlTowers;
    
    public FlightPlan(LinkedList<ControlTower> controlTowers) {
    	setControlTowers(controlTowers);
    }

    public LinkedList<ControlTower> getControlTowers() {
        return controlTowers;
    }

    public void setControlTowers(LinkedList<ControlTower> controlTowers) {
        this.controlTowers = controlTowers;
    }

}