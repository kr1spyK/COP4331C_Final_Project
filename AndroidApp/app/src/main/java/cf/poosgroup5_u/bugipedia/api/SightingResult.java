package cf.poosgroup5_u.bugipedia.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SightingResult extends Result {

    @SerializedName("sightings")
    @Expose
    public List<Sighting> sightings = null;

    public List<Sighting> getSightings() {
        return sightings;
    }
}
