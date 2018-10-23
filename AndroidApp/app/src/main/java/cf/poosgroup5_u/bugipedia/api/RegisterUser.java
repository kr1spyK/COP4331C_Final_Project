package cf.poosgroup5_u.bugipedia.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * A POJO (Plain Old Java Object) representing the all fields that be contained within a JSON registration call
 * @author Klayton Killough
 * @see APIEndpoints#register(RegisterUser)
 */
public class RegisterUser {

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("isAdmin")
    @Expose
    private Integer isAdmin;

    public RegisterUser(String username, String password) {
        this.username = username;
        this.password = password;
        isAdmin = 0;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getIsAdmin() {
        return isAdmin == 1;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin ? 1 : 0 ;
    }

}