package com.reynalddev.firebasedatabaseui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateDataActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String keyExtra, emailExtra, nameExtra, pwdExtra, DOBExtra, genderExtra;
    EditText emailInputUpdate, nameInputUpdate, pwdInputUpdate, dobInputUpdate;
    RadioGroup genderGroup;
    RadioButton maleRbUpdate, femaleRbUpdate;
    Button UpdtBtn, deleteBtn;
    UserData UserDataUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_data);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("UserData");

        keyExtra = getIntent().getStringExtra("Key");
        emailExtra = getIntent().getStringExtra("Email");
        nameExtra = getIntent().getStringExtra("Name");
        pwdExtra = getIntent().getStringExtra("PWD");
        DOBExtra = getIntent().getStringExtra("DOB");
        genderExtra = getIntent().getStringExtra("Gender");
        UserDataUpdate = new UserData();

        emailInputUpdate = (EditText)findViewById(R.id.EdtEmail);
        nameInputUpdate = (EditText)findViewById(R.id.EdtName);
        pwdInputUpdate = (EditText)findViewById(R.id.EdtPwd);
        dobInputUpdate = (EditText)findViewById(R.id.EdtDob);
        maleRbUpdate = (RadioButton)findViewById(R.id.MaleUpdate);
        femaleRbUpdate = (RadioButton)findViewById(R.id.FemaleUpdate);
        genderGroup = (RadioGroup)findViewById(R.id.genderGroup);
        bindData();
        UpdtBtn = (Button)findViewById(R.id.Update_data_btn);
        deleteBtn = (Button)findViewById(R.id.Delete_data_btn);

        UpdtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gender ;
                UserDataUpdate.setEmail(emailInputUpdate.getText().toString());
                UserDataUpdate.setName(nameInputUpdate.getText().toString());
                UserDataUpdate.setPwd(pwdInputUpdate.getText().toString());
                UserDataUpdate.setDob(dobInputUpdate.getText().toString());
                if (maleRbUpdate.isChecked()){
                    gender = "male";
                }else {
                    gender = "female";
                }
                UserDataUpdate.setGender(gender);

                databaseReference.child(keyExtra).setValue(UserDataUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(UpdateDataActivity.this, "Update Succses", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateDataActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child(keyExtra).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(UpdateDataActivity.this, "Delete Data Succses", Toast.LENGTH_SHORT).show();
                        Intent DelIntent = new Intent(UpdateDataActivity.this, MainActivity.class);
                        startActivity(DelIntent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateDataActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
    private void bindData() {
        emailInputUpdate.setText(emailExtra);
        nameInputUpdate.setText(nameExtra);
        pwdInputUpdate.setText(pwdExtra);
        dobInputUpdate.setText(DOBExtra);
        switch (genderExtra){
            case "male":
                maleRbUpdate.setChecked(true);
                break;
            case  "female":
                femaleRbUpdate.setChecked(true);
                break;

        }
    }
}
