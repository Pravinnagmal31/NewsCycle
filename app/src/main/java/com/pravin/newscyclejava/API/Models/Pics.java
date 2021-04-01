package com.pravin.newscyclejava.API.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Pics implements Parcelable {

    public int id;
    public String author;

    protected Pics(Parcel in) {
        id = in.readInt();
        author = in.readString();
    }

    public static final Creator<Pics> CREATOR = new Creator<Pics>() {
        @Override
        public Pics createFromParcel(Parcel in) {
            return new Pics(in);
        }

        @Override
        public Pics[] newArray(int size) {
            return new Pics[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(author);
    }
}
