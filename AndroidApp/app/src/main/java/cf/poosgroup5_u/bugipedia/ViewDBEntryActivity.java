package cf.poosgroup5_u.bugipedia;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cf.poosgroup5_u.bugipedia.api.APICaller;
import cf.poosgroup5_u.bugipedia.api.BugEntry;
import cf.poosgroup5_u.bugipedia.api.BugIDWrapper;
import cf.poosgroup5_u.bugipedia.api.Sighting;
import cf.poosgroup5_u.bugipedia.gallery.GalleryActivity;
import cf.poosgroup5_u.bugipedia.utils.AppUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewDBEntryActivity extends AppCompatActivity implements OnMapReadyCallback {


    private String TAG;

    private ImageView photoThumbnail, transparentMapOverlay, btnExpandDescription,
            btnExpandCharacteristics, btnExpandAddtlInfo;
    CardView descriptionCard, characteristicCard, additionalInfoCard;
    LinearLayout characteristicItemLayout;
    private TextView commonNameHeader, scientificNameHeader, descriptionText, additionalInfoText;
    private Context context = this;
    private GoogleMap map;
    BugEntry bugEntry;
    BugIDWrapper bugIDWrapper;
    ScrollView scrollView;


    @SuppressLint({"RestrictedApi", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TAG = getLocalClassName();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_dbentry);

        //initalize layout objects
        commonNameHeader = findViewById(R.id.headerCommonName);
        scientificNameHeader = findViewById(R.id.headerSciName);
        descriptionText = findViewById(R.id.textDescription);
        additionalInfoText = findViewById(R.id.textAdditionalInfo);
        photoThumbnail = findViewById(R.id.viewDbPhotoThumbnail);
        additionalInfoCard = findViewById(R.id.additionalInfoBox);
        characteristicItemLayout = findViewById(R.id.characteristicLinearLayout);
        SpeedDialView fab = findViewById(R.id.viewDBEntryFab);
        transparentMapOverlay = findViewById(R.id.transparentMapImageView);
        scrollView = findViewById(R.id.viewDB_ScrollView);
        btnExpandDescription  =findViewById(R.id.button_expandDescription);
        btnExpandCharacteristics =findViewById(R.id.button_expandCharacteristics);
        btnExpandAddtlInfo = findViewById(R.id.button_expandAdditional);


        //get bugID to do everything else

        try {
            bugIDWrapper = new BugIDWrapper((Integer)getIntent().getExtras().get(AppUtils.BUG_INFO_KEY));
        } catch (NullPointerException | ClassCastException ex ){
            Log.e(TAG, "Main Activity didnt Pass a Bug Info ", ex);
            Toast.makeText(this, getString(R.string.bugInfo_obtain_error), Toast.LENGTH_LONG).show();
            finish(); //close the activity as we literally cannot continue without our data.
        }

        obtainBugEntry();


//        //disable the fab if the user isnt logged in
        fab.setVisibility(AppUtils.isLoggedIn(this) ? View.VISIBLE : View.INVISIBLE);
        initFab(fab);

        //setup imageBox to go to gallery
        photoThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GalleryActivity.class);
                intent.putExtra(GalleryActivity.COMMON_NAME_KEY, bugEntry.getCommonName());
                intent.putParcelableArrayListExtra(GalleryActivity.IMAGES_KEY,  new ArrayList<>(bugEntry.getPictures()));
                startActivityForResult(intent, 1);
            }
        });

        //setup map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.viewDb_map);
        mapFragment.getMapAsync(this);

        transparentMapOverlay.setOnTouchListener(mapScrollFixListener);

        //setup information dropdowns
        View descriptionHeaderBar = findViewById(R.id.DescriptionBar);
        ExpandableLayout expandDescription = findViewById(R.id.expandDescription);
        descriptionHeaderBar.setOnClickListener(toggleExpander(expandDescription, btnExpandDescription));

        View additionalInfoHeaderBar= findViewById(R.id.AdditionalInfoBar);
        ExpandableLayout expandAdditional = findViewById(R.id.expandAddtlInfo);
        additionalInfoHeaderBar.setOnClickListener(toggleExpander(expandAdditional, btnExpandAddtlInfo));

        View characteristicHeaderBar = findViewById(R.id.CharactersticsBar);
        ExpandableLayout expandCharacteristics =  findViewById(R.id.expandCharacteristics);
        characteristicHeaderBar.setOnClickListener(toggleExpander(expandCharacteristics, btnExpandCharacteristics));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //The user updated the information of the bug, retrieve again.
        if (resultCode == Activity.RESULT_OK){
            bugEntry = null;
            obtainBugEntry();
        }
    }

    private void initFab(SpeedDialView fab) {
        //id's for the buttons
        final int cameraButtonId = 1;
        final int addSightingId = 2;

        //create the action buttons
        SpeedDialActionItem fabAddImageButton =  new SpeedDialActionItem.Builder(cameraButtonId, android.R.drawable.ic_menu_camera)
                .setLabel(R.string.add_picture)
                .setLabelClickable(true)
                .create();

        SpeedDialActionItem fabAddSightingButton=  new SpeedDialActionItem.Builder(addSightingId, R.drawable.location_marker_24dp)
                .setLabel(R.string.add_sighting)
                .setLabelClickable(true)
                .create();

        fab.addActionItem(fabAddImageButton);
        fab.addActionItem(fabAddSightingButton);

        //add all onclick listeners
        fab.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem actionItem) {

                Class callingClass;

                switch (actionItem.getId()){
                    case cameraButtonId: {
                        //start add picture activity
                        callingClass  = AddPictureActivity.class;
                        break;
                    }

                    case addSightingId: {
                        callingClass =  AddSightingActivity.class;
                        break;
                    }
                    default:{
                        Log.wtf(TAG, "unknown fab button id clicked: " + actionItem.getId());
                        return false;
                    }
                }

                //call the activity
                Intent intent = new Intent(context, callingClass );
                //all of the side activites need the bugID
                intent.putExtra(AppUtils.BUG_INFO_KEY,  bugIDWrapper.getId());
                startActivityForResult(intent, 1);


                //return false to close the popup menu
                return false;
            };
        });
    }

    private void obtainBugEntry() {

        //we've already made the network call for this, dont do it again, just update.
        if (bugEntry != null) {
            updateBugInformation();
            return;
        }

        APICaller.call().getBugEntry(bugIDWrapper).enqueue(new Callback<BugEntry>() {
            @Override
            public void onResponse(Call<BugEntry> call, Response<BugEntry> response) {
                if (response.isSuccessful() && response.body().wasSuccessful()){
                    //fill out all relevant fields
                    bugEntry = response.body();
                    updateBugInformation();
                } else {
                    //error on retrieval from server
                    try {
                        Log.e(TAG, response.errorBody().string());
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                    Toast.makeText(context, getString(R.string.error_getting_bug_info), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<BugEntry> call, Throwable t) {
                //throw the user back to main DB with error
                Log.e(TAG, "Server error occurred while attempting to get DB entry.", t);
                Toast.makeText(context, getString(R.string.error_getting_bug_info), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void updateBugInformation() {
        //handle top header
        commonNameHeader.setText(bugEntry.getCommonName());
        scientificNameHeader.setText(String.format("(%s)", bugEntry.getCharacteristics().getScientificName()));
        String firstImageURL = bugEntry.getPictures().get(0).getUrl().trim();

        AppUtils.loadImageIntoView(firstImageURL, photoThumbnail, context);

        //handle description box
        descriptionText.setText(bugEntry.getDescription());

        //handle characteristics box
        populateCharacteristicsBox();

        //handle additional info box
        //special case, if we dont have any additional info, dont display the box.
        if (!bugEntry.getAdditionalAdvice().isEmpty())
            additionalInfoText.setText(bugEntry.getAdditionalAdvice());
        else {
            additionalInfoCard.setVisibility(View.GONE);
        }

        //add sightings
        if (map != null)
            addMapSightings(bugEntry.getSightings());
    }

    private void populateCharacteristicsBox() {
        for (Pair<String, String> characteristicPair : bugEntry.getCharacteristics().getAllCharacteristics()) {

            //check if the value is NULL or false, if so dont worry about this characteristic
            String value = characteristicPair.second;
            if (value.contains("null"))
                continue;

            //if the value is "true" convert that to yes for plain ol english
            if (value.equalsIgnoreCase("true"))
                value = getString(R.string.yes);
            else if (value.contains("false"))
                value = getString(R.string.no);

            //Setup the layout objects
            TextView nameView = new TextView(this);
            TextView valueView= new TextView(this);
            valueView.setGravity(Gravity.END);

            LinearLayout horizontalLayout = new LinearLayout(this);
            horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
            horizontalLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            horizontalLayout.addView(nameView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            horizontalLayout.addView(valueView,LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            int padding  = (int) getResources().getDimension(R.dimen.viewDb_innerCardViewMargin);
//            horizontalLayout.setPadding(padding, padding, padding, padding );
            horizontalLayout.setBackgroundResource(R.drawable.layout_black_border);


            //populate the text views with the characteristic information
            nameView.setText(characteristicPair.first);
            valueView.setText(value);

            //add the characteristic to the list
            characteristicItemLayout.addView(horizontalLayout);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        map.moveCamera(CameraUpdateFactory.newLatLng(AddSightingActivity.centerFloridaCoords));
        map.moveCamera(CameraUpdateFactory.zoomTo(6));

        map.getUiSettings().setMapToolbarEnabled(false);

    }

    private void addMapSightings(List<Sighting> sightings) {

        double sumLats = 0;
        double sumLongs = 0;

        //add all the markers
        for (Sighting sighting : sightings){
            double lat = sighting.getLatitude();
            double lng = sighting.getLongitude();

            sumLats += lat;
            sumLongs += lng;

            map.addMarker(new MarkerOptions().position(new LatLng(lat, lng)));
        }

        //center the map around all the markers
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(sumLats/sightings.size(), sumLongs/sightings.size())));

    }

    private View.OnClickListener toggleExpander(final ExpandableLayout expandableLayout, final ImageView buttonImage){
        return new View.OnClickListener() {
            boolean toggled = false;
            @Override
            public void onClick(View v) {
                expandableLayout.toggle();
                buttonImage.animate().rotationX(toggled ? 0f : 180f);
                toggled = !toggled;

            }
        };
    }

    /**
     * This on touch listner is needed to fix a
     */
    private View.OnTouchListener mapScrollFixListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    // Disallow ScrollView to intercept touch events.
                    scrollView.requestDisallowInterceptTouchEvent(true);
                    // Disable touch on transparent view
                    return false;

                case MotionEvent.ACTION_UP:
                    // Allow ScrollView to intercept touch events.
                    scrollView.requestDisallowInterceptTouchEvent(false);
                    return true;

                case MotionEvent.ACTION_MOVE:
                    scrollView.requestDisallowInterceptTouchEvent(true);
                    return false;

                default:
                    return true;
            }
        }

    };
}
