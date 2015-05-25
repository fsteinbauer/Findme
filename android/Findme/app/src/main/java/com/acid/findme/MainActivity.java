package com.acid.findme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.acid.findme.exception.JSONParserException;
import com.acid.findme.exception.LocationException;
// import com.google.android.gms.maps.model.LatLng;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends ActionBarActivity implements
        FilterLatLngDialog.FilterLatLngDialogListener, ConnectionCallbacks, OnConnectionFailedListener{

    // Creating JSON Parser object
    private JSONParser jParser = new JSONParser();

    private Intent intent = null;

    private ArrayList<Category> categories = new ArrayList<Category>();
    private ArrayList<String> categoryNames = new ArrayList<String>();
    private Spinner spinner = null;
    private Button btnGetResult = null;

    private LocationManager locationManager = null;
    private Location location = null;

    private boolean isSpinnerPopulated = false;
    private boolean isLocationFound = false;

    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;

    private void setLocation(Location location_) {
        this.location = location_;
        isLocationFound = true;

        if (isSpinnerPopulated && isLocationFound) {
            btnGetResult.setClickable(true);
            btnGetResult.setEnabled(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Check if we have network access
        if (!isNetworkAvailable()) {
            Util.startErrorActivity(this, R.string.error_noNetwork);
            return;
        }

        buildGoogleApiClient();
        mGoogleApiClient.connect();

        setContentView(R.layout.activity_main);

        btnGetResult = (Button) findViewById(R.id.get_result);
        btnGetResult.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                getResultButtonClick();
            }
        });



        // Populate the spinner first with the default value,
        // then with the DB category values
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                new ArrayList<String>(Arrays.asList(getString(R.string.spinner_loading)))
        ));
        if (isNetworkAvailable()) {
            new LoadSpinner().execute();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        //Check if we have network access
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public void getResultButtonClick() {
        // Get the Category ID
        Category selectedCategory = null;
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getName().equals(spinner.getSelectedItem().toString()))
                selectedCategory = categories.get(i);
        }
        new LoadAllNodesTask(this, location, jParser).execute(selectedCategory);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_filterlatlng:
                new FilterLatLngDialog().show(getFragmentManager(), "Test");
                return true;
	       /* case R.id.action_settings:
	            return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * This function checks if there is a Internet connection available
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onDialogPositiveClick(FilterLatLngDialog dialog) {
        // todo
    }

    @Override
    public void onDialogNegativeClick(FilterLatLngDialog dialog) {
        // todo
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        // Provides a simple way of getting a device's location and is well suited for
        // applications that do not require a fine-grained location and that do not need location
        // updates. Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (lastLocation != null) {
            Log.d("LATITUDE", String.valueOf(lastLocation.getLatitude()));
            Log.d("LONGITUDE", String.valueOf(lastLocation.getLongitude()));
            setLocation(lastLocation);
        } else {
            Util.startErrorActivity(MainActivity.this, R.string.error_noLocation);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Util.startErrorActivity(MainActivity.this, R.string.error_noLocation);
    }


    /**
     * Background Async Task to load up the Spinner
     */
    class LoadSpinner extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("task", "all_categories"));

            JSONObject json = null;
            try {
                json = jParser.makeHttpRequest(Var.httpHost, "GET", params);
            } catch (JSONParserException e) {
                return null;
            }
            Log.d("FINDME Json", json.toString());


            try {
                JSONArray jsonArray = new JSONArray(json.optString("categories"));
                Log.d("FINDME Array", jsonArray.toString());

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jCategory = jsonArray.getJSONObject(i);

                    Category category = new Category(
                            jCategory.optInt("cid"),
                            jCategory.optString("name"));
                    categories.add(category);

                    categoryNames.add(jCategory.optString("name"));
                }

            } catch (Exception e) {
                e.printStackTrace();
                Util.startErrorActivity(MainActivity.this);
                return null;
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         */
        @Override
        protected void onPostExecute(String file_url) {

            runOnUiThread(new Runnable() {
                public void run() {
                    Log.v("findme", categoryNames.toString());
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, categoryNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                    isSpinnerPopulated = true;

                    if (isSpinnerPopulated && isLocationFound) {
                        btnGetResult.setClickable(true);
                        btnGetResult.setEnabled(true);
                    }
                }
            });
        }
    }
}


