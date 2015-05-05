package com.acid.findme;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    User() {;}
    User(Parcel in){;}

    @Override
    public void writeToParcel(Parcel dest, int flags){;}

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }
}