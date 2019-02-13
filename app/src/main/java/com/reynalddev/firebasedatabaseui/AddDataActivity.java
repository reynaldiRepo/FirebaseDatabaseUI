package com.reynalddev.firebasedatabaseui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddDataActivity extends AppCompatActivity {

    EditText emailInput, nameInput, pwdInput, dobInput;
    RadioButton maleRb, femaleRb;
    Button AddBtn;

    //Firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        emailInput = (EditText)findViewById(R.id.inputEmail);
        nameInput = (EditText)findViewById(R.id.inputName);
        pwdInput = (EditText)findViewById(R.id.inputPwd);
        dobInput = (EditText)findViewById(R.id.inputDob);
        maleRb = (RadioButton)findViewById(R.id.male_radio_btn);
        femaleRb = (RadioButton)findViewById(R.id.female_radio_btn);
        AddBtn = (Button)findViewById(R.id.add_data_btn);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("UserData");

        AddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddDataFunc();
            }
        });
    }

    private void AddDataFunc() {
        Intent intent ;
        String Email = emailInput.getText().toString();
        String Name = nameInput.getText().toString();
        String pwd = pwdInput.getText().toString();
        String dob = dobInput.getText().toString();
        String gender = "";
        if (femaleRb.isChecked()){
            gender = "female";
        }else{
            gender = "male";
        }
        //Make Object UserData
        UserData userData = new UserData();
        userData.setEmail(Email);
        userData.setName(Name);
        userData.setPwd(pwd);
        userData.setDob(dob);
        userData.setGender(gender);

        databaseReference.push().setValue(userData);//for make unique id and post the data
        intent = new Intent(AddDataActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
