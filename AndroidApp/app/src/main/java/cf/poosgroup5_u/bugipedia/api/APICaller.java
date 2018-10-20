package cf.poosgroup5_u.bugipedia.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APICaller {

    protected static Retrofit retrofit;
    public static final String BASE_URL = "https://poosgroup5-u.cf/api/";

    public static Retrofit getCaller() {
        if (retrofit != null)
            return retrofit;

        return retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
