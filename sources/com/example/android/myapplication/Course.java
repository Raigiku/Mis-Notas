package com.example.android.myapplication;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class Course implements Parcelable, Serializable {
    public static final Creator<Course> CREATOR = new C03061();
    private float finalGrade;
    private int finalPercentage;
    public ArrayList<Grade> grades;
    private String name;
    private String teacherName;
    private int themeCode;

    /* renamed from: com.example.android.myapplication.Course$1 */
    static class C03061 implements Creator<Course> {
        C03061() {
        }

        public Course createFromParcel(Parcel parcel) {
            return new Course(parcel);
        }

        public Course[] newArray(int i) {
            return new Course[i];
        }
    }

    public int describeContents() {
        return 0;
    }

    public Course(String str, String str2, int i) {
        this.name = str;
        this.teacherName = str2;
        this.themeCode = i;
        this.grades = new ArrayList();
        this.finalGrade = null;
        this.finalPercentage = null;
    }

    public int getThemeCode() {
        return this.themeCode;
    }

    public String getName() {
        return this.name;
    }

    public String getTeacherName() {
        return this.teacherName;
    }

    public float getFinalGrade() {
        return this.finalGrade;
    }

    public int getFinalPercentage() {
        return this.finalPercentage;
    }

    public void updateFinalGrade() {
        Iterator it = this.grades.iterator();
        float f = 0.0f;
        int i = 0;
        while (it.hasNext()) {
            Grade grade = (Grade) it.next();
            double d = (double) f;
            double gradeNumber = (double) grade.getGradeNumber();
            double percentage = (double) grade.getPercentage();
            Double.isNaN(percentage);
            percentage /= 100.0d;
            Double.isNaN(gradeNumber);
            gradeNumber *= percentage;
            Double.isNaN(d);
            f = (float) (d + gradeNumber);
            i += grade.getPercentage();
        }
        this.finalGrade = f;
        this.finalPercentage = i;
    }

    protected Course(Parcel parcel) {
        this.name = parcel.readString();
        this.themeCode = parcel.readInt();
        this.teacherName = parcel.readString();
        if (parcel.readByte() == (byte) 1) {
            this.grades = new ArrayList();
            parcel.readList(this.grades, Grade.class.getClassLoader());
        } else {
            this.grades = null;
        }
        this.finalGrade = parcel.readFloat();
        this.finalPercentage = parcel.readInt();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.name);
        parcel.writeInt(this.themeCode);
        parcel.writeString(this.teacherName);
        if (this.grades == 0) {
            parcel.writeByte(0);
        } else {
            parcel.writeByte(1);
            parcel.writeList(this.grades);
        }
        parcel.writeFloat(this.finalGrade);
        parcel.writeInt(this.finalPercentage);
    }
}
