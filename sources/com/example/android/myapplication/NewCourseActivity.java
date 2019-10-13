package com.example.android.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Iterator;

public class NewCourseActivity extends AppCompatActivity {
    private Button addCourseButton;
    private TextView courseNameTextView;
    private TextView teacherNameTextView;
    private ArrayList<Button> themeButtons;
    private int themeCode;

    /* renamed from: com.example.android.myapplication.NewCourseActivity$1 */
    class C03211 implements OnClickListener {
        C03211() {
        }

        public void onClick(View view) {
            NewCourseActivity.this.themeCode = 1;
            int i = 0;
            ((Button) NewCourseActivity.this.themeButtons.get(0)).getBackground().setColorFilter(null);
            while (i < 8) {
                if (i != NewCourseActivity.this.themeCode - 1) {
                    ((Button) NewCourseActivity.this.themeButtons.get(i)).getBackground().setColorFilter(Color.parseColor("#8c8c8c"), Mode.DARKEN);
                }
                i++;
            }
        }
    }

    /* renamed from: com.example.android.myapplication.NewCourseActivity$2 */
    class C03222 implements OnClickListener {
        C03222() {
        }

        public void onClick(View view) {
            NewCourseActivity.this.themeCode = 2;
            ((Button) NewCourseActivity.this.themeButtons.get(1)).getBackground().setColorFilter(null);
            for (view = null; view < 8; view++) {
                if (view != NewCourseActivity.this.themeCode - 1) {
                    ((Button) NewCourseActivity.this.themeButtons.get(view)).getBackground().setColorFilter(Color.parseColor("#8c8c8c"), Mode.DARKEN);
                }
            }
        }
    }

    /* renamed from: com.example.android.myapplication.NewCourseActivity$3 */
    class C03233 implements OnClickListener {
        C03233() {
        }

        public void onClick(View view) {
            NewCourseActivity.this.themeCode = 3;
            ((Button) NewCourseActivity.this.themeButtons.get(2)).getBackground().setColorFilter(null);
            for (view = null; view < 8; view++) {
                if (view != NewCourseActivity.this.themeCode - 1) {
                    ((Button) NewCourseActivity.this.themeButtons.get(view)).getBackground().setColorFilter(Color.parseColor("#8c8c8c"), Mode.DARKEN);
                }
            }
        }
    }

    /* renamed from: com.example.android.myapplication.NewCourseActivity$4 */
    class C03244 implements OnClickListener {
        C03244() {
        }

        public void onClick(View view) {
            NewCourseActivity.this.themeCode = 4;
            ((Button) NewCourseActivity.this.themeButtons.get(3)).getBackground().setColorFilter(null);
            for (view = null; view < 8; view++) {
                if (view != NewCourseActivity.this.themeCode - 1) {
                    ((Button) NewCourseActivity.this.themeButtons.get(view)).getBackground().setColorFilter(Color.parseColor("#8c8c8c"), Mode.DARKEN);
                }
            }
        }
    }

    /* renamed from: com.example.android.myapplication.NewCourseActivity$5 */
    class C03255 implements OnClickListener {
        C03255() {
        }

        public void onClick(View view) {
            NewCourseActivity.this.themeCode = 5;
            ((Button) NewCourseActivity.this.themeButtons.get(4)).getBackground().setColorFilter(null);
            for (view = null; view < 8; view++) {
                if (view != NewCourseActivity.this.themeCode - 1) {
                    ((Button) NewCourseActivity.this.themeButtons.get(view)).getBackground().setColorFilter(Color.parseColor("#8c8c8c"), Mode.DARKEN);
                }
            }
        }
    }

    /* renamed from: com.example.android.myapplication.NewCourseActivity$6 */
    class C03266 implements OnClickListener {
        C03266() {
        }

        public void onClick(View view) {
            NewCourseActivity.this.themeCode = 6;
            ((Button) NewCourseActivity.this.themeButtons.get(5)).getBackground().setColorFilter(null);
            for (view = null; view < 8; view++) {
                if (view != NewCourseActivity.this.themeCode - 1) {
                    ((Button) NewCourseActivity.this.themeButtons.get(view)).getBackground().setColorFilter(Color.parseColor("#8c8c8c"), Mode.DARKEN);
                }
            }
        }
    }

    /* renamed from: com.example.android.myapplication.NewCourseActivity$7 */
    class C03277 implements OnClickListener {
        C03277() {
        }

        public void onClick(View view) {
            NewCourseActivity.this.themeCode = 7;
            ((Button) NewCourseActivity.this.themeButtons.get(6)).getBackground().setColorFilter(null);
            for (view = null; view < 8; view++) {
                if (view != NewCourseActivity.this.themeCode - 1) {
                    ((Button) NewCourseActivity.this.themeButtons.get(view)).getBackground().setColorFilter(Color.parseColor("#8c8c8c"), Mode.DARKEN);
                }
            }
        }
    }

    /* renamed from: com.example.android.myapplication.NewCourseActivity$8 */
    class C03288 implements OnClickListener {
        C03288() {
        }

        public void onClick(View view) {
            NewCourseActivity.this.themeCode = 8;
            ((Button) NewCourseActivity.this.themeButtons.get(7)).getBackground().setColorFilter(null);
            for (view = null; view < 8; view++) {
                if (view != NewCourseActivity.this.themeCode - 1) {
                    ((Button) NewCourseActivity.this.themeButtons.get(view)).getBackground().setColorFilter(Color.parseColor("#8c8c8c"), Mode.DARKEN);
                }
            }
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0330R.layout.activity_new_course);
        this.courseNameTextView = (TextView) findViewById(C0330R.id.courseNameEditText);
        this.teacherNameTextView = (TextView) findViewById(C0330R.id.teacherNameEditText);
        this.addCourseButton = (Button) findViewById(C0330R.id.addCourseButton);
        this.themeButtons = new ArrayList();
        this.themeButtons.add((Button) findViewById(C0330R.id.theme1Button));
        this.themeButtons.add((Button) findViewById(C0330R.id.theme2Button));
        this.themeButtons.add((Button) findViewById(C0330R.id.theme3Button));
        this.themeButtons.add((Button) findViewById(C0330R.id.theme4Button));
        this.themeButtons.add((Button) findViewById(C0330R.id.theme5Button));
        this.themeButtons.add((Button) findViewById(C0330R.id.theme6Button));
        this.themeButtons.add((Button) findViewById(C0330R.id.theme7Button));
        this.themeButtons.add((Button) findViewById(C0330R.id.theme8Button));
        ((Button) this.themeButtons.get(0)).setOnClickListener(new C03211());
        ((Button) this.themeButtons.get(1)).setOnClickListener(new C03222());
        ((Button) this.themeButtons.get(2)).setOnClickListener(new C03233());
        ((Button) this.themeButtons.get(3)).setOnClickListener(new C03244());
        ((Button) this.themeButtons.get(4)).setOnClickListener(new C03255());
        ((Button) this.themeButtons.get(5)).setOnClickListener(new C03266());
        ((Button) this.themeButtons.get(6)).setOnClickListener(new C03277());
        ((Button) this.themeButtons.get(7)).setOnClickListener(new C03288());
        bundle = getIntent().getParcelableArrayListExtra("Courses");
        this.addCourseButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                view = NewCourseActivity.this.courseNameTextView.getText().toString();
                String charSequence = NewCourseActivity.this.teacherNameTextView.getText().toString();
                int i = 0;
                if (!view.matches(BuildConfig.FLAVOR)) {
                    if (!charSequence.matches(BuildConfig.FLAVOR)) {
                        Iterator it = bundle.iterator();
                        while (it.hasNext()) {
                            if (((Course) it.next()).getName().equals(view)) {
                                Context applicationContext = NewCourseActivity.this.getApplicationContext();
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("El curso ");
                                stringBuilder.append(view);
                                stringBuilder.append(" ya existe.");
                                Toast.makeText(applicationContext, stringBuilder.toString(), 0).show();
                                break;
                            }
                        }
                        i = 1;
                        if (i != 0) {
                            Parcelable course = new Course(view, charSequence, NewCourseActivity.this.themeCode);
                            view = NewCourseActivity.this.getIntent();
                            view.putExtra("New Course", course);
                            NewCourseActivity.this.setResult(-1, view);
                            NewCourseActivity.this.finish();
                        }
                    }
                }
                Toast.makeText(NewCourseActivity.this.getApplicationContext(), "Rellenar los campos vacios.", 0).show();
                if (i != 0) {
                    Parcelable course2 = new Course(view, charSequence, NewCourseActivity.this.themeCode);
                    view = NewCourseActivity.this.getIntent();
                    view.putExtra("New Course", course2);
                    NewCourseActivity.this.setResult(-1, view);
                    NewCourseActivity.this.finish();
                }
            }
        });
    }

    public void goToPreviousActivity(View view) {
        finish();
    }
}
