package com.acid.findme;

/**
 * Created by florian on 22.04.2015.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.util.Log;

import com.acid.findme.Category;
import com.acid.findme.MainActivity;
import com.acid.findme.Node;
import com.acid.findme.R;
import com.acid.findme.User;
import com.acid.findme.Util;
import com.acid.findme.Var;
import com.acid.findme.exception.JSONParserException;
import com.google.android.gms.maps.model.LatLng;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Background Async Task to Load all product by making HTTP Request
 * */
public class LoadAllNodesTask extends AsyncTask<Category, String, ArrayList<Node>> {
    // Progress Dialog
    private ProgressDialog pDialog;

    //location manager
    private LocationManager locMan;
    private MainActivity activity;
    private final Location location;
    private JSONParser jParser;
    private Intent intent;

    public LoadAllNodesTask(MainActivity activity, Location location, JSONParser jParser)
    {
        this.activity = activity;
        this.location = location;
        this.jParser = jParser;

        intent = activity.getIntent();
    }

    /**
     * Before starting background thread Show Progress Dialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(activity);
        pDialog.setMessage( activity.getString(R.string.nodes_loading) );
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    /**
     * getting All products from url
     * */
    protected ArrayList<Node> doInBackground(Category... categories) {

        // Get the last known location
        locMan = (LocationManager)activity.getSystemService(Context.LOCATION_SERVICE);
        Location lastLoc = location;

        ArrayList<Node> nodes = new ArrayList<Node>();

        try {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("task", "all_nodes"));
            params.add(new BasicNameValuePair("lat", String.valueOf(lastLoc.getLatitude())) );
            params.add(new BasicNameValuePair("lon", String.valueOf(lastLoc.getLongitude())) );
            params.add(new BasicNameValuePair("cid", String.valueOf(categories[0].getCid())));
            params.add(new BasicNameValuePair("minLat", String.valueOf(Var.MIN_LAT)));
            params.add(new BasicNameValuePair("maxLat", String.valueOf(Var.MAX_LAT)));
            params.add(new BasicNameValuePair("minLng", String.valueOf(Var.MIN_LNG)));
            params.add(new BasicNameValuePair("maxLng", String.valueOf(Var.MAX_LNG)));


            JSONObject json = null;
            try {
                json = jParser.makeHttpRequest(Var.httpHost, "GET", params);
            } catch (JSONParserException e) {
                if (e.hasReason())
                    Util.startErrorActivity(activity, e.getReason());
                else
                    Util.startErrorActivity(activity);
                return null;
            }
            JSONArray jsonArray = new JSONArray(json.optString(Var.TAG_NODES));


            // looping through All Products
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jNode = jsonArray.getJSONObject(i);

                Category category = new Category();
                for(int j = 0; j < categories.length; j++ ){
                    if(categories[j].getCid() == jNode.optInt("cid")){
                        category = categories[j];
                        break;
                    }
                }


                Node node = new Node(
                        jNode.optInt("nid"),
                        jNode.optString("name"),
                        jNode.getDouble("distance"),
                        new LatLng(
                                jNode.getDouble("lat"),
                                jNode.getDouble("lon")),
                        category,//categories[0],
                        new User()
                );

                nodes.add(node);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Util.startErrorActivity(activity);
            return null;
        }
        for(int i = 0; i<nodes.size(); i++){
            Log.d("node", nodes.get(i).getName());
        }
        return nodes;
    }

    /**
     * After completing background task Dismiss the progress dialog
     * **/
    protected void onPostExecute(final ArrayList<Node> nodes) {
        // dismiss the dialog after getting all products
        pDialog.dismiss();

        activity.runOnUiThread(new Runnable() {
            public void run() {
                intent.putParcelableArrayListExtra("nodes", nodes);
                activity.startActivity(intent);
            }
        });
    }
}


