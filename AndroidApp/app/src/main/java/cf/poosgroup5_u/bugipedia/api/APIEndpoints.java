package cf.poosgroup5_u.bugipedia.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIEndpoints {

    @POST("register")
    Call<Result> register(@Body RegisterUser registerUser);

    @POST("login")
    Call<LoginResult> login(@Body Login login);





}
