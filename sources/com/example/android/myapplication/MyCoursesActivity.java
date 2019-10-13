package com.example.android.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.android.myapplication.CourseActivity.CallbackAddGrade;
import com.example.android.myapplication.CourseAdapter.CallbackInterface;
import com.example.android.myapplication.GradeAdapter.CallbackDeleteGrade;

public class MyCoursesActivity extends AppCompatActivity implements CallbackInterface, CallbackAddGrade, CallbackDeleteGrade {
    static final int REQUEST_CODE = 1;
    public static Context helloContext;
    private CourseAdapter adapter;
    private Button addCourseButton;
    private LayoutManager layoutManager;
    private User mainUser;
    private TextView noCoursesTextView;
    private RecyclerView recyclerView;

    /* renamed from: com.example.android.myapplication.MyCoursesActivity$1 */
    class C03201 implements OnClickListener {
        C03201() {
        }

        public void onClick(View view) {
            view = new Intent(MyCoursesActivity.this.getApplicationContext(), NewCourseActivity.class);
            view.putExtra("Courses", MyCoursesActivity.this.mainUser.courses);
            MyCoursesActivity.this.startActivityForResult(view, 1);
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0330R.layout.activity_my_courses);
        helloContext = this;
        this.mainUser = (User) getIntent().getParcelableExtra("User");
        this.recyclerView = (RecyclerView) findViewById(C0330R.id.recyclerView);
        this.addCourseButton = (Button) findViewById(C0330R.id.addCourseButton);
        this.noCoursesTextView = (TextView) findViewById(C0330R.id.noCoursesTextView);
        this.layoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(this.layoutManager);
        this.adapter = new CourseAdapter(this.mainUser.courses, this);
        this.recyclerView.setAdapter(this.adapter);
        this.adapter.notifyDataSetChanged();
        if (this.mainUser.courses.size() == null) {
            this.noCoursesTextView.setVisibility(0);
        } else {
            this.noCoursesTextView.setVisibility(4);
        }
        this.addCourseButton.setOnClickListener(new C03201());
    }

    protected void onActivityResult(int i, int i2, @Nullable Intent intent) {
        try {
            super.onActivityResult(i, i2, intent);
            if (i == 1 && i2 == -1) {
                this.mainUser.courses.add((Course) intent.getParcelableExtra("New Course"));
                MainActivity.writeFile(getApplicationContext(), this.mainUser, MainActivity.filename);
                this.noCoursesTextView.setVisibility(4);
                this.adapter.notifyDataSetChanged();
                i2 = new StringBuilder();
                i2.append("grades size: ");
                i2.append(((Course) this.mainUser.courses.get(0)).grades.size());
                Log.d("grades", i2.toString());
            }
        } catch (int i3) {
            Toast.makeText(getApplicationContext(), i3.toString(), 0).show();
        }
    }

    public void onHandleRemove() {
        if (this.mainUser.courses.size() == 0) {
            this.noCoursesTextView.setVisibility(0);
        }
        MainActivity.writeFile(this, this.mainUser, MainActivity.filename);
    }

    public void onHandleAddGrade(Course course) {
        for (int i = 0; i < this.mainUser.courses.size(); i++) {
            if (((Course) this.mainUser.courses.get(i)).getName().equals(course.getName())) {
                this.mainUser.courses.set(i, course);
                break;
            }
        }
        MainActivity.writeFile(this, this.mainUser, MainActivity.filename);
    }

    public void onHandleDeleteGrade(Course course) {
        for (int i = 0; i < this.mainUser.courses.size(); i++) {
            if (((Course) this.mainUser.courses.get(i)).getName().equals(course.getName())) {
                this.mainUser.courses.set(i, course);
                break;
            }
        }
        MainActivity.writeFile(this, this.mainUser, MainActivity.filename);
    }
}
