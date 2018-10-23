package cf.poosgroup5_u.bugipedia.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * A POJO (Plain Old Java Object) representing the all fields that be contained within a JSON Login call
 * @author Klayton Killough
 * @see APIEndpoints#login(Login)
 */
public class Login {

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;

    public Login(String username, String password){
        this.username = username;
        this.password = password;
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

}