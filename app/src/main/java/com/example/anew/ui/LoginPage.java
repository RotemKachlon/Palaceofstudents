package com.example.anew.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.anew.R;
import com.example.anew.moodle.StudentTask;
import com.example.anew.moodle.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//קוד זה מגדיר את דף הכניסת משתמש של האפליקציה
public class LoginPage extends AppCompatActivity implements View.OnClickListener {
    EditText EDTloginemail, EDTloginpassword;
    Button BTNlogin;
    SharedPreferences sp;
    User myuser;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef; DatabaseReference newRef;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        getSupportActionBar().hide();
        EDTloginemail = findViewById(R.id.EDTloginemail);
        EDTloginpassword = findViewById(R.id.EDTloginpassword);
        BTNlogin = findViewById(R.id.BTNlogin);
        sp=getSharedPreferences("details1",0);
        String STRloginemail = sp.getString("email", "");
        String STRloginpassword = sp.getString("password", "");
        EDTloginemail.setText(STRloginemail);
        EDTloginpassword.setText(STRloginpassword);
        BTNlogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == BTNlogin) {
            if (EDTloginemail.getText().toString().equals("")) {
                Toast.makeText(LoginPage.this, "Please fill Email", Toast.LENGTH_SHORT).show();
            } else if (EDTloginpassword.getText().toString().equals("")) {
                Toast.makeText(LoginPage.this, "Please fill Password", Toast.LENGTH_SHORT).show();
            } else {
                signIn(view);
            }
        }
    }
    public void signIn(View view) {
        mAuth.signInWithEmailAndPassword(
                        EDTloginemail.getText().toString(),
                        EDTloginpassword.getText().toString())
                .addOnCompleteListener(LoginPage.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "Sign In Success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            uid = user.getUid();
                            myRef = database.getReference("Persons").child(uid);
                            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    myuser = dataSnapshot.getValue(User.class);

                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString("email", EDTloginemail.getText().toString());
                                    editor.putString("password", EDTloginpassword.getText().toString());
                                    editor.putString("uid", uid);
                                    editor.putString("role", myuser.getRole());
                                    editor.commit();

                                    // Move role-check logic here, where myuser is initialized
                                    if (myuser.getRole().equals("Teacher")) {
                                        startActivity(new Intent(getApplicationContext(), TeacherChoosClasses.class));
                                    } else {
                                        startActivity(new Intent(getApplicationContext(), StudentChooseClasses.class));
                                    }

                                    Toast.makeText(LoginPage.this, "Authentication Success.", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });

                        } else {
                            Log.w("TAG", "Login failed: " + task.getException().getMessage());
                            Toast.makeText(LoginPage.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    }