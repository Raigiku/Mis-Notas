package com.example.android.myapplication;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class GradeAdapter extends Adapter<GradeViewHolder> {
    private CallbackDeleteGrade callback;
    private Course course;
    private TextView courseFinalGrade;

    public interface CallbackDeleteGrade {
        void onHandleDeleteGrade(Course course);
    }

    public static class GradeViewHolder extends ViewHolder {
        TextView courseNameTextView;
        ImageView deleteGradeImageView;
        TextView gradeNameTextView;
        TextView gradeNumberTextView;
        TextView gradePercentageTextView;

        public GradeViewHolder(@NonNull View view) {
            super(view);
            this.courseNameTextView = (TextView) view.findViewById(C0330R.id.lastCourseNameTextView);
            this.gradeNameTextView = (TextView) view.findViewById(C0330R.id.gradeNameTextView);
            this.gradePercentageTextView = (TextView) view.findViewById(C0330R.id.gradePercentageTextView);
            this.gradeNumberTextView = (TextView) view.findViewById(C0330R.id.gradeNumberTextView);
            this.deleteGradeImageView = (ImageView) view.findViewById(C0330R.id.deleteGradeImageView);
        }
    }

    public GradeAdapter(Course course, TextView textView) {
        this.course = course;
        this.courseFinalGrade = textView;
        try {
            this.callback = (CallbackDeleteGrade) MyCoursesActivity.helloContext;
        } catch (Course course2) {
            Log.e("MyAdapter", "Must implement the CallbackInterface in the Activity", course2);
        }
    }

    @NonNull
    public GradeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new GradeViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C0330R.layout.grades_layout, null));
    }

    public void onBindViewHolder(@NonNull GradeViewHolder gradeViewHolder, final int i) {
        Grade grade = (Grade) this.course.grades.get(i);
        gradeViewHolder.gradeNameTextView.setText(grade.getName());
        TextView textView = gradeViewHolder.gradePercentageTextView;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.valueOf(grade.getPercentage()));
        stringBuilder.append("%");
        textView.setText(stringBuilder.toString());
        gradeViewHolder.gradeNumberTextView.setText(String.valueOf(grade.getGradeNumber()));
        if (grade.getGradeNumber() < 13.0f) {
            gradeViewHolder.gradeNumberTextView.setTextColor(Color.parseColor("#fe6161"));
        }
        gradeViewHolder.deleteGradeImageView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                GradeAdapter.this.course.grades.remove(i);
                GradeAdapter.this.course.updateFinalGrade();
                GradeAdapter.this.courseFinalGrade.setText(String.format("%.2f", new Object[]{Float.valueOf(GradeAdapter.this.course.getFinalGrade())}));
                GradeAdapter.this.notifyDataSetChanged();
                if (GradeAdapter.this.callback != null) {
                    GradeAdapter.this.callback.onHandleDeleteGrade(GradeAdapter.this.course);
                }
            }
        });
    }

    public int getItemCount() {
        return this.course.grades.size();
    }
}
