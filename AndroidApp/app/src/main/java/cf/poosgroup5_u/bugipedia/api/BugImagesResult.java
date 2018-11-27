package cf.poosgroup5_u.bugipedia.api;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BugImagesResult extends Result {

    @SerializedName("images")
    @Expose
    public List<BugImage> images = null;

    public List<BugImage> getImages() {
        return images;
    }
}
