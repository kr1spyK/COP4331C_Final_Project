package cf.poosgroup5_u.bugipedia;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import cf.poosgroup5_u.bugipedia.api.APICaller;
import cf.poosgroup5_u.bugipedia.api.RegisterUser;
import cf.poosgroup5_u.bugipedia.api.Result;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *  Register screen accessed through link on login screen
 */
public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";

    // UI references.
    private Switch adminSwitch;
    private AutoCompleteTextView mUserView;
    private EditText mPasswordView;
//    private EditText mPasswordCheck;
    private Button mUserSignInButton;
    private TextView mLoginLink;

    private boolean setAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Intent intent = getIntent();
        String loggieName = intent.getStringExtra(Intent.EXTRA_USER);

        // Set up the login form.
        adminSwitch = findViewById(R.id.adminSwitch);
        mUserView = findViewById(R.id.user);
        mPasswordView = findViewById(R.id.password);
        mUserSignInButton = findViewById(R.id.signup_button);
        mLoginLink = findViewById(R.id.link_login);

        if (BuildConfig.DEBUG) {
            adminSwitch.setVisibility(View.VISIBLE);
        }

        if (loggieName != null) mUserView.setText(loggieName);

        mUserSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

        // Going back to login page from link, carry over email
        mLoginLink.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = mUserView.getText().toString();
                Intent loginIntent = new Intent();
                loginIntent.putExtra("REGGIE_USER", user);

                setResult(RESULT_CANCELED, loginIntent);
                finish();
            }
        });
    }

    /**
     * Attempts to register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no attempt is made.
     */
    private void attemptRegister() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        if (adminSwitch.isChecked()) setAdmin = true;
        else setAdmin = false;

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            // setError in isPasswordValid()
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            try {
                doRegister(email, password);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d(TAG, "register");
        }
    }

   private void doRegister(String name, String password, final APICallback cb) {
        RegisterUser user = new RegisterUser(name, password);
        user.setIsAdmin(setAdmin);

        APICaller.call().register(user).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.isSuccessful() && response.body().wasSuccessful()) {
                    Snackbar.make(findViewById(R.id.register_form), getString(R.string.action_success), Snackbar.LENGTH_SHORT).show();
                } else {
                    String error = response.body().getErrorMessage();
                    Log.i(TAG, error);
                    Snackbar.make(findViewById(R.id.register_form), error, Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                if (t instanceof IOException) {
                    Snackbar.make(findViewById(R.id.register_form), getString(R.string.error_internet), Snackbar.LENGTH_SHORT).show();
                } else {
                    // logging probably necessary
                    Log.wtf(TAG, t.getMessage(), t);
                    String error = "Conversion error";
                    Snackbar.make(findViewById(R.id.register_form), error, Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isPasswordValid(String password) {
        boolean yeet = true;
        Pattern pattern;
        Matcher matcher;
        String error = getString(R.string.error_invalid_password);
        final String CASE_PATTERN = "((?=.*[A-Z])(?=.*[a-z]).{1,512})";
        final String SYMBOL_PATTERN = "((?=.*\\d)(?=.*[`~!@#$%^&*()_\\-=+\\]\\[{}|;:\"'<,>.?/]).{1,512})";

        if (password.length() < 8) {
            yeet = false;
            error = error + getString(R.string.error_short_password);
        }

        pattern = Pattern.compile(CASE_PATTERN);
        matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            yeet = false;
            error = error + getString(R.string.error_case_password);
        }

        pattern = Pattern.compile(SYMBOL_PATTERN);
        matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            yeet = false;
            error = error + getString(R.string.error_symbol_password);
        }
        if (!yeet) mPasswordView.setError(error);

        return yeet;
    }
}
