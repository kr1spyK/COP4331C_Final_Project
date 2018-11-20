package cf.poosgroup5_u.bugipedia.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchResultEntry {

    @SerializedName("common_name")
    @Expose
    private String commonName;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("scientific_name")
    @Expose
    private String scientificName;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;

    public String getCommonName() {
        return commonName;
    }

    public Integer getId() {
        return id;
    }

    public String getScientificName() {
        return scientificName;
    }

    public String getThumbnailURL() {
        return thumbnail;
    }
}