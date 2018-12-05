package cf.poosgroup5_u.bugipedia.api;

import android.util.Pair;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BugCharacteristics {

    @SerializedName("antenna")
    @Expose
    private Boolean antenna;
    @SerializedName("class")
    @Expose
    private String _class;
    @SerializedName("color1")
    @Expose
    private String color1;
    @SerializedName("color2")
    @Expose
    private String color2;
    @SerializedName("family")
    @Expose
    private String family;
    @SerializedName("general_type")
    @Expose
    private String generalType;
    @SerializedName("genus")
    @Expose
    private String genus;
    @SerializedName("hairy_furry")
    @Expose
    private Boolean hairyFurry;
    @SerializedName("hind_legs_jump")
    @Expose
    private Boolean hindLegsJump;
    @SerializedName("mouth_parts")
    @Expose
    private String mouthParts;
    @SerializedName("order")
    @Expose
    private String order;
    @SerializedName("scientific_name")
    @Expose
    private String scientificName;
    @SerializedName("thin_body")
    @Expose
    private Boolean thinBody;

    @SerializedName("wings")
    @Expose
    private Boolean wings;

    public Boolean hasAntenna() {
        return antenna;
    }

    public String get_class() {
        return _class;
    }

    public String getColor1() {
        return color1;
    }

    public String getColor2() {
        return color2;
    }

    public String getFamily() {
        return family;
    }

    public String getGeneralType() {
        return generalType;
    }

    public String getGenus() {
        return genus;
    }

    public Boolean isHairyOrFurry() {
        return hairyFurry;
    }

    public Boolean hasHindLegJumping() {
        return hindLegsJump;
    }

    public String getMouthParts() {
        return mouthParts;
    }

    public String getOrder() {
        return order;
    }

    public String getScientificName() {
        return scientificName;
    }

    public Boolean hasThinBody() { return thinBody;}

    public Boolean hasWings() {
        return wings;
    }

    public List<Pair<String, String>> getAllCharacteristics() {
        ArrayList<Pair<String, String>> returnList = new ArrayList<>();

        //iterate over all characteristics
        for (String characteristic : this.toString().split("\\n")){
            //split the label and the value
            String[] split = characteristic.split(":");
            returnList.add(new Pair<String, String>(split[0] + ':', split[1].trim()));
        }

        return returnList;
    }

    @Override
    public String toString() {
        String returnObject;
        String newline = "\n";
        returnObject =  "Has Antenna(s): " + hasAntenna() + newline +
                "Color 1: " + getColor1() + newline +
                "Color 2: " + getColor2() + newline +
                "Class: " + get_class() + newline +
                "Order: " + getOrder() + newline +
                "Family: " + getFamily() + newline +
                "Genus: " + getGenus() + newline +
                "Scientific name: " + getScientificName() + newline +
                "General Type: " + getGeneralType() + newline +
                "Has hair or fur: " + isHairyOrFurry() + newline +
                "Has hind jump legs: " + hasHindLegJumping() + newline +
                "Mouth parts: " + getMouthParts() + newline +
                "Thin Body: " + hasThinBody() + newline +
                "Has wings: " + hasWings() + newline;

        return returnObject;
    }
}
