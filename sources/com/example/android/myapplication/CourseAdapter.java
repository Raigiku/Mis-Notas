package com.example.android.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class CourseAdapter extends Adapter<CourseViewHolder> {
    private CallbackInterface callback;
    private Context context;
    private ArrayList<Course> courses;
    private Context intentContext;

    public interface CallbackInterface {
        void onHandleRemove();
    }

    public static class CourseViewHolder extends ViewHolder {
        Button courseLookUpButton;
        TextView courseTextView;
        ImageView deleteCourseImageView;
        TextView initialCourseTextView;
        TextView teacherTextView;

        public CourseViewHolder(@NonNull View view) {
            super(view);
            this.courseTextView = (TextView) view.findViewById(C0330R.id.courseTextView);
            this.teacherTextView = (TextView) view.findViewById(C0330R.id.teacherTextView);
            this.courseLookUpButton = (Button) view.findViewById(C0330R.id.gradeLookUpButton);
            this.initialCourseTextView = (TextView) view.findViewById(C0330R.id.initialCourseTextView);
            this.deleteCourseImageView = (ImageView) view.findViewById(C0330R.id.deleteCourseImageView);
        }
    }

    public CourseAdapter(ArrayList<Course> arrayList, Context context) {
        this.courses = arrayList;
        this.context = context;
        try {
            this.callback = (CallbackInterface) context;
        } catch (ArrayList<Course> arrayList2) {
            Log.e("MyAdapter", "Must implement the CallbackInterface in the Activity", arrayList2);
        }
    }

    @NonNull
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        CourseViewHolder courseViewHolder = new CourseViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C0330R.layout.list_layout, null));
        this.intentContext = viewGroup.getContext();
        return courseViewHolder;
    }

    public void onBindViewHolder(@NonNull CourseViewHolder courseViewHolder, final int i) {
        Course course = (Course) this.courses.get(i);
        courseViewHolder.courseTextView.setText(course.getName());
        courseViewHolder.teacherTextView.setText(course.getTeacherName());
        courseViewHolder.initialCourseTextView.setText(String.valueOf(Character.toUpperCase(course.getName().charAt(0))));
        int parseColor = Color.parseColor("#485A6A");
        switch (course.getThemeCode()) {
            case 1:
                parseColor = Color.parseColor("#5eb9ff");
                break;
            case 2:
                parseColor = Color.parseColor("#485a6a");
                break;
            case 3:
                parseColor = Color.parseColor("#eb7979");
                break;
            case 4:
                parseColor = Color.parseColor("#ebae79");
                break;
            case 5:
                parseColor = Color.parseColor("#937afc");
                break;
            case 6:
                parseColor = Color.parseColor("#f8cd76");
                break;
            case 7:
                parseColor = Color.parseColor("#4e90c4");
                break;
            case 8:
                parseColor = Color.parseColor("#5dd8a7");
                break;
            default:
                break;
        }
        courseViewHolder.courseLookUpButton.setTextColor(parseColor);
        if (course.getThemeCode() != 4) {
            courseViewHolder.initialCourseTextView.getBackground().setColorFilter(parseColor, Mode.SRC_OVER);
        } else {
            courseViewHolder.initialCourseTextView.setBackground(this.context.getResources().getDrawable(C0330R.drawable.circle_4));
        }
        courseViewHolder.courseLookUpButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CourseActivity.class);
                intent.putExtra("Course", (Parcelable) CourseAdapter.this.courses.get(i));
                CourseAdapter.this.intentContext.startActivity(intent);
            }
        });
        courseViewHolder.deleteCourseImageView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                CourseAdapter.this.courses.remove(i);
                CourseAdapter.this.notifyDataSetChanged();
                if (CourseAdapter.this.callback != null) {
                    CourseAdapter.this.callback.onHandleRemove();
                }
            }
        });
    }

    public int getItemCount() {
        return this.courses.size();
    }
}
