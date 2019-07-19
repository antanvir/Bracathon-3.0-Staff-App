package com.example.firebaseauthenticationtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class WorkStatusActivity extends AppCompatActivity implements View.OnClickListener  {
    EditText UID, InsNo, InsAm, savedAm, feedback;
    TextView loanDetails;
    RadioButton missed, exact, less;
    CheckBox saved;
    Button done;
    String mid, msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_status);

        UID = (EditText) findViewById(R.id.ID);
        UID.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                mid = UID.getText().toString();
                int am = 50000;
                Double inst = 50000*0.10;
                msg = " Loan taken:  " + am + " Taka \n Instalment per month: " + inst + " Taka";

                loanDetails.setVisibility(View.VISIBLE);
                loanDetails.setText(msg);

                // you can call or do what you want with your EditText here

                // yourEditText...
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        InsNo = (EditText) findViewById(R.id.instalment);
        InsAm =  findViewById(R.id.instalmentAmount);
        savedAm = (EditText) findViewById(R.id.savingQ);
        feedback = (EditText) findViewById(R.id.feedback);

        saved = findViewById(R.id.savings);
        missed = findViewById(R.id.overdue);
        exact = findViewById(R.id.exact);
        less = findViewById(R.id.less);

        done = findViewById(R.id.buttonDone);
        loanDetails = findViewById(R.id.loan);





//        if(!UID.getText().toString().isEmpty()){
//            mid = UID.getText().toString();
//            int am = 50000;
//            Double inst = 50000*0.10;
//            msg = " Loan taken:  " + am + " Taka \n Instalment per month: " + inst + " Taka";
//
//            loanDetails.setVisibility(View.VISIBLE);
//            loanDetails.setText(msg);
//        }

        saved.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    Toast.makeText(WorkStatusActivity.this,
                            "Savings checkbox checked", Toast.LENGTH_LONG).show();
                    findViewById(R.id.savingH).setVisibility(View.VISIBLE);
                }
                else{
                    findViewById(R.id.savingH).setVisibility(View.INVISIBLE);
                }

            }
        });



        findViewById(R.id.buttonDone).setOnClickListener((View.OnClickListener) this);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.exact:
                if (checked)
                    // Pirates are the best
                    break;
            case R.id.overdue:
                if (checked)
                    // Ninjas rule
                    break;
            case R.id.less:
                if (checked)
                    // Ninjas rule
                    break;
        }
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.buttonDone:
                //finish();


                UID.setText("");
                InsAm.setText("");
                InsNo.setText("");
                savedAm.setText("");
                feedback.setText("");

                loanDetails.setVisibility(View.INVISIBLE);
                findViewById(R.id.savingH).setVisibility(View.INVISIBLE);

                Toast.makeText(WorkStatusActivity.this, "Member info updated", Toast.LENGTH_LONG).show();

                startActivity(new Intent(this, WorkStatusActivity.class));
                break;

//            case R.id.btn_login:
//                userSignIn();
//                break;
        }
    }
}
