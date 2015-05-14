package com.acid.findme;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

/**
 * This class is responsible for all tasks afflicted
 * with calculating the Distance between to points
 */
public class Distance {
    
	/**
     *
     */
	protected static double calculate( LatLng point1, LatLng point2, char unit) {
		double theta = point1.longitude - point2.longitude;
		double dist = Math.sin(deg2rad(point1.latitude)) * Math.sin(deg2rad(point2.latitude))
            + Math.cos(deg2rad(point1.latitude)) * Math.cos(deg2rad(point2.latitude)) * Math.cos(deg2rad(theta));
		
        dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		if (unit == 'K') 
			dist = dist * 1.609344;
		return dist;
	}
	
    private static double deg2rad(double deg) {
      return (deg * Math.PI / 180.0);
    }


    private static double rad2deg(double rad) {
      return (rad * 180.0 / Math.PI);
    }
	
	
	/**
     * This Function formats the Distance to a readable String
     * 
     * @param d
     * @return String
     */
    public static String format(double d) {
    	Double distance = new Double(d);
    	if(d < 1) {
    		//Log.d("Distance", distance.toString());
    		return String.format("%dm", (int)(distance*1000));
    	} else if(d >= 10){
    		
    		return String.format("%dkm", distance.intValue());
    	}
    	return String.format("%.2fkm", d);
    }
}
