package com.example.android.myapplication;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.Serializable;

public class Grade implements Parcelable, Serializable {
    public static final Creator<Grade> CREATOR = new C03161();
    private float gradeNumber;
    private String name;
    private int percentage;

    /* renamed from: com.example.android.myapplication.Grade$1 */
    static class C03161 implements Creator<Grade> {
        C03161() {
        }

        public Grade createFromParcel(Parcel parcel) {
            return new Grade(parcel);
        }

        public Grade[] newArray(int i) {
            return new Grade[i];
        }
    }

    public int describeContents() {
        return 0;
    }

    public Grade(String str, int i, float f) {
        this.name = str;
        this.percentage = i;
        this.gradeNumber = f;
    }

    public String getName() {
        return this.name;
    }

    public int getPercentage() {
        return this.percentage;
    }

    public float getGradeNumber() {
        return this.gradeNumber;
    }

    protected Grade(Parcel parcel) {
        this.name = parcel.readString();
        this.percentage = parcel.readInt();
        this.gradeNumber = parcel.readFloat();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.name);
        parcel.writeInt(this.percentage);
        parcel.writeFloat(this.gradeNumber);
    }
}
