public class ControlTowerTest {

    public Flight getFlight(){
        coordinates = new GPSCoordinate("164°57'12\"E", "77°30'36\"S");
        coordinates1 = new GPSCoordinate("37'19.85\"E", "41°37'0.26\"N");
        List<ControlTower> controlTowers = new ArrayList<>();
        ControlTower cont = new ControlTower(coordinates);
        Airport airport = new Airport("Tio", "TSE", cont);
        Airport dest = new Airport("Geo", "GEW", new ControlTower(coordinates1));

        coordinates2 = new GPSCoordinate("120°57'12\"E", "44°30'36\"S");
        coordinates3 = new GPSCoordinate("118°57'12\"E", "35°30'36\"S");

        ControlTower cont1 = new ControlTower(coordinate2);
        controlTowers.add(cont1);
        ControlTower cont2 = new ControlTower(coordinate3);
        controlTowers.add(cont2);
        FlightPlan plan = new FlightPlan(controlTowers);
        Flight flight = new Flight();
        flight.setDestinationAirport(dest);
        flight.setDepartureAirport(airport);
        flight.setFlightPlan(plan);
        flight.setPlane(new Aeroplane("", 0.0, "", 0.0));
        return flight;
    }

    @Test
    public void testDistanceBetween(){
        GPSCoordinate coordinates2 = new GPSCoordinate("120°57'12\"E", "44°30'36\"S");
        GPSCoordinate coordinates3 = new GPSCoordinate("118°57'12\"E", "35°30'36\"S");
        ControlTower cont1 = new ControlTower(coordinate2);
        ControlTower cont2 = new ControlTower(coordinate3);
        assertEquals(cont1.distanceBetweenControlTower(cont2), 308.45, "Wrong result.");
    }
}