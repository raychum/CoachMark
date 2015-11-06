package com.theoon.ray.coachmark;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by raychum on 5/11/15.
 */
public class CoachMarkModel implements Parcelable {
    public static final Creator<CoachMarkModel> CREATOR = new Creator<CoachMarkModel>() {
        public CoachMarkModel createFromParcel(Parcel source) {
            return new CoachMarkModel(source);
        }

        public CoachMarkModel[] newArray(int size) {
            return new CoachMarkModel[size];
        }
    };
    public String message;
    public int indicatorHeight = -1;
    public float x;
    public float y;
    public int width;
    public int height;

    public CoachMarkModel(String message, int indicatorHeight, float x, float y, int width, int height) {
        this.message = message;
        this.indicatorHeight = indicatorHeight;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public CoachMarkModel(String message, float x, float y, int width, int height) {
        this.message = message;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public CoachMarkModel() {
    }

    protected CoachMarkModel(Parcel in) {
        this.message = in.readString();
        this.x = in.readFloat();
        this.y = in.readFloat();
        this.width = in.readInt();
        this.height = in.readInt();
        this.indicatorHeight = in.readInt();
    }

    @Override
    public String toString() {
        return "CoachMarkModel{" +
                "message='" + message + '\'' +
                ", indicatorHeight=" + indicatorHeight +
                ", x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.message);
        dest.writeFloat(this.x);
        dest.writeFloat(this.y);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeInt(this.indicatorHeight);
    }
}
