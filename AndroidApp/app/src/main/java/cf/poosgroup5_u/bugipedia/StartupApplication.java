package cf.poosgroup5_u.bugipedia;

import android.app.Application;
import cf.poosgroup5_u.bugipedia.api.APICaller;
import cf.poosgroup5_u.bugipedia.utils.AppUtils;

public class StartupApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

//        if (AppUtils.isFirstTime(this)) {
//            ;
//        }
        if (AppUtils.isLoggedIn(this)) {
            String sessionID = AppUtils.getSessionID(this);
            APICaller.updateAuthToken(sessionID, this);
        }
    }
}
