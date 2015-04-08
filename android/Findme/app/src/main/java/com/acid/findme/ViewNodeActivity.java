package com.acid.findme;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;

import com.acid.findme.exception.GMapDirectionException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


public class ViewNodeActivity extends Activity implements LocationListener, SensorEventListener {

	//the map
	private GoogleMap map;
	
	private GMapDirection md = new GMapDirection();
	
	//location manager
	private LocationManager locationManager;
	private Location currentLocation = null;
	private LatLng currentLatLng = new LatLng(0,0);
	
	// sensors
	private static SensorManager sensorManager;
	private Sensor accelerometer, magnetometer;
	private float[] gravity;
	private float[] geomagnetic;

	//user marker
	private Marker userMarker, nodeMarker;
	private CameraPosition cameraPosition;
	private TextView tvDistance;
	
	private Node node = null;
    private Location location;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_node);
		
		// Get a compass
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	    accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	    magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

	    //get location manager
	  	locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
	  	locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 1, this);

		node = (Node) getIntent().getExtras().getParcelable("node");
		
		// update the TextViews
		TextView tvName = (TextView) findViewById(R.id.name);
		TextView tvCat = (TextView) findViewById(R.id.cat);
		TextView tvDistance = (TextView) findViewById(R.id.distance);
		tvName.setText(node.getName());
		tvCat.setText(node.getCategory().getName());
		tvDistance.setText(Distance.format(node.getDistance()));
		
		

		
		
		//find out if we already have it
		if(map==null){
			map = ((MapFragment)getFragmentManager().findFragmentById(R.id.the_map)).getMap();
			if(map!=null){
				 updatePlaces();
			}
		}
	}
	
	@Override
	protected void onResume(){
		 super.onResume();
		 sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
		 sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
	}
	
	
	protected void onPause() {
	    super.onPause();
	    sensorManager.unregisterListener(this);
	}
	
	
	private void updatePlaces(){


        List<String> providers = locationManager.getProviders(true);
        Location l = null;

        for (int i=providers.size()-1; i>=0; i--) {
            l = locationManager.getLastKnownLocation(providers.get(i));
            if (l != null) break;
        }

        Location currentLocation = l;
        Log.v("findme", "locatin node"+ l);


		currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
		
		CameraPosition cameraPosition = new CameraPosition.Builder()
		    .target(currentLatLng)      // Sets the center of the map to Mountain View
		    .zoom(13)                   // Sets the zoom
		    .bearing(0)                // Sets the orientation of the camera to east
		    .tilt(30)                   // Sets the tilt of the camera to 30 degrees
		    .build();                   // Creates a CameraPosition from the builder
		
		userMarker = map.addMarker(new MarkerOptions()
			.position(currentLatLng)
			.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_arrow))
			.title("You are here")
			.flat(true)
			.anchor(0.5f, 0.5f)
		);

		//remove any existing marker
		if(nodeMarker!=null) nodeMarker.remove();
		nodeMarker = map.addMarker(new MarkerOptions()
			.position(node.getLatlng())
			.title(node.getName())
		);
		//move to location
		map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 3000, null);
		
		GMapDirection md = new GMapDirection();
        Document doc = null;
        try {
            doc = md.getDocument(currentLatLng, node.getLatlng(), GMapDirection.MODE_WALKING);
        } catch (GMapDirectionException e) {
            Util.startErrorActivity(this);
            return;
        }

        ArrayList<LatLng> directionPoint = md.getDirection(doc);
        PolylineOptions rectLine = new PolylineOptions().width(6).color(Color.RED);

        for (int i = 0; i < directionPoint.size(); i++) {
            rectLine.add(directionPoint.get(i));
        }
        Polyline polyline = map.addPolyline(rectLine);
	}
	
	@Override
    public void onLocationChanged(Location location) {
		LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
		userMarker.setPosition(currentLatLng);
		
		//cameraPosition. .target = currentLatLng;
		//map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 200, null);
    }
	
	public void onProviderDisabled(String arg0) {
        Log.e("GPS", "provider disabled " + arg0);
    }
    public void onProviderEnabled(String arg0) {
        Log.e("GPS", "provider enabled " + arg0);
    }
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        Log.e("GPS", "status changed to " + arg0 + " [" + arg1 + "]");
    }

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
		      gravity = event.values;
		if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
		      geomagnetic = event.values;
		
		if(gravity != null && geomagnetic != null){
			float R[] = new float[9];
		    float I[] = new float[9];
		    boolean success = SensorManager.getRotationMatrix(R, I, gravity, geomagnetic);
		    if (success) {
	        float orientation[] = new float[3];
		        SensorManager.getOrientation(R, orientation);
		        float azimut = Math.round(-orientation[0]*360/(2*(float)Math.PI))-90;
		        userMarker.setRotation(azimut);
		    }
		}
	}
}



