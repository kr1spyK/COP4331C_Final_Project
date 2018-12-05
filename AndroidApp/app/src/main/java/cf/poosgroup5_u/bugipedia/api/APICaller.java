package cf.poosgroup5_u.bugipedia.api;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cf.poosgroup5_u.bugipedia.utils.AppUtils;
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

    public static final String API_BASE_URL = "https://poosgroup5-u.cf/api/";
    protected static Retrofit retrofit;
    private static boolean debugLoggingEnabled = false;
    private static APIEndpoints api;
    private static String authToken = AppUtils.DEFAULT_AUTH_TOKEN;

    /**
     * creates
     * @param enableDebugLogging
     */
    private static void setCaller(boolean enableDebugLogging){
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
                    .addConverterFactory(GsonConverterFactory.create(createGson()))
                    .client(okHttpClient)
                    .build();

        } else {

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new AuthTokenInterceptor(authToken))
                    .connectTimeout(60, TimeUnit.SECONDS) //suport super slow internet connections
                    .build();

            //create a basic Retrofit with the minimal needed functionality
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(createGson()))
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
     * @param enable True to enable debug logging in the app for API related activities, False otherwise. <br/> Method will do no action if the APICaller is already in the specified state set by the argument.
     */
    public static void enableDebugLogging(boolean enable) {
        if (debugLoggingEnabled != enable){
            debugLoggingEnabled = enable;
            setCaller(debugLoggingEnabled);
        }

    }

    //only accessible by test methods and subclasses
    static void updateAuthToken(String newAuthToken){
        authToken = newAuthToken;
        api = null;
        setCaller(debugLoggingEnabled);
    }

    /**
     * Updates the API caller with the SessionID which can be found in the
     * {@link AppUtils#globalAppPref} Global shared Preferences
     *
     * @param context The calling context, If calling from an activity simply put: <code>this</code>
     */
    public static void updateAuthToken(String newAuthToken, Context context){
        //set the value in the global storage
        AppUtils.getGlobalPreferences(context).edit()
                .putString(AppUtils.sessionIDKey, newAuthToken).apply();

        updateAuthToken(newAuthToken);
    }

    /**
     * Handles all custom implementatins for serializing and deserializing JSON objects.
     * Applies all the changes to a gsonObject
     * @return A Gson object ready to interact with the API's JSON objects
     */
    protected static Gson createGson(){

        //create the type of List<SearchField> so we can properly recognize the object
        Type searchQueryType = new TypeToken<List<SearchField>>(){}.getType();

        //create the custom Serializer for SearchFields
        JsonSerializer<List<SearchField>> searchQuerySerializer = new JsonSerializer<List<SearchField>>() {
            @Override
            public JsonElement serialize(List<SearchField> src, Type typeOfSrc, JsonSerializationContext context) {
                JsonObject retElement = new JsonObject();
                for (SearchField searchField : src) {
                    List<String> options = searchField.getOptions();
                    String label = searchField.getLabel();

                    if (options.size() > 1) {
                        JsonArray ja = new JsonArray(options.size());
                        for (String option : options) {
                            ja.add(option);
                        }
                        retElement.add(label, ja);
                    } else if (options.size() == 1) {
                        retElement.addProperty(label, options.get(0));
                    } else { //0 or less than zero
                        retElement.addProperty(label, "");
                    }
                }
                return retElement;
            }
        };

        //GSon for custom serialization and deserialzation for Java objects
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(searchQueryType, searchQuerySerializer)
                .create();

        return gson;
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
            Request request = chain.request().newBuilder().addHeader("X-Auth-Token", authToken).build();
            return chain.proceed(request);
        }
    }
}
