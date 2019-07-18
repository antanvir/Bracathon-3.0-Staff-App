package com.example.firebaseauthenticationtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText textUsername, textPassword;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView register = (TextView) findViewById(R.id.textViewRegister);
        register.setPaintFlags(register.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textUsername = (EditText) findViewById(R.id.textUsername);
        textPassword = (EditText) findViewById(R.id.textPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("Users");

        findViewById(R.id.textViewRegister).setOnClickListener(this);
        findViewById(R.id.btn_login).setOnClickListener(this);
    }


    private void userSignIn(){
        final String u_name = textUsername.getText().toString().trim();
        final String pwd = textPassword.getText().toString().trim();

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

        progressBar.setVisibility(View.VISIBLE);

//        Toast.makeText(getApplicationContext(), "Sign in successful",
//                Toast.LENGTH_SHORT).show();
        //String username = ref.child(u_name).child("email").getKey();
        ref.child(u_name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);


                if(user == null){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Wrong Username or password",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    String mail = user.getEmail();

                    mAuth.signInWithEmailAndPassword(mail, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                finish();
                                Toast.makeText(getApplicationContext(), "Sign in successful",
                                        Toast.LENGTH_SHORT).show();

                                progressBar.setVisibility(View.GONE);

//                                Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                                Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
                                intent.putExtra("arg", u_name);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);

                                textUsername.setText("");
                                textPassword.setText("");
                            }
                            else{
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "Wrong Username or password",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //System.out.println("The read failed: " + databaseError.getCode());
                Toast.makeText(getApplicationContext(), databaseError.getCode(),
                    Toast.LENGTH_SHORT).show();
            }
        });

        /*
        */

    }


    protected void onStart() {
        super.onStart();

//        if(mAuth.getCurrentUser() != null){
//            //handle already registered user
//            //finish();
//            startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
//        }

    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.textViewRegister:
                finish();
                startActivity(new Intent(this, SignUpActivity.class));
                break;

            case R.id.btn_login:
                userSignIn();
                break;
        }
    }
}
