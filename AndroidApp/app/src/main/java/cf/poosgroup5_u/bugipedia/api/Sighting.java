package cf.poosgroup5_u.bugipedia.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sighting {

    private Sighting(){
        //private no-arg for GOSn
    }

    public Sighting(int bugID, double latitude, double longitude){
        this.id = bugID;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @SerializedName("id")
    @Expose(deserialize = false)
    private int id;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("latitude")
    @Expose
    private Double latitude;

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    @Override
    public String toString() {
        return "lat: " + latitude + " Long: " + longitude;
    }
}
