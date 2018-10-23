package cf.poosgroup5_u.bugipedia.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * A POJO representation of a returned JSON Login attempt from the Sever API. <br/>
 * Includes the additional details of a SessionID if the response was successful.
 * @author Klayton Killough
 */

public class LoginResult extends Result {

    @SerializedName("sessionID")
    @Expose
    private String sessionID;

    public String getSessionID() {
        return sessionID;
    }


}