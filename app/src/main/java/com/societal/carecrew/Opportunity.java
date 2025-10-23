package com.societal.carecrew;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Opportunity implements Parcelable {
    private String title;
    private String description;
    private String date;
    private String location;
    private String category;
    private double latitude;
    private double longitude;

    public Opportunity(String title, String description, String date, String location) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.location = location;
        this.latitude = 0.0;
        this.longitude = 0.0;
    }
    
    // Constructor with coordinates
    public Opportunity(String title, String description, String date, String location, double latitude, double longitude) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    // No-argument constructor for Firebase
    public Opportunity() {
    }

    protected Opportunity(Parcel in) {
        title = in.readString();
        description = in.readString();
        date = in.readString();
        location = in.readString();
        category = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<Opportunity> CREATOR = new Creator<Opportunity>() {
        @Override
        public Opportunity createFromParcel(Parcel in) {
            return new Opportunity(in);
        }

        @Override
        public Opportunity[] newArray(int size) {
            return new Opportunity[size];
        }
    };

    // Getters for title, description, date, and location
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(date);
        dest.writeString(location);
        dest.writeString(category);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }
    public String getCategory() { // Add this getter method
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public double getLatitude() {
        return latitude;
    }
    
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    
    public double getLongitude() {
        return longitude;
    }
    
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}