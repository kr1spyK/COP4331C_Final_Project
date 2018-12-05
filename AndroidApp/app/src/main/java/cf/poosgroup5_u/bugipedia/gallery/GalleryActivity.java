package cf.poosgroup5_u.bugipedia.gallery;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.rd.PageIndicatorView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cf.poosgroup5_u.bugipedia.R;
import cf.poosgroup5_u.bugipedia.api.APICaller;
import cf.poosgroup5_u.bugipedia.api.BugImage;
import cf.poosgroup5_u.bugipedia.api.Result;
import cf.poosgroup5_u.bugipedia.utils.AppUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class GalleryActivity extends AppCompatActivity {

    /**
     * String used for for the key, when passing the arrayList of strings of links to images of bugs
     */
    public static final String IMAGES_KEY = "bugImages";
    public static final String COMMON_NAME_KEY = "commonName";

    private static final int UI_ANIMATION_DELAY = 0;

    private final Handler mHideHandler = new Handler();
    private Toolbar toolbar;
    private ViewPager photoPager;
    private PageIndicatorView pageIndicatorView;
    private static String TAG;

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            photoPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = getLocalClassName();

        setContentView(R.layout.activity_gallery);
        mVisible = true;


        //get the images from the bundle
        List<BugImage> images;
        try {
            images = getIntent().getExtras().getParcelableArrayList(IMAGES_KEY);
        } catch (NullPointerException | ClassCastException npe ){

            Log.e(TAG, "Didn't get any images from previous activity", npe);
            //log and show an error image
            images = new ArrayList<>();
            images.add(new BugImage(-1, null));//picasso will generate a basic holder but not send the request when given null
        }


        photoPager = findViewById(R.id.galleryViewPager);
        pageIndicatorView = findViewById(R.id.galleryPageIndicator);
        pageIndicatorView.setIdleDuration(2000);

        photoPager.setPageMargin(60);

        // Set up the user interaction to manually show or hide the system UI.
        View.OnClickListener photoViewOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        };


        //setup the display
        photoPager.setAdapter(new galleryPageAdapter(this, images, photoViewOnClickListener));

        //preload all the images that are in the entire view
        for (BugImage image : images){
            if (image != null)
                Picasso.get().load(image.getUrl()).fetch();
        }

        //setup the toolbar
        toolbar = findViewById(R.id.galleryToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try{ //set the toolbar title to the bug name.
            String nameOfBug = getIntent().getExtras().getString(COMMON_NAME_KEY);
            getSupportActionBar().setTitle(nameOfBug);
        }catch (NullPointerException npe ){
            Log.e(TAG, "GalleryActivity was not passed the bug name ", npe);
        };

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (AppUtils.isLoggedIn(this))
        getMenuInflater().inflate(R.menu.menu_gallery, menu);

        //only display report button if we are logged in
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final Toast errorToast = Toast.makeText(GalleryActivity.this, R.string.uploadErrorMessage, Toast.LENGTH_SHORT);
        final Toast successToast = Toast.makeText(GalleryActivity.this, R.string.photoReportSuccess, Toast.LENGTH_SHORT);

        final int id = item.getItemId();
        if (id == R.id.report_photo){
            final BugImage currentImage;
            try {
                currentImage = ((List<BugImage>) getIntent().getExtras().get(IMAGES_KEY)).get(photoPager.getCurrentItem());
            } catch (NullPointerException | ClassCastException ex){
                Log.e(TAG, "Error occurred getting image to report from original Intent.", ex);
                errorToast.show();
                return true;
            }


            new AlertDialog.Builder(this)
                    .setTitle(R.string.report_photo)
                    .setMessage(getString(R.string.confirmReportPhoto))
                    .setPositiveButton(getString(R.string.report), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            APICaller.call().flagImage(currentImage).enqueue(new Callback<Result>() {



                                @Override
                                public void onResponse(Call<Result> call, Response<Result> response) {
                                    if (response.isSuccessful()){
                                        if (response.body().wasSuccessful())
                                            successToast.show();
                                        else {//error message
                                            Log.e(TAG, response.body().getErrorMessage());
                                            errorToast.show();
                                        }
                                    } else {
                                        try {
                                            Log.e(TAG, response.errorBody().string());
                                        } catch (IOException e) {
                                            Log.e(TAG, e.getMessage(), e);
                                        }
                                        errorToast.show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Result> call, Throwable t) {
                                    Log.e(TAG, "Error occurred during photo report" , t);
                                    errorToast.show();
                                }
                            });
                        }
                    })
                    .setNegativeButton(getString(R.string.cancel), null)
                    .show();
        } else if (id == android.R.id.home){
            finish();
        } else {
            //this...should be impossible
            Log.wtf(TAG, "GalleryActivity option menu clicked on something other than the 1 report option.");
        }
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide();
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
//        mControlsView.setVisibility(View.GONE);
        mVisible = false;
        pageIndicatorView.setFadeOnIdle(true);

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        photoPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        //show the page indicator
        pageIndicatorView.setFadeOnIdle(false);

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide() {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, 1000);
    }
}
