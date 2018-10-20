package cf.poosgroup5_u.bugipedia.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class Result {
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
