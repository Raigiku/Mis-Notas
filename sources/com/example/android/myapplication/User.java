package com.example.android.myapplication;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.Serializable;
import java.util.ArrayList;

public class User implements Parcelable, Serializable {
    public static final Creator<User> CREATOR = new C03311();
    public ArrayList<Course> courses;
    public boolean fileExists;
    public String userName;

    /* renamed from: com.example.android.myapplication.User$1 */
    static class C03311 implements Creator<User> {
        C03311() {
        }

        public User createFromParcel(Parcel parcel) {
            return new User(parcel);
        }

        public User[] newArray(int i) {
            return new User[i];
        }
    }

    public int describeContents() {
        return 0;
    }

    public User() {
        this.userName = BuildConfig.FLAVOR;
        this.courses = new ArrayList();
        this.fileExists = false;
    }

    protected User(Parcel parcel) {
        this.userName = parcel.readString();
        boolean z = true;
        if (parcel.readByte() == (byte) 1) {
            this.courses = new ArrayList();
            parcel.readList(this.courses, Course.class.getClassLoader());
        } else {
            this.courses = null;
        }
        if (parcel.readByte() == null) {
            z = false;
        }
        this.fileExists = z;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.userName);
        if (this.courses == 0) {
            parcel.writeByte(0);
        } else {
            parcel.writeByte(1);
            parcel.writeList(this.courses);
        }
        parcel.writeByte((byte) this.fileExists);
    }
}
