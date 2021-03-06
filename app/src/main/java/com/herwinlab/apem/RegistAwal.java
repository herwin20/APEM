package com.herwinlab.apem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.herwinlab.apem.databaselogin.LoginDataBaseAdapter;

public class RegistAwal extends AppCompatActivity {

    EditText editTextUserName,editTextPassword,editTextConfirmPassword;
    LinearLayout btnCreateAccount;


    LoginDataBaseAdapter loginDataBaseAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        // get Instance  of Database Adapter
        loginDataBaseAdapter=new LoginDataBaseAdapter(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();

        // Get Refferences of Views
        editTextUserName=(EditText)findViewById(R.id.editTextUserName);
        editTextPassword=(EditText)findViewById(R.id.editTextPassword);
        editTextConfirmPassword=(EditText)findViewById(R.id.editTextConfirmPassword);

        btnCreateAccount=(LinearLayout)findViewById(R.id.buttonCreateAccount);
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub

                String userName=editTextUserName.getText().toString();
                String password=editTextPassword.getText().toString();
                String confirmPassword=editTextConfirmPassword.getText().toString();

                // check if any of the fields are vaccant
                if(userName.equals("")||password.equals("")||confirmPassword.equals(""))
                {
                    Snackbar.make(v, "Field Vaccant", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                // check if both password matches
                if(!password.equals(confirmPassword))
                {
                    Snackbar.make(v, "Password does not match", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                else
                {
                    // Save the Data in Database
                    loginDataBaseAdapter.insertEntry(userName, password);
                    Toast.makeText(getApplicationContext(), "Account Successfully Created ", Toast.LENGTH_LONG).show();
                    gotomain();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        loginDataBaseAdapter.close();
    }

    @Override
    public void onBackPressed() {
        startActivity (new Intent(RegistAwal.this, LoginActivity.class));
        //overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        RegistAwal.this.finish();
    }

    public void gotomain(){
        startActivity (new Intent(RegistAwal.this, LoginActivity.class));
        //overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        RegistAwal.this.finish();
    }
}
