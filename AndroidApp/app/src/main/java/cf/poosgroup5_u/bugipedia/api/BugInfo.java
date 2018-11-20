package cf.poosgroup5_u.bugipedia.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BugInfo {

    /**
     * Protected constructor meant for test purposes only, the user should never createa  BugInfo
     */
    BugInfo(int bugID){
        this.id = bugID;
    }


    @SerializedName("id")
    @Expose(deserialize = false)
    private int id;


    public int getId() {
        return id;
    }
}
