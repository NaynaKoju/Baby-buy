package com.example.babybuy.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.babybuy.Database.Database;
import com.example.babybuy.R;

public class to_login extends AppCompatActivity {
    EditText Eemail, Epassword;
    Button Blogin, Bsignup;
    String email, password;
    CheckBox rmbMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loggingin_activity);
        Database db = new Database(this);
        Eemail = findViewById(R.id.inputEmail);
        Epassword = findViewById(R.id.inputPassword);
        Blogin = findViewById(R.id.btn);
        Bsignup = findViewById(R.id.losignup);
        rmbMe = findViewById(R.id.chkbx);

        SharedPreferences sp = getSharedPreferences("AUTH", MODE_PRIVATE);

        String spUsername = sp.getString("email", "");
        String spPasswd = sp.getString("password", "");

        if(!spUsername.isEmpty() && !spPasswd.isEmpty()){
            boolean i = db.checkemailandpassword(spUsername, spPasswd);
            if (i == true) {
                Intent homeIntent = new Intent(to_login.this, MAIN_main.class);
                startActivity(homeIntent);
            } else {
                Toast.makeText(to_login.this, "Invalid Credential. Couldn't login",
                        Toast.LENGTH_SHORT).show();
            }
        }

        Blogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //To convert edit text to string
                email = Eemail.getText().toString();
                password = Epassword.getText().toString();


                //in case there is null value in edit text
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(to_login.this, "Email and/or password empty! Make sure you entered it", Toast.LENGTH_SHORT).show();
                }

                //Checking password length & Email pattern
                else if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                    //here is how we created boolean variable for pop up message for successful login or failure
                    boolean i = db.checkemailandpassword(email, password);
                    if (i == true) {
                        Toast.makeText(to_login.this, "Successfully Logged in", Toast.LENGTH_SHORT).show();
                        Intent homeIntent = new Intent(to_login.this, MAIN_main.class);
                        if(rmbMe.isChecked()){
                            SharedPreferences.Editor editSp = sp.edit();
                            editSp.putString("email", email);
                            editSp.putString("password", password);
                            editSp.apply();
                        }
                        startActivity(homeIntent);
                    } else {
                        Toast.makeText(to_login.this, "Invalid Credential", Toast.LENGTH_SHORT).show();
                    }
                }
                //if email pattern is wrong show this message
                else {
                    Toast.makeText(to_login.this, "Re-enter your email please ", Toast.LENGTH_LONG).show();
                    Eemail.setError(" Valid email is required");
                }
            }
        });

        Bsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(to_login.this, registration.class));
            }
        });

    }

    public void getemail() {
        SharedPreferences sp=getSharedPreferences("Login", MODE_PRIVATE);
        SharedPreferences.Editor Ed=sp.edit();
        Ed.putString("email",email );
        Ed.apply();
    }
}
