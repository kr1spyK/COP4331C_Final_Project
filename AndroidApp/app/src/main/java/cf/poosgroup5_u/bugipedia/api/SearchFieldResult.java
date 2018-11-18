package cf.poosgroup5_u.bugipedia.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchFieldResult extends Result {

    @SerializedName("fields")
    @Expose
    private List<SearchField> fields = null;

    public List<SearchField> getFields() {
        return fields;
    }
}