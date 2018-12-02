package cf.poosgroup5_u.bugipedia;

import android.app.Application;
import cf.poosgroup5_u.bugipedia.api.APICaller;
import cf.poosgroup5_u.bugipedia.utils.AppUtils;

public class StartupApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Checking for FirstTime should be done in the main activity.
        // Starting activity intents from here is kinda iffy.
//        if (AppUtils.isFirstTime(this)) {
//            ;
//        }
        if (AppUtils.isLoggedIn(this)) {
            String sessionID = AppUtils.getSessionID(this);
            APICaller.updateAuthToken(sessionID, this);
        }
    }
}
