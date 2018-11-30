package cf.poosgroup5_u.bugipedia.api;

import android.util.Pair;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BugCharacteristics {

    @SerializedName("antenna")
    @Expose
    public Boolean antenna;
    @SerializedName("class")
    @Expose
    public String _class;
    @SerializedName("color1")
    @Expose
    public String color1;
    @SerializedName("color2")
    @Expose
    public String color2;
    @SerializedName("family")
    @Expose
    public String family;
    @SerializedName("general_type")
    @Expose
    public String generalType;
    @SerializedName("genus")
    @Expose
    public String genus;
    @SerializedName("hairy_furry")
    @Expose
    public Boolean hairyFurry;
    @SerializedName("hind_legs_jump")
    @Expose
    public Boolean hindLegsJump;
    @SerializedName("mouth_parts")
    @Expose
    public String mouthParts;
    @SerializedName("order")
    @Expose
    public String order;
    @SerializedName("scientific_name")
    @Expose
    public String scientificName;
    @SerializedName("wings")
    @Expose
    public Boolean wings;

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

    public Boolean hasWings() {
        return wings;
    }

    public List<Pair<String, String>> getAllCharacteristics() {
        ArrayList<Pair<String, String>> returnList = new ArrayList<>();

        //iterate over all characteristics
        for (String characteristic : this.toString().split("\\n")){
            //split the label and the value
            String[] split = characteristic.split(":");
            returnList.add(new Pair<String, String>(split[0] + ':', split[1]));
        }

        return returnList;
    }

    @Override
    public String toString() {
        String returnObject;
        String newline = "\n";
        returnObject =  "has Antenna: " + hasAntenna() + newline +
                "Color 1: " + getColor1() + newline +
                "color 2: " + getColor2() + newline +
                "Class: " + get_class() + newline +
                "Order: " + getOrder() + newline +
                "Family: " + getFamily() + newline +
                "Genus: " + getGenus() + newline +
                "Scientific name: " + getScientificName() + newline +
                "General Type: " + getGeneralType() + newline +
                "Has hair or fur: " + isHairyOrFurry() + newline +
                "Mouth parts: " + getMouthParts() + newline +
                "has wings: " + hasWings() + newline;

        return returnObject;
    }
}
