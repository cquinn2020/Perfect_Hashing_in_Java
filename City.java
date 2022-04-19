import java.util.Objects;
import java.io.Serializable;

public class City implements Serializable {
    // data members
    public String name;
    public float latitude;
    public float longitude;

    public City() {
    }

    public City(String name, float latitude, float longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getLatitude() {
        return this.latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return this.longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public City name(String name) {
        setName(name);
        return this;
    }

    public City latitude(float latitude) {
        setLatitude(latitude);
        return this;
    }

    public City longitude(float longitude) {
        setLongitude(longitude);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof City)) {
            return false;
        }
        City city = (City) o;
        return Objects.equals(name, city.name) && latitude == city.latitude && longitude == city.longitude;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, latitude, longitude);
    }

    @Override
    public String toString() {
        return "Found " +
            getName() + " (" +
            getLatitude() + ", " +
            getLongitude() + " )" + "\n" +
            "http://www.google.com/maps?z=10&q=" +
            getLatitude() + "," + getLongitude();

    }

}
