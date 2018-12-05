package cf.poosgroup5_u.bugipedia.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BugIDWrapper {


    public BugIDWrapper(int bugID){
        this.id = bugID;
    }


    @SerializedName("id")
    @Expose(deserialize = false)
    private int id;


    public int getId() {
        return id;
    }
}
