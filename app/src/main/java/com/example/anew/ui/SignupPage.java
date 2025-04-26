package com.example.anew.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.anew.R;
import com.example.anew.moodle.Teacher;
import com.example.anew.moodle.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//קוד זה מגדיר את דף ההרשמה של המשתמשים באפליקציה
public class SignupPage extends AppCompatActivity {

    EditText EDTsignupemail, EDTsignupusername, EDTsignuppassword, EDTsignupphone;
    String STRsignupemail,STRsignupusername, STRsignuppassword, STRsignupphone, STRrole;
    RadioGroup RADrole;
    RadioButton RADpicked;
    Button BTNsignup;
    SharedPreferences sp;
    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://newpalace-aae01-default-rtdb.firebaseio.com/");
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_signup_page);
        EDTsignupemail = findViewById(R.id.EDTsignupemail);
        EDTsignupusername = findViewById(R.id.EDTsignupusername);
        EDTsignuppassword = findViewById(R.id.EDTsignuppassword);
        EDTsignupphone = findViewById(R.id.EDTsignupphone);
        RADrole = findViewById(R.id.RADGRProlepick);
        BTNsignup = findViewById(R.id.BTNsignup);
        mAuth = FirebaseAuth.getInstance();
        sp=getSharedPreferences("details1",0);
        BTNsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                int RADchecked = RADrole.getCheckedRadioButtonId(); RADpicked = findViewById(RADchecked);
                if(RADpicked.isChecked()){
                    STRrole = RADpicked.getText().toString();
                }
                STRsignupemail = EDTsignupemail.getText().toString();
                STRsignupusername = EDTsignupusername.getText().toString();
                STRsignuppassword = EDTsignuppassword.getText().toString();
                STRsignupphone = EDTsignupphone.getText().toString();
                if(STRsignupemail.equals("")){
                    Toast.makeText(SignupPage.this, "Please fill email", Toast.LENGTH_SHORT).show();
                }
                else if(STRsignupusername.equals("")){
                    Toast.makeText(SignupPage.this, "Please fill username", Toast.LENGTH_SHORT).show();
                }
                else if(STRsignuppassword.equals("")){
                    Toast.makeText(SignupPage.this, "Please fill password", Toast.LENGTH_SHORT).show();
                }
                else if(STRsignupphone.equals("")){
                    Toast.makeText(SignupPage.this, "Please fill phone number", Toast.LENGTH_SHORT).show();
                }
                else{
                    signUp();
                }
            }
        });
    }
    private void signUp(){
        mAuth.createUserWithEmailAndPassword(STRsignupemail, STRsignuppassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task){
                if(task.isSuccessful()){
                    Log.d("TAG", "createUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    myRef = database.getReference("Persons").child(user.getUid());
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("email", STRsignupemail);
                    editor.putString("username", STRsignupusername);
                    editor.putString("password", STRsignuppassword);
                    editor.putString("phone", STRsignupphone);
                    editor.putString("uid", user.getUid());
                    editor.putString("role", STRrole);
                    editor.commit();

                    if (STRrole.equals("Teacher")) {
                        String tempSchoolID = ""; // You can fix this later after teacher picks school
                        Teacher teacher = new Teacher(user.getUid(), STRsignupusername, STRsignupemail, STRsignuppassword, STRsignupphone, STRrole, tempSchoolID);
                        myRef.setValue(teacher);
                    } else {
                        User p = new User(user.getUid(), STRsignupusername, STRsignupemail, STRsignuppassword, STRrole);
                        myRef.setValue(p);
                    }

                    Toast.makeText(SignupPage.this, "User created successfully", Toast.LENGTH_LONG).show();
                }
                else{
                    Log.w("TAG", "createUserWithEmail:failure", task.getException());
                    Toast.makeText(SignupPage.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}