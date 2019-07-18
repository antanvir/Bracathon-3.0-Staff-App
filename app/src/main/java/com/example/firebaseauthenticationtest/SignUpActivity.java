package com.example.firebaseauthenticationtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    EditText textUsername, textPassword, textEmail;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;
    DatabaseReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        textUsername = (EditText) findViewById(R.id.textUsername);
        textPassword = (EditText) findViewById(R.id.textPassword);
        textEmail = (EditText) findViewById(R.id.textEmail);
        progressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("Users");

        findViewById(R.id.buttonSignUp).setOnClickListener(this);
    }

    private void registerUser(){
        final String u_name = textUsername.getText().toString().trim();
        final String pwd = textPassword.getText().toString().trim();
        final String mail = textEmail.getText().toString().trim();

        if(u_name.isEmpty()){
            textUsername.setError("Username is required");
            textUsername.requestFocus();
            return;
        }

        if(pwd.isEmpty()){
            textPassword.setError("Password is required");
            textPassword.requestFocus();
            return;
        }
        if(pwd.length() < 6){
            textPassword.setError("Password must be of length 06 or more");
            textPassword.requestFocus();
            return;
        }

        if(mail.isEmpty()){
            textEmail.setError("Email is required");
            textEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            textEmail.setError("Please enter a valid email");
            textEmail.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(mail, pwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User( u_name, mail, pwd);

                            //String id = FirebaseAuth.getInstance().getCurrentUser().getUid();

//                            Toast.makeText(SignUpActivity.this, id,
//                                    Toast.LENGTH_LONG).show();

                            ref.child(u_name).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    progressBar.setVisibility(View.GONE);

                                    if (task.isSuccessful()) {
                                        Toast.makeText(SignUpActivity.this, "Registration successful",
                                                Toast.LENGTH_LONG).show();

                                        finish();
                                        Intent intent = new Intent(SignUpActivity.this, ProfileActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);

                                        textUsername.setText("");
                                        textEmail.setText("");
                                        textPassword.setText("");

                                    } else {
                                        //display a failure message
                                        Toast.makeText(getApplicationContext(), "User creation failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        } else {

                            Toast.makeText(getApplicationContext(), "Registration failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });


    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser() != null){
            //handle already registered user
            //finish();
            startActivity(new Intent(SignUpActivity.this, ProfileActivity.class));
        }
        else{

        }
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.buttonSignUp:
                registerUser();
                break;
        }
    }
}
