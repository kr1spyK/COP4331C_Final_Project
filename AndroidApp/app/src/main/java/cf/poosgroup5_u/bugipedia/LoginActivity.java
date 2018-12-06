package cf.poosgroup5_u.bugipedia;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import cf.poosgroup5_u.bugipedia.api.APICaller;
import cf.poosgroup5_u.bugipedia.api.Login;
import cf.poosgroup5_u.bugipedia.api.LoginResult;
import cf.poosgroup5_u.bugipedia.utils.AppUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// A login screen that offers login via email/password.
public class LoginActivity extends AppCompatActivity {
    private final Context context = this;
    private static final String TAG = "LoginActivity";
    private static final int REGISTER_REQUEST_CODE = 0;

    // UI references.
    private AutoCompleteTextView mUserView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mUserView = findViewById(R.id.user);

        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mSignInButton = findViewById(R.id.login_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        TextView signUpLink = findViewById(R.id.link_signup);
        signUpLink.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                intent.putExtra(Intent.EXTRA_USER, mUserView.getText().toString());
                startActivityForResult(intent, REGISTER_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle extras = data.getExtras();
        if (requestCode == REGISTER_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String returnName = extras.getString(Intent.EXTRA_USER);
                mUserView.setText(returnName);
            }
        }
    }

    // Checking for empty fields before sending a login request.
    private void attemptLogin() {
        // Reset errors.
        mUserView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String user = mUserView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a password.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a username.
        if (TextUtils.isEmpty(user)) {
            mUserView.setError(getString(R.string.error_field_required));
            focusView = mUserView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            try {
                doLogin(user, password);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d(TAG, "dologin");
        }
    }

    private void doLogin(String user, String password) {
        APICaller.call().login(new Login(user, password)).enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                if (response.isSuccessful() && response.body().wasSuccessful()) {
                    String sessionID = response.body().getSessionID();
                    APICaller.updateAuthToken(sessionID, context);

                    if (BuildConfig.DEBUG) {
                        Toast.makeText(context, sessionID, Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "login server response");
                    }

                    //tell everyone that the user has passed the slideshow
                    AppUtils.setFirstTimeUse(false, context);

                    //take the user to the main activity
                    Intent i = new Intent(context, BuggyMain.class);
                    finish();
                    startActivity(i);

                } else {
                    String error = response.body().getErrorMessage();
                    Log.i(TAG, error);
                    Snackbar.make(findViewById(R.id.email_login_form), getString(R.string.error_login), Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                if (t instanceof IOException) {
                    // logging probably not necessary
                    Snackbar.make(findViewById(R.id.email_login_form), getString(R.string.error_internet), Snackbar.LENGTH_SHORT).show();
                } else {
                    Log.wtf(TAG, t.getMessage(), t);
                    Snackbar.make(findViewById(R.id.email_login_form), getString(R.string.error_conversion), Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }
}
