package cf.poosgroup5_u.bugipedia.api;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Class which provides the ability to initiate API calls to the server.<br/>
 * Logs from the API requests sent and received can be tracked if the debugging is enabled via {@link APICaller#enableDebugLogging(boolean)}
 * @author Klayton
 * @see APIEndpoints
 */
public class APICaller {

    protected static Retrofit retrofit;
    private static boolean debugLoggingEnabled = false;
    private static APIEndpoints api;
    private static String authToken;

    public static final String API_BASE_URL = "https://poosgroup5-u.cf/api/";

    /**
     * creates
     * @param enableDebugLogging
     */
    private static void setCaller(boolean enableDebugLogging){

        //todo get authToken from sharedPreferences
        // https://stackoverflow.com/questions/40043166/shared-prefrences-to-save-a-authentication-token-in-android
        authToken = "INVALID_AUTH_TOKEN";

        if (enableDebugLogging){
            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            HttpLoggingInterceptor httpBodyInterceptor = new HttpLoggingInterceptor();
            httpBodyInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            builder.networkInterceptors().add(httpBodyInterceptor);
            builder.build();

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new AuthTokenInterceptor(authToken))
                    .addInterceptor(httpBodyInterceptor)
                    .readTimeout(5, TimeUnit.SECONDS)
                    .build();


            retrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)

                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();

        } else {

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new AuthTokenInterceptor(authToken))
                    .build();

            //create a basic Retrofit with the minimal needed functionality
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();

        }

    }


    /**
     * Creates an instance of {@link APIEndpoints} backed by {@link Retrofit} for contacting an API server
     * @return an instance of {@link APIEndpoints} ready for network interaction
     */
    public static APIEndpoints call(){
        if (api != null)
            return api;

        return api = getCaller().create(APIEndpoints.class);
    }

    private static Retrofit getCaller() {
        if (retrofit == null)
            setCaller(debugLoggingEnabled);

        return retrofit;
    }

    /**
     * Method which will recreate the {@link APICaller} with debug-logging functionality enabled/disabled for the {@link Retrofit} and {@link OkHttpClient} abstracted underneath. <br/>
     *
     * @param enable True to enable debug logging in the app for API related activites, False otherwise. <br/> Method will do no action if the APICaller is already in the specified state set by the arugment.
     */
    public static void enableDebugLogging(boolean enable) {
        if (debugLoggingEnabled != enable){
            debugLoggingEnabled = enable;
            setCaller(debugLoggingEnabled);
        }

    }

    public static void updateAuthToken(String newAuthToken){
        authToken = newAuthToken;
    }

    /**
     * Private class meant to add the Auth token to all requests to the API
     */
    private static class AuthTokenInterceptor implements Interceptor{

        String authToken;
        AuthTokenInterceptor(String authToken){
            this.authToken = authToken;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            //todo replace with actual value for interceptor
            Request request = chain.request().newBuilder().addHeader("X-Auth-Token", authToken).build();
            return chain.proceed(request);
        }
    }



}
