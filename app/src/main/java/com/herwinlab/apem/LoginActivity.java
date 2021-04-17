package com.herwinlab.apem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.herwinlab.apem.databaselogin.LoginDataBaseAdapter;

public class LoginActivity extends AppCompatActivity {

    LoginDataBaseAdapter loginDataBaseAdapter;
    public static final String MyPREFERENCES = "MyPrefs";
    public  static final String TAG_USERNAME = "userName";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();

        SharedPreferences settings = getSharedPreferences(MyPREFERENCES, 0);
        if (settings.getString("logged", "").equals("logged")) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        final EditText editTextUserName = (EditText)findViewById(R.id.editTextUserNameToLogin);
        final EditText editTextPassword = (EditText)findViewById(R.id.editTextPasswordToLogin);

        TextView btnSignUp = (TextView) findViewById(R.id.regis);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistAwal.class);
                startActivity(intent);
                //overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        LinearLayout btnSignIn = (LinearLayout) findViewById(R.id.buttonSignIn);
        btnSignIn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                String userName = editTextUserName.getText().toString();
                String password = editTextPassword.getText().toString();
                String storedPassword = loginDataBaseAdapter
                        .getSinlgeEntry(userName);
                if (password.equals(storedPassword)) {
                    Toast.makeText(LoginActivity.this,
                            "Congrats: Login Successfull", Toast.LENGTH_LONG)
                            .show();

                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("userName", userName);
                    editor.putString("password", password);
                    editor.putString("logged", "logged");
                    editor.apply();

                    Intent main = new Intent(LoginActivity.this, MainActivity.class);
                    main.putExtra("userName", userName);
                    startActivity(main);
                } else {
                    Snackbar.make(v, "No. ID or Password does not match", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

    }
}
