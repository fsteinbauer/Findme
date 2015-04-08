package com.acid.findme;

import android.os.Parcel;
import android.os.Parcelable;

public class Category implements Parcelable{
	private int cid;
	private String name;
	
	public Category(int cid, String name ){
		this.cid = cid;
		this.name = name;
	}

	public Category(Parcel in){
		cid = in.readInt();
		name = in.readString();
	}
	
	public Category(){}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(cid);
		dest.writeString(name);
	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { 
		public Category createFromParcel(Parcel in) { 
			return new Category(in); 
		}   
		
		public Category[] newArray(int size) { 
			return new Category[size]; 
		} 
	};
	
	public String getName(){
		return name;
	}

	public int getCid(){
		return cid;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public String toString(){
		return cid+", "+name;
	}
}
