package com.example.anew.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.anew.R;
import com.example.anew.moodle.Classroom;
import com.example.anew.moodle.Teacher;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//הקוד מגדיר את הדף בו המורה יוצר כיתה ומוסיף אותה למערכת ולרשימת הכיתות של בית הספר שלו
public class TeacherCreateClasses extends AppCompatActivity {
    EditText etClassNameTeacherCreateClass, etLayerTeacherCreateClass;
    Button btnJoinClass;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    SharedPreferences sp;
    String uid, schoolID;
    Teacher teacher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_create_classes);

        etClassNameTeacherCreateClass =findViewById(R.id.etClassNameTeacherCreateClass);
        etLayerTeacherCreateClass =findViewById(R.id.etLayerTeacherCreateClass);
        btnJoinClass=findViewById(R.id.btnJoinClass);
        sp=getSharedPreferences("details1",0);
        uid = sp.getString("uid","");
        getUser();
        btnJoinClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etClassNameTeacherCreateClass.getText().toString().equals("")|| etLayerTeacherCreateClass.getText().toString().equals(""))
                {
                    Toast.makeText(TeacherCreateClasses.this, "נא למלא את כל הפרטים",
                            Toast.LENGTH_SHORT).show();
                }
                else
                {
                    myRef = database.getReference("Classes").push();
                    Classroom classes = new Classroom(myRef.getKey(), etClassNameTeacherCreateClass.getText().toString(), uid);
                    myRef.setValue(classes);
                    teacher.addClass(myRef.getKey());

                    myRef = database.getReference("Persons").child(uid);
                    myRef.setValue(teacher);

                    myRef = database.getReference("School").child(schoolID);
                    Intent intent = new Intent(TeacherCreateClasses.this, TeacherChoosClasses.class);
                    startActivity(intent);
                }
            }
        });

    }
    private void getUser() {

        myRef = database.getReference("Persons").child(uid);
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("TAG", "Snapshot: " + dataSnapshot); // ADD THIS
                teacher = dataSnapshot.getValue(Teacher.class);
                if (teacher != null) {
                    schoolID = teacher.getSchoolID();
                    getSchool();
                } else {
                    Log.e("TAG", "Teacher is null!");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });

    }
    private void getSchool() {

        myRef = database.getReference("School").child(schoolID);
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });

    }
}