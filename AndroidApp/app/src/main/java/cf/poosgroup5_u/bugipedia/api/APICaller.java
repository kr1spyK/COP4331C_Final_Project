package cf.poosgroup5_u.bugipedia.api;

import okhttp3.OkHttpClient;
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

    public static final String API_BASE_URL = "https://poosgroup5-u.cf/api/";

    /**
     * creates
     * @param enableDebugLogging
     */
    private static void setCaller(boolean enableDebugLogging){
        if (enableDebugLogging){
            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();

            // Can be Level.BASIC, Level.HEADERS, or Level.BODY
            // See http://square.github.io/okhttp/3.x/logging-interceptor/ to see the options.
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.networkInterceptors().add(httpLoggingInterceptor);
            builder.build();

            OkHttpClient okHttpClient = new OkHttpClient.Builder().
                    addInterceptor(httpLoggingInterceptor)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
//                    .client(okHttpClient)
                    .build();

        } else {
            //create a basic Retrofit with the minimal needed functionality
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
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
}
