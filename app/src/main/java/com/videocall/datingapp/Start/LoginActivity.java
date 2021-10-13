package com.videocall.datingapp.Start;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.videocall.datingapp.Main.MainActivity;
import com.videocall.datingapp.Main.Splash_act;
import com.videocall.datingapp.R;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth userAuth;
    private EditText loginEmail;
    private EditText loginPass;
    private EditText edit_admin;
    private CheckBox check_admin;
    private CheckBox termspolicy;

    private Button btnLoginPageLogin;
    private Button btnLoginPageRegister;

    ProgressDialog dialog;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.login_activity);

        userAuth = FirebaseAuth.getInstance();

        loginEmail = (EditText) findViewById(R.id.loginEmailText);
        loginPass = (EditText) findViewById(R.id.loginPassText);
        edit_admin = (EditText) findViewById(R.id.edit_admin);
        btnLoginPageLogin = (Button) findViewById(R.id.btnLoginPageLogin);

        btnLoginPageRegister = findViewById(R.id.btnLoginPageRegister);
        check_admin = findViewById(R.id.check_admin);
        termspolicy = findViewById(R.id.termspolicy);

        check_admin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    edit_admin.setVisibility(View.VISIBLE);
                }
                else
                    edit_admin.setVisibility(View.GONE);

            }
        });

        dialog = new ProgressDialog(LoginActivity.this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);

        prefs = getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor myEdit = prefs.edit();

            btnLoginPageLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (termspolicy.isChecked()){
                    try {
                        if (check_admin.isChecked()&& edit_admin.getText()!=null){
                            if (!edit_admin.getText().toString().equals("92119211")){
                                Toast.makeText(LoginActivity.this, "invalid admin password.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }


                        String userEmail = loginEmail.getText().toString();
                        String userPass = loginPass.getText().toString();

                        if (!TextUtils.isEmpty(userEmail) && !TextUtils.isEmpty((userPass))) {

                            dialog.show();

                            userAuth.signInWithEmailAndPassword(userEmail, userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    dialog.dismiss();
                                    if (task.isSuccessful()) {

                                        if (edit_admin.getText()!=null && edit_admin.getText().toString().equals("92119211")){
                                            myEdit.putBoolean("isAdmin",true);
                                            myEdit.commit();
                                            myEdit.apply();
                                        }

                                        sendToMain();

                                    } else {
                                        String errorMessage = task.getException().getMessage();
                                        Toast.makeText(LoginActivity.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(LoginActivity.this, "Please enter your email and password!", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        Intent intent = new Intent(LoginActivity.this, Splash_act.class);
                        startActivity(intent);
                        finish();
                    }
                }else {
                    Toast.makeText(LoginActivity.this, "Please check tern and Condition", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnLoginPageRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }


    private void sendToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
