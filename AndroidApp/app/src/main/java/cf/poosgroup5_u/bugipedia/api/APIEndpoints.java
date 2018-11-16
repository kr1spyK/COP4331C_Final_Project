package cf.poosgroup5_u.bugipedia.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 *  Interface which holds all possible API endpoints that could be made to the server.
 *
 * @author Klayton Killough
 */
public interface APIEndpoints {

    /**
     * API endpoint to register a user on the database of the server
     * @param registerUser  the user details to be sent for registration
     * @return A {@link Result} Object containing whether the registration was successful
     */
    @POST("register")
    Call<Result> register(@Body RegisterUser registerUser);

    @POST("login")
    Call<LoginResult> login(@Body Login login);

    @GET("search")
    Call<SearchFieldResult> getSearchFields();

    @POST("search")
    Call<SearchResult> search(@Body List<SearchField> searchQuery);

    @POST("getSightings")
    Call<SightingResult> getSightings(@Body BugInfo bug);

    @POST("addSighting")
    Call<Result> addSighting(@Body Sighting sighting);




}
