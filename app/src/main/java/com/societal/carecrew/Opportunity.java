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
    private boolean isUrgent;

    public Opportunity(String title, String description, String date, String location) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.location = location;
        this.isUrgent = false;
    }

    public Opportunity(String title, String description, String date, String location, boolean isUrgent) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.location = location;
        this.isUrgent = isUrgent;
    }

    protected Opportunity(Parcel in) {
        title = in.readString();
        description = in.readString();
        date = in.readString();
        location = in.readString();
        category = in.readString();
        isUrgent = in.readByte() != 0;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isUrgent() {
        return isUrgent;
    }

    public void setUrgent(boolean urgent) {
        isUrgent = urgent;
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
        dest.writeByte((byte) (isUrgent ? 1 : 0));
    }
}
