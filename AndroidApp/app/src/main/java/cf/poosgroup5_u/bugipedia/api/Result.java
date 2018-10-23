package cf.poosgroup5_u.bugipedia.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * A POJO representation of a returned JSON result from the Sever API. <br/>
 * The Purpose of the class is to provide more detail than a standard HTTP error code would,
 * as well as provide a baseline for more specific results to base off of.
 * @author Klayton Killough
 */
public class Result {
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("success")
    @Expose
    private Integer success;

    public String getErrorMessage() {
        return error;
    }

    public boolean wasSuccessful() {
        return success == 1;
    }
}
