package cf.poosgroup5_u.bugipedia.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BugEntry extends Result {

    @SerializedName("additional_advice")
    @Expose
    private String additionalAdvice;
    @SerializedName("characteristics")
    @Expose
    private BugCharacteristics characteristics;
    @SerializedName("common_name")
    @Expose
    private String commonName;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("pictures")
    @Expose
    private List<BugImage> pictures = null;
    @SerializedName("sightings")
    @Expose
    private List<Sighting> sightings = null;

    public String getAdditionalAdvice() {
        return additionalAdvice;
    }

    public BugCharacteristics getCharacteristics() {
        return characteristics;
    }

    public String getCommonName() {
        return commonName;
    }

    public String getDescription() {
        return description;
    }

    public List<BugImage> getPictures() {
        return pictures;
    }

    public List<Sighting> getSightings() {
        return sightings;
    }

    @Override
    public String toString() {
        String newline = "\n";
        String returnObject =  "Additional Advice: " + getAdditionalAdvice() + newline +
                "Characteristics: " + getCharacteristics()+ newline +
                "Common name: " + getCommonName() + newline +
                "Description: " + getDescription() + newline  +
                "Pictures: ";
        for (BugImage bugImage : getPictures())
            returnObject += "\t" + bugImage + newline;
        returnObject += "Sightings: ";
        for (Sighting sighting : getSightings())
            returnObject += sighting + newline;

        return returnObject;
    }
}
