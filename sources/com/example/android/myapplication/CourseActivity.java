package com.example.android.myapplication;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CourseActivity extends AppCompatActivity {
    private GradeAdapter adapter;
    private Button addGradeButton;
    private CallbackAddGrade callback;
    private Course course;
    private ConstraintLayout courseActivityConstraint;
    private TextView courseFinalGrade;
    private String courseName;
    private TextView courseTextView;
    private Button finalGradeButton;
    private View finalGradeView;
    private ImageView goBackImageView;
    private LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private TextView teacherCourseTextView;
    private String teacherName;

    /* renamed from: com.example.android.myapplication.CourseActivity$1 */
    class C03091 implements OnClickListener {
        C03091() {
        }

        public void onClick(View view) {
            if (CourseActivity.this.course.getFinalPercentage() >= 100) {
                Toast.makeText(view.getContext(), "Todas las notas ya están colocadas.", 0).show();
                return;
            }
            view = new Dialog(CourseActivity.this);
            view.setContentView(C0330R.layout.final_exam_layout);
            view.show();
            Button button = (Button) view.findViewById(C0330R.id.okButton);
            final EditText editText = (EditText) view.findViewById(C0330R.id.finalPossibleGradeEditText);
            ((EditText) view.findViewById(C0330R.id.wantedFinalGradeEditText)).addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                public void afterTextChanged(Editable editable) {
                    if (!editable.toString().isEmpty()) {
                        double parseFloat = (double) (Float.parseFloat(editable.toString()) - CourseActivity.this.course.getFinalGrade());
                        double finalPercentage = (double) (100 - CourseActivity.this.course.getFinalPercentage());
                        Double.isNaN(finalPercentage);
                        finalPercentage /= 100.0d;
                        Double.isNaN(parseFloat);
                        parseFloat /= finalPercentage;
                        if (parseFloat < 0.0d) {
                            editable = "No es necesario dar examen final.";
                        } else if (parseFloat > 20.0d) {
                            editable = "No se puede alcanzar la nota deseada.";
                        } else {
                            editable = String.format("%.2f", new Object[]{Double.valueOf(parseFloat)});
                        }
                        editText.setText(editable);
                    }
                }
            });
            button.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    view.cancel();
                }
            });
        }
    }

    /* renamed from: com.example.android.myapplication.CourseActivity$2 */
    class C03102 implements OnClickListener {
        C03102() {
        }

        public void onClick(View view) {
            CourseActivity.this.finish();
        }
    }

    /* renamed from: com.example.android.myapplication.CourseActivity$3 */
    class C03133 implements OnClickListener {
        C03133() {
        }

        public void onClick(View view) {
            view = new Dialog(CourseActivity.this);
            view.setContentView(C0330R.layout.activity_new_grade);
            view.show();
            final EditText editText = (EditText) view.findViewById(C0330R.id.gradeNameEditTex);
            final EditText editText2 = (EditText) view.findViewById(C0330R.id.gradePercentageEditText);
            final EditText editText3 = (EditText) view.findViewById(C0330R.id.gradeNumberEditText);
            Button button = (Button) view.findViewById(C0330R.id.addGradeButton);
            Button button2 = (Button) view.findViewById(C0330R.id.cancelGradeButton);
            ((TextView) view.findViewById(C0330R.id.lastCourseNameTextView)).setText(CourseActivity.this.courseName);
            final View view2 = view;
            button.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    view = editText.getText().toString();
                    String obj = editText2.getText().toString();
                    String obj2 = editText3.getText().toString();
                    if (!(view.matches(BuildConfig.FLAVOR) || obj.matches(BuildConfig.FLAVOR))) {
                        if (!obj2.matches(BuildConfig.FLAVOR)) {
                            if (CourseActivity.this.course.getFinalPercentage() + Integer.parseInt(obj) > 100) {
                                Toast.makeText(CourseActivity.this.getApplicationContext(), "El porcentage máximo del curso no puede sobrepasar el 100%.", 0).show();
                                return;
                            } else if (Integer.parseInt(obj) > 100) {
                                Toast.makeText(CourseActivity.this.getApplicationContext(), "El peso no puede superar 100%.", 0).show();
                                return;
                            } else if (Integer.parseInt(obj) <= 0) {
                                Toast.makeText(CourseActivity.this.getApplicationContext(), "El peso no puede ser 0% o menos.", 0).show();
                                return;
                            } else if (Float.parseFloat(obj2) > 20.0f) {
                                Toast.makeText(CourseActivity.this.getApplicationContext(), "La nota no puede ser mayor de 20.", 0).show();
                                return;
                            } else if (Float.parseFloat(obj2) < 0.0f) {
                                Toast.makeText(CourseActivity.this.getApplicationContext(), "La nota no puede ser negativa.", 0).show();
                                return;
                            } else {
                                CourseActivity.this.course.grades.add(new Grade(view, Integer.parseInt(obj), Float.parseFloat(obj2)));
                                CourseActivity.this.adapter.notifyDataSetChanged();
                                CourseActivity.this.course.updateFinalGrade();
                                CourseActivity.this.courseFinalGrade.setText(String.format("%.2f", new Object[]{Float.valueOf(CourseActivity.this.course.getFinalGrade())}));
                                view2.cancel();
                                if (CourseActivity.this.callback != null) {
                                    CourseActivity.this.callback.onHandleAddGrade(CourseActivity.this.course);
                                    return;
                                }
                                return;
                            }
                        }
                    }
                    Toast.makeText(CourseActivity.this.getApplicationContext(), "Rellenar los campos vacios.", 0).show();
                }
            });
            button2.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    view.cancel();
                }
            });
        }
    }

    public interface CallbackAddGrade {
        void onHandleAddGrade(Course course);
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0330R.layout.activity_course);
        this.recyclerView = (RecyclerView) findViewById(C0330R.id.gradesRecyclerView);
        this.courseTextView = (TextView) findViewById(C0330R.id.courseTextView);
        this.teacherCourseTextView = (TextView) findViewById(C0330R.id.teacherCourseTextView);
        this.courseFinalGrade = (TextView) findViewById(C0330R.id.finalGradeNumberTextView);
        this.addGradeButton = (Button) findViewById(C0330R.id.addGradeButton);
        this.finalGradeButton = (Button) findViewById(C0330R.id.finalGradeButton);
        this.goBackImageView = (ImageView) findViewById(C0330R.id.goBackImageView);
        this.finalGradeView = findViewById(C0330R.id.finalGradeView);
        this.courseActivityConstraint = (ConstraintLayout) findViewById(C0330R.id.courseActivityConstraint);
        this.layoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(this.layoutManager);
        this.course = (Course) getIntent().getParcelableExtra("Course");
        try {
            this.callback = (CallbackAddGrade) MyCoursesActivity.helloContext;
        } catch (Bundle bundle2) {
            Log.e("MyAdapter", "Must implement the CallbackInterface in the Activity", bundle2);
        }
        bundle2 = Color.parseColor("#485A6A");
        switch (this.course.getThemeCode()) {
            case 1:
                bundle2 = Color.parseColor("#5eb9ff");
                break;
            case 2:
                bundle2 = Color.parseColor("#485a6a");
                break;
            case 3:
                bundle2 = Color.parseColor("#eb7979");
                break;
            case 4:
                bundle2 = Color.parseColor("#ebae79");
                break;
            case 5:
                bundle2 = Color.parseColor("#937afc");
                break;
            case 6:
                bundle2 = Color.parseColor("#f8cd76");
                break;
            case 7:
                bundle2 = Color.parseColor("#4e90c4");
                break;
            case 8:
                bundle2 = Color.parseColor("#5dd8a7");
                break;
            default:
                break;
        }
        if (this.course.getThemeCode() != 4) {
            this.finalGradeButton.getBackground().setColorFilter(bundle2, Mode.SRC_OVER);
            this.finalGradeView.getBackground().setColorFilter(bundle2, Mode.SRC_OVER);
            this.courseActivityConstraint.getBackground().setTint(bundle2);
        } else {
            this.finalGradeButton.setBackground(getResources().getDrawable(C0330R.drawable.round_rectangle7_grad));
            this.finalGradeView.setBackground(getResources().getDrawable(C0330R.drawable.round_rectangle6_grad));
            this.courseActivityConstraint.setBackground(getResources().getDrawable(C0330R.drawable.course_background_grad));
        }
        this.courseName = this.course.getName();
        this.teacherName = this.course.getTeacherName();
        this.courseTextView.setText(this.courseName);
        this.teacherCourseTextView.setText(this.teacherName);
        this.adapter = new GradeAdapter(this.course, this.courseFinalGrade);
        this.recyclerView.setAdapter(this.adapter);
        this.adapter.notifyDataSetChanged();
        if (this.course.grades.size() > null) {
            this.course.updateFinalGrade();
            this.courseFinalGrade.setText(String.format("%.2f", new Object[]{Float.valueOf(this.course.getFinalGrade())}));
        }
        this.finalGradeButton.setOnClickListener(new C03091());
        this.goBackImageView.setOnClickListener(new C03102());
        this.addGradeButton.setOnClickListener(new C03133());
    }
}
