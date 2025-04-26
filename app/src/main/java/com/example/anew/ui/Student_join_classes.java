package com.example.anew.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.anew.R;
import com.example.anew.moodle.AdapterClasses;
import com.example.anew.moodle.Classroom;
import com.example.anew.moodle.Student;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//הקוד מגדיר את הדף של התלמיד שבו הוא מצטרף לכיתה חדשה מתוך המערכת
public class Student_join_classes extends AppCompatActivity {
    SharedPreferences sp;
    ListView lvClassesToJoin;
    String uid;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef, classRef, studentRef;
    ArrayList<Classroom> newClassesToJoin;
    ArrayList<String> newClasses;
    Student student;
    Classroom c;
    AdapterClasses adapterClasses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_join_classes);
        sp=getSharedPreferences("details1",0);
        uid = sp.getString("uid","");

        getUser();
        lvClassesToJoin=findViewById(R.id.lvClassesToJoin);
        lvClassesToJoin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                c= newClassesToJoin.get(position);
                if(c.getStudentsId()==null){
                    c.setStudentsId(new ArrayList<>());
                }
                if(!c.getStudentsId().contains(uid)){
                    student.addClass(c.getId());
                    studentRef = FirebaseDatabase.getInstance().getReference("Persons").child(uid);
                    studentRef.setValue(student);
                    c.AddStudend(uid);
                    classRef = FirebaseDatabase.getInstance().getReference("Classes").child(c.getId());
                    classRef.setValue(c);
                    Intent intent = new Intent(Student_join_classes.this, StudentChooseClasses.class);
                    startActivity(intent);
                }
            }
        });

    }
    private void getUser() {
        Log.d("TAG",uid);
        myRef = database.getReference("Persons").child(uid);
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                student = dataSnapshot.getValue(Student.class);

                getClasses();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }

    private void getClasses() {
        newClassesToJoin = new ArrayList<>();
        classRef = FirebaseDatabase.getInstance().getReference("Classes");

        classRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                newClassesToJoin.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Classroom c2 = data.getValue(Classroom.class);

                    // If student is not already in this class
                    if (student.getClassrooms() == null || !student.getClassrooms().contains(c2.getId())) {
                        newClassesToJoin.add(c2);
                    }
                }
                adapterClasses = new AdapterClasses(Student_join_classes.this, 0, 0, newClassesToJoin);
                lvClassesToJoin.setAdapter(adapterClasses);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }
    public void onCancelled(DatabaseError error) {
        // Failed to read value
        Log.w("TAG", "Failed to read value.", error.toException());
    }

}
