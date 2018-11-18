package cf.poosgroup5_u.bugipedia.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResult extends Result{

    @SerializedName("results")
    @Expose
    private List<SearchResultEntry> results = null;

    public List<SearchResultEntry> getSearchResults() {
        return results;
    }
}