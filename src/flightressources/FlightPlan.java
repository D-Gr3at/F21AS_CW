package flightressources;
import java.util.LinkedList;
import java.util.List;

public class FlightPlan {

    private LinkedList<Airport> airports;

    public FlightPlan(LinkedList<Airport> controlTowers) {
    	setAirports(controlTowers);
    }

    public List<Airport> getAirports() {
        return airports;
    }

    public void setAirports(LinkedList<Airport> airports) {
        this.airports = airports;
    }
}