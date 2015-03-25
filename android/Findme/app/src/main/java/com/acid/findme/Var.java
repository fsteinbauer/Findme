package com.acid.findme;

public class Var {
	
	// JSON Node names
	protected static final String TAG_SUCCESS = "success";
	protected static final String TAG_NODES = "nodes";
	protected static final String TAG_NODE = "node";
	protected static final String TAG_ID = "id";
	protected static final String TAG_CAT = "cat";
	protected static final String TAG_NAME = "name";
	protected static final String TAG_LAT = "lat";
	protected static final String TAG_LON = "lon";
	protected static final String TAG_DISTANCE = "distance";
	protected static final String TAG_TIMESTAMP ="timestamp";

    public static final String ERROR_ID = "error";
    public static final String CATEGORY_ID = "category";

    // URL to query-Script	
    protected static final String httpHost = "http://findme.acid-design.at/";

    public static final int MIN_TIME_BW_UPDATES = 2;
    public static final int MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;

    public static final float MIN_LAT = 46f;
    public static final float MAX_LAT = 49f;
    public static final float MIN_LNG = 9f;
    public static final float MAX_LNG = 17.6f;
}