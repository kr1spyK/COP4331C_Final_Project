package cf.poosgroup5_u.bugipedia;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonPolygon;

import java.util.ArrayList;
import java.util.List;

import cf.poosgroup5_u.bugipedia.api.Result;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddSighting extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker chosenLocation;
    private List<List<LatLng>> floridaGeoFence;
    private Snackbar outOfBoundsWarning;
    private final LatLng centerFloridaCoords = new LatLng(29.282079,-83.281953);
    private LatLngBounds floridaBounds = new LatLngBounds(new LatLng(24.527164,-79.267069),new LatLng(30.995235,-87.698731));

    ObjectAnimator progressBarAnimation;
    ProgressDialog progressDialog;
    Button addLocationButton;
    private AlertDialog confirmLocationDialog;
    private Callback<Result> addSightingCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sighting);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);
        outOfBoundsWarning = Snackbar.make(mapFragment.getView(), R.string.AddSightingWarning, Snackbar.LENGTH_SHORT);

        addLocationButton = findViewById(R.id.addLocationButton);
        addLocationButton.setOnClickListener(onAddLocationClick);

        createConfirmationDialog();

        progressDialog = createProgressSpinner();
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try {
            GeoJsonLayer gsl = new  GeoJsonLayer(mMap, R.raw.florida_geo_fence, this);
            floridaGeoFence = getGeoFences(gsl);
        } catch (Exception e) {
            Log.wtf(this.getLocalClassName(), e);
        }

        //Add a marker each time the user clicks a spot on the ma
        mMap.setOnMapClickListener(mapClickListener);

        //the markers shouldnt do anything other than display their tooltip when clicked
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                //tell all listeners that this event was consumed.
                return true;
            }
        });

        // move the camera to florida
        mMap.moveCamera(CameraUpdateFactory.newLatLng(centerFloridaCoords));
//        zoom levels: 1 = world 5 = contient 10 = city
        mMap.moveCamera(CameraUpdateFactory.zoomTo(6));
        createAddSightingCallback();

    }

    View.OnClickListener onAddLocationClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (chosenLocation != null){
                confirmLocationDialog.show();

            } else {
                //tell the user to select a location first.
                Snackbar.make(v, R.string.sightingNotChosen, Snackbar.LENGTH_SHORT).show();
            }
        }
    };


    GoogleMap.OnMapClickListener mapClickListener = new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {
            if (insideGeoFence(latLng)) {
                if (chosenLocation != null) {
                    chosenLocation.setPosition(latLng);
                } else {
                    chosenLocation = mMap.addMarker(new MarkerOptions().position(latLng).title(getString(R.string.BugLocation)));
                }
                chosenLocation.showInfoWindow();
            } else {
                //display notification to click within florida
                outOfBoundsWarning.show();
            }
        }
    };

    private void createConfirmationDialog() {
         confirmLocationDialog = new AlertDialog.Builder(this)
                .setMessage(R.string.confirmBugSightingMessage)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //todo send out the add sighting request
//                        progressBarAnimation.start();
                        progressDialog.show();
//                APICaller.call().addSighting().enqueue(addSightingCallback);
                        //todo start a progress bar or spinner


                        //todo DEBUG, REMOVE ME
//                        finish();
                        //todo DEBUG, REMOVE ME

                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                }).create();
    }

    private ProgressDialog createProgressSpinner() {
        //https://stackoverflow.com/questions/18579030/prevent-progressdialog-from-getting-dismissed-by-onclick
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading Sighting");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }



    private List<List<LatLng>> getGeoFences(GeoJsonLayer gsl){
        ArrayList<GeoJsonPolygon> polyList = (ArrayList) gsl.getFeatures().iterator().next().getGeometry().getGeometryObject();
        ArrayList<List<LatLng>> bounds = new ArrayList<>();

        for (GeoJsonPolygon poly : polyList){
            bounds.add(poly.getOuterBoundaryCoordinates());
        }

        return bounds;
    }

    private boolean insideGeoFence(LatLng latLng){

        //very bad workaround, but stops the app from crashing and the user can continue as normal.
        if (floridaGeoFence == null)
                return true;

        for (List<LatLng> bound : floridaGeoFence){
            if (PolyUtil.containsLocation(latLng, bound, true))
                return true;
        }
        return false;
    }

    private void createAddSightingCallback() {
        addSightingCallback = new Callback<Result>() {
            Snackbar errorUploadingSnackBar = Snackbar.make(findViewById(R.id.mapView), getString(R.string.uploadErrorMessage), Snackbar.LENGTH_SHORT);

            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.body().wasSuccessful()) {
                    //close the view and report success
                    Toast.makeText(AddSighting.this, getString(R.string.sightingAdded), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    finish();
                } else {
                    errorUploadingSnackBar.show();
                    Log.e(AddSighting.class.getName(), response.message());

                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Log.e(AddSighting.class.getName(), t.getMessage(), t);
                errorUploadingSnackBar.show();
                progressDialog.dismiss();
            }
        };
    }

}
