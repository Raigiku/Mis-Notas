package com.example.android.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;

public class MainActivity extends AppCompatActivity {
    public static String filename;
    private Button creditosButton;
    private Button enterAppButton;
    private User mainUser;
    private EditText userNameEditText;

    /* renamed from: com.example.android.myapplication.MainActivity$1 */
    class C03181 implements OnClickListener {
        C03181() {
        }

        public void onClick(View view) {
            view = new Dialog(MainActivity.this);
            view.setContentView(C0330R.layout.credits_layout);
            view.show();
        }
    }

    /* renamed from: com.example.android.myapplication.MainActivity$2 */
    class C03192 implements OnClickListener {
        C03192() {
        }

        public void onClick(View view) {
            MainActivity.this.mainUser = new User();
            MainActivity.this.mainUser.userName = MainActivity.this.userNameEditText.getText().toString();
            MainActivity.filename = MainActivity.this.mainUser.userName;
            int i = 0;
            if (MainActivity.this.mainUser.userName.equals(BuildConfig.FLAVOR) == null) {
                view = MainActivity.this.getApplicationContext();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(MainActivity.filename);
                stringBuilder.append(".ser");
                view = view.getFileStreamPath(stringBuilder.toString());
                if (!(view == null || view.exists() == null)) {
                    i = 1;
                }
                if (i != 0) {
                    MainActivity.this.mainUser = (User) MainActivity.readFile(MainActivity.this.getApplicationContext(), MainActivity.filename);
                } else {
                    MainActivity.writeFile(MainActivity.this.getApplicationContext(), MainActivity.this.mainUser, MainActivity.filename);
                }
                MainActivity.this.saveLastUserName();
                view = new Intent(MainActivity.this.getApplicationContext(), MyCoursesActivity.class);
                view.putExtra("User", MainActivity.this.mainUser);
                MainActivity.this.startActivity(view);
                return;
            }
            Toast.makeText(MainActivity.this, "Por favor ingrese un nombre de usuario.", 0).show();
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0330R.layout.activity_main);
        this.userNameEditText = (EditText) findViewById(C0330R.id.userNameEditText);
        this.enterAppButton = (Button) findViewById(C0330R.id.enterAppButton);
        this.creditosButton = (Button) findViewById(C0330R.id.creditosButton);
        bundle = getApplicationContext().getFileStreamPath("lastusername.txt");
        bundle = (bundle == null || bundle.exists() == null) ? null : true;
        if (bundle != null) {
            this.userNameEditText.setText(loadLastUserName());
        }
        this.creditosButton.setOnClickListener(new C03181());
        this.enterAppButton.setOnClickListener(new C03192());
    }

    public void saveLastUserName() {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getApplicationContext().openFileOutput("lastusername.txt", 0));
            outputStreamWriter.write(this.mainUser.userName);
            outputStreamWriter.close();
        } catch (IOException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("File write failed: ");
            stringBuilder.append(e.toString());
            Log.e("Exception", stringBuilder.toString());
        }
    }

    public String loadLastUserName() {
        StringBuilder stringBuilder;
        String str = BuildConfig.FLAVOR;
        try {
            InputStream openFileInput = getApplicationContext().openFileInput("lastusername.txt");
            if (openFileInput == null) {
                return str;
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(openFileInput));
            StringBuilder stringBuilder2 = new StringBuilder();
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    stringBuilder2.append(readLine);
                } else {
                    openFileInput.close();
                    return stringBuilder2.toString();
                }
            }
        } catch (FileNotFoundException e) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("File not found: ");
            stringBuilder.append(e.toString());
            Log.e("login activity", stringBuilder.toString());
            return str;
        } catch (IOException e2) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Can not read file: ");
            stringBuilder.append(e2.toString());
            Log.e("login activity", stringBuilder.toString());
            return str;
        }
    }

    public static void writeFile(Context context, Serializable serializable, String str) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(".ser");
            str = context.openFileOutput(stringBuilder.toString(), 0);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(str);
            objectOutputStream.writeObject(serializable);
            objectOutputStream.close();
            str.close();
        } catch (Serializable serializable2) {
            serializable2.printStackTrace();
            Toast.makeText(context, "error write", 0).show();
        }
    }

    public static <T> T readFile(Context context, String str) {
        T readObject;
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(".ser");
            str = context.openFileInput(stringBuilder.toString());
            ObjectInputStream objectInputStream = new ObjectInputStream(str);
            readObject = objectInputStream.readObject();
            try {
                objectInputStream.close();
                str.close();
            } catch (Exception e) {
                str = e;
                str.printStackTrace();
                Toast.makeText(context, "error read", 0).show();
                return readObject;
            }
        } catch (Exception e2) {
            str = e2;
            readObject = null;
            str.printStackTrace();
            Toast.makeText(context, "error read", 0).show();
            return readObject;
        }
        return readObject;
    }
}
