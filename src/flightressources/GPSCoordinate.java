package flightressources;
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
    private final Pattern DMS_LAT_PATTERN =
            Pattern.compile("(-?)([0-9]{1,3})°([0-5]?[0-9])'([0-5]?[0-9])\\.([0-9]{0,4})\\\"([NS])");
    private final Pattern DMS_LNG_PATTERN =
            Pattern.compile("(-?)([0-9]{1,3})°([0-5]?[0-9])'([0-5]?[0-9])\\.([0-9]{0,4})\\\"([EW])");

    public GPSCoordinate(String longitude, String latitude) throws NumberFormatException{
        try {
	        setLongitude(longitude);
	        setLatitude(latitude);
        } catch (NumberFormatException nfe) {
        	throw nfe;
        }
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

    private Double toDouble(Matcher matcher){
        int sign = "".equals(matcher.group(1)) ? 1 : -1;
        double degrees = Double.parseDouble(matcher.group(2));
        double minutes = Double.parseDouble(matcher.group(3));
        double seconds = Double.parseDouble(matcher.group(4) + '.' + matcher.group(5));
        int direction = "NE".contains(matcher.group(6)) ? 1 : -1;
        return sign * direction * (degrees + minutes / 60 + seconds / 3600 );
    }
}
