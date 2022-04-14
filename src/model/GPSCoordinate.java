package model;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Describe the GPS Coordinate class using both latitude and longitude axis
 * @author  Carl-Olivier N'Diaye (cn2013@hw.ac.uk)
 * @version v1.0
 * @since   07 Feb 2022
 */

public class GPSCoordinate {
    private String latitude;
    private String longitude;
	/*
	* Regular Expression (Regex) patterns for DMS validation
	* */
    private final Pattern DMS_LAT_PATTERN =
            Pattern.compile("(-?)([0-9]{1,3})°([0-5]?[0-9])'([0-5]?[0-9])\\.([0-9]{0,4})\\\"([NS])");
    private final Pattern DMS_LNG_PATTERN =
            Pattern.compile("(-?)([0-9]{1,3})°([0-5]?[0-9])'([0-5]?[0-9])\\.([0-9]{0,4})\\\"([EW])");

    public GPSCoordinate(String longitude, String latitude) throws NumberFormatException{
        setLongitude(longitude);
        setLatitude(latitude);
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) throws NumberFormatException {
        Matcher matcherLongitude = DMS_LNG_PATTERN.matcher(longitude.trim());
        if(matcherLongitude.matches()) {
        	this.longitude = longitude;
        } else {
        	throw new NumberFormatException("Malformed DMS longitude");
        }
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) throws NumberFormatException{
        Matcher matcherLatitude = DMS_LAT_PATTERN.matcher(latitude.trim());
        if(matcherLatitude.matches()) {
        	this.latitude = latitude;
        } else {
        	throw new NumberFormatException("Malformed DMS latitude");
        }
    }

	/*Calculates longitude in degrees*/
    public Double getLongitudeInDegree() throws NumberFormatException{
        Matcher matcher = DMS_LNG_PATTERN.matcher(this.longitude.trim());
        if (matcher.matches()){
            double longitude = toDouble(matcher);
            if ((Math.abs(longitude) > 180)) {
                throw new NumberFormatException("Invalid longitude");
            }
            return longitude;
        }else {
            throw new NumberFormatException("Malformed DMS coordiniates");
        }
    }

	/*Calculates latitude in degrees*/
    public Double getLatitudeInDegree() throws NumberFormatException{
        Matcher matcher = DMS_LAT_PATTERN.matcher(this.latitude.trim());
        if (matcher.matches()){
            double latitude = toDouble(matcher);
            if ((Math.abs(latitude) > 180)) {
                throw new NumberFormatException("Invalid latitude");
            }
            return latitude;
        }else {
        	//Maybe show these malformed coordinates?
            throw new NumberFormatException("Malformed DMS coordiniates");
        }
    }

    public Double getLatitudeInRadian() throws NumberFormatException{
    	try {
    		return this.getLatitudeInDegree() * (Math.PI / 180);
    	} catch (NumberFormatException nfe) {
    		throw nfe;
    	}
    }

    public Double getLongitudeInRadian() throws NumberFormatException{
    	try {
            return this.getLongitudeInDegree() * (Math.PI / 180);
    	} catch (NumberFormatException nfe) {
    		throw nfe;
    	}
    }

	/*converts DMS to double*/
    private Double toDouble(Matcher matcher){
        int sign = "".equals(matcher.group(1)) ? 1 : -1;
        double degrees = Double.parseDouble(matcher.group(2));
        double minutes = Double.parseDouble(matcher.group(3));
        double seconds = Double.parseDouble(matcher.group(4) + '.' + matcher.group(5));
        int direction = "NE".contains(matcher.group(6)) ? 1 : -1;
        return sign * direction * (degrees + minutes / 60 + seconds / 3600 );
    }

	/*converts double to DMS*/
    private String doubleToDMSCoordinate(double latlonInRadian) {
    	String latlon = "";
    	
    	latlonInRadian = Math.abs(latlonInRadian);
    	
    	int deg = (int) latlonInRadian;
    	double latRemain = latlonInRadian - deg;
    	int min = (int) (latRemain*60);
    	double sec = (latRemain*60 - min)*60;
    	
    	latlon += deg + "°" + min + "'" + String.format("%.4f", sec).replace(",", ".") + "\"";
    	    	
    	return latlon;
    }
    
    private String doubleToLatitude(double latitudeInRadian) {
    	String lat = doubleToDMSCoordinate(latitudeInRadian);
    	
    	lat += latitudeInRadian > 0 ? "N" : "S";
    	
    	return lat;
    }
    
    private String doubleToLongitude(double longitudeInRadian) {
    	String lon = doubleToDMSCoordinate(longitudeInRadian);
    	
    	lon += longitudeInRadian > 0 ? "E" : "W";
    	
    	return lon;
    }
    
    //Method from the support program given by the teacher available here:
    //https://gitlab-student.macs.hw.ac.uk/f21as_2022/cw2_support/-/blob/master/src/p4_gps/GPS.java
    /**
	 * Add a distance d to the current GPS in the direction of the second GPS (g).
	 * Calculate the corresponding intermediate GPS position.
	 * The method is more precise as it takes into consideration the curvature of the line between this and g.
	 * @param g the direction towards which we add d
	 * @param d the added distance in km
	 * @return
	 */
    public GPSCoordinate addCircleDistance(GPSCoordinate directionCoordinate, double distance) {
    	
		double r_lat1 = this.getLatitudeInRadian();
		double r_lat2 = directionCoordinate.getLatitudeInRadian();
		double r_lon1= this.getLongitudeInRadian();
		double r_lon2 = directionCoordinate.getLongitudeInRadian();
		
		double angular = distance/6371.0;
		double f = distance/circleDistance(directionCoordinate);
		double a = Math.sin(angular*(1-f))/Math.sin(angular);
		double b = Math.sin(angular * f) / Math.sin(angular);
		
		//calculate Cartesian coordinates
		double x = a * Math.cos(r_lat1)* Math.cos(r_lon1) + b * Math.cos(r_lat2) * Math.cos(r_lon2);
		double y = a * Math.cos(r_lat1)* Math.sin(r_lon1) + b * Math.cos(r_lat2) * Math.sin(r_lon2);
		double z = a *  Math.sin(r_lat1) + b * Math.sin(r_lat2);
		
		//calculate lat and lon for intermediate point
		double r_latx = Math.atan2(z,  Math.sqrt(x*x + y*y));
		double r_lonx = Math.atan2(y, x);
		
		//System.out.println(r_lat1);
		//System.out.println(r_latx);
		
		return new GPSCoordinate(doubleToLongitude(r_lonx*180/Math.PI), doubleToLatitude(r_latx*180/Math.PI));

    }

    //Method from the support program given by the teacher available here:
    //https://gitlab-student.macs.hw.ac.uk/f21as_2022/cw2_support/-/blob/master/src/p4_gps/GPS.java
	/**
	 * Calculate the great circle distance between this GPS and the given GPS position.
	 * See illustration: <img src="./doc-files/great_circle.png" />
	 * @param g the second GPS position
	 * @return
	 */
	public double circleDistance(GPSCoordinate directionCoordinate) {
		double phi1 = this.getLatitudeInRadian(); //radian
		double theta1 = this.getLongitudeInRadian(); //radian
		double phi2 = directionCoordinate.getLatitudeInRadian(); //radian
		double theta2 = directionCoordinate.getLongitudeInRadian(); //radian
		//calculate differences
		double dphi = phi2 - phi1;
		double dtheta = theta2 - theta1;
		//calculate the great circle arc distance
		double d1 = Math.pow(Math.sin(dphi/2), 2) + Math.cos(phi1) * Math.cos(phi2) * Math.pow(Math.sin(dtheta/2),2);
		double d2 = 2 * Math.asin(Math.sqrt(d1));
		return 6371.0 * d2;
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof GPSCoordinate)) return false;
		
		if(((GPSCoordinate) o).getLatitude().equals(this.latitude)
				&&
				((GPSCoordinate) o).getLongitude().equals(this.longitude)) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.latitude, this.longitude);
	}
}
