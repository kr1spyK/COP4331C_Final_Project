package cf.poosgroup5_u.bugipedia.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Contains global Utilities to be used by multiple classes in the App
 */
public class AppUtils {

    //static global variables
    public static final String DEFAULT_AUTH_TOKEN = "INVALID_AUTH_TOKEN";
    public static final String sessionIDKey = "authToken";
    public static final String firstTimeUseKey = "firstTimeUse";
    public static final String globalAppPref = "BugipediaPreferences";


    /**
     * Sets a global value whether or not the user has already passed the
     * {@link cf.poosgroup5_u.bugipedia.FirstTimeActivity}
     * @param firstTimeUse
     */
    @SuppressLint("ApplySharedPref")
    public static void setFirstTimeUse(boolean firstTimeUse, Context context){
       getGlobalPreferences(context).edit().putBoolean(firstTimeUseKey, firstTimeUse).commit();
    }

    /**
     * Method to check if its the first time the user has used our application. <br/>
     * If the value cannot be determined (not in global storage) assume it is the first time.
     * @param context The calling context, If calling from an activity simply put: <code>this</code>
     * @return true if the user has not used our application before. false otherwise.
     */
    public static boolean isFirstTime(Context context){
        return  getGlobalPreferences(context).getBoolean(firstTimeUseKey, true);
    }

    /////////////////////////NETWORKING UTIL METHODS///////////////////////
    /**
     * Returns whether or not the user is currently logged in.
     * Simply checks the global storage to see if the Auth Token isn't a default value
     * @see AppUtils#getSessionID(Context)
     * @param context The calling context, If calling from an activity simply put: <code>this</code>
     * @return True - There is a user logged in, false otherwise.
     */
    public static boolean isLoggedIn(Context context){
            String sessionID = getSessionID(context);
            return !sessionID.equals(DEFAULT_AUTH_TOKEN);
    }

    /**
     * Returns the value in the global storage for the Session ID value. <br/>
     * Will return {@link #DEFAULT_AUTH_TOKEN} if the value cannot be found or hasnt been chaged.
     * @param context The calling context, If calling from an activity simply put: <code>this</code>
     * @return the Session ID if the user is logged in. or {@link #DEFAULT_AUTH_TOKEN}
     * @see AppUtils#isLoggedIn(Context)
     */
    public static String getSessionID(Context context){
        return  getGlobalPreferences(context).getString(sessionIDKey, DEFAULT_AUTH_TOKEN);
    }

    /**
     * Returns an instance to the global app-wide preferences. <br/>
     * Can be used to store Key-Value pairs that need to be accessed across the app.
     * @param context The calling context, If calling from an activity simply put: <code>this</code>
     * @return
     */
    public static SharedPreferences getGlobalPreferences (Context context){
        return context.getSharedPreferences(globalAppPref, Context.MODE_PRIVATE);
    }

}
