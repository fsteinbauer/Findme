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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class MainActivity extends FragmentActivity implements FilterLatLngDialog.FilterLatLngDialogListener {
	
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

    private void setLocation(Location location_){
        this.location = location_;
        isLocationFound = true;

        if(isSpinnerPopulated && isLocationFound ){
            btnGetResult.setClickable(true);
            btnGetResult.setEnabled(true);
        }
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        //Check if we have network access
        if(!isNetworkAvailable()){
            Util.startErrorActivity(this, R.string.error_noNetwork);
            return;
        }

        setContentView(R.layout.activity_main);

        btnGetResult = (Button) findViewById(R.id.get_result);
        btnGetResult.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                getResultButtonClick();
            }
        });


        LocationManager locationManager = (LocationManager) getApplicationContext()
                .getSystemService(Context.LOCATION_SERVICE);

        location = locationManager
                .getLastKnownLocation(LocationManager.GPS_PROVIDER);


        // Populate the spinner first with the default value,
		// then with the DB category values
		spinner = (Spinner) findViewById(R.id.spinner); 
		spinner.setAdapter( new ArrayAdapter<String>(
			this, 
			android.R.layout.simple_spinner_item, 
			new ArrayList<String>( Arrays.asList(getString(R.string.spinner_loading)))
		));
        if(isNetworkAvailable()){
            new LoadSpinner().execute();
        }

	}

    public void getResultButtonClick()
    {
        // Get the Category ID
        Category selectedCategory = null;
        for( int i=0; i < categories.size(); i++ ){
            if(categories.get(i).getName().equals(spinner.getSelectedItem().toString()) )
                selectedCategory = categories.get(i);
        }
        intent = new Intent(MainActivity.this, ResultActivity.class);
        intent.putExtra(Var.CATEGORY_ID, selectedCategory);
        new LoadAllNodesTask(this, location, jParser).execute(selectedCategory);
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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


	/**
	 * Background Async Task to load up the Spinner
	 */
	class LoadSpinner extends AsyncTask<String, String, String> {
 
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

            try {
				JSONArray jsonArray = new JSONArray(json.optString("categories"));

				for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jCategory = jsonArray.getJSONObject(i);
                }



			} catch(Exception e){
                e.printStackTrace();
                Util.startErrorActivity(MainActivity.this);
                return null;
			}

			return null;
		}
		
		/**
		 * After completing background task Dismiss the progress dialog
		 * */
		protected void onPostExecute(String file_url) {
			
			runOnUiThread(new Runnable() {
				public void run() {
                    Log.v("findme", categoryNames.toString());
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, categoryNames);
					adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spinner.setAdapter(adapter);
					isSpinnerPopulated = true;

				}
			});	
		}
	}



