package cf.poosgroup5_u.bugipedia.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResult extends Result {

    @SerializedName("sessionID")
    @Expose
    private String sessionID;

    public String getSessionID() {
        return sessionID;
    }


}