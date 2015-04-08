package com.acid.findme;

import java.sql.Timestamp;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class Node implements Parcelable{
	private int nid;
	private String name;
	private LatLng latlng;
	private double distance;
	private Category category;
	private User user;

	public Node(int nid, String name, double distance, LatLng latLng, Category category, User user ){
		this.nid = nid;
		this.name = name;
		this.latlng = latLng;
		this.distance = distance;
		this.category = category;
		this.user = user;
	}

	public Node(Parcel in){
		nid = in.readInt();
		name = in.readString();
		latlng = in.readParcelable(LatLng.class.getClassLoader());
		distance = in.readDouble();
		category = in.readParcelable(Category.class.getClassLoader());
		user = in.readParcelable(User.class.getClassLoader());
	}
	
	@Override 
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(nid);
		dest.writeString(name);
		dest.writeParcelable(latlng, flags);
		dest.writeDouble(distance);		
		dest.writeParcelable(category, flags);
		dest.writeParcelable(user, flags);		
	}
	
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { 
		public Node createFromParcel(Parcel in) { 
			return new Node(in); 
		}   
		
		public Node[] newArray(int size) { 
			return new Node[size]; 
		} 
	};
	
	public double getDistance(){
		return distance;
	}
	
	public void setDistance(double distance){
		this.distance = distance;
	}

	public LatLng getLatlng() {
		return latlng;
	}

	public void setLatlng(LatLng _latlng) {
		latlng = _latlng;
	}

	public String getName() {
		return name;
	}

	public void setName(String _name) {
		name = _name;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category _category) {
		category = _category;
	}

	public int getNid() {
		return nid;
	}

	public void setNid(int _nid) {
		nid = _nid;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User _user) {
		user = _user;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
}
