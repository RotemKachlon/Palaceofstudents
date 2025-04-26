package com.example.anew.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

//הקוד מגדיר את הדף של התלמיד בו מוצגת לו רשימת הכיתות שלו בליסטויו והוא יכול דרך לחיצה להכנס לפרטי הכיתה, ודרך הדף הזה גם ניגשים לדף בו הוא מצטרף לכיתות חדשות. בנוסף, ניתן דרך הדף לגשת לתפריט השלוש נקודות - דרכו המשתמש יכול לגשת חזרה ל-דף הבית, דף כניסת משתמש, דף הרשמת משתמש, ודף אודות האפליקציה
public class StudentChooseClasses extends AppCompatActivity {
    ListView lvClassesStudent;
    Button btnJoinClass;
    TextView tvMyStudentClasses;
    SharedPreferences sp;
    String uid, role;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef, classRef;
    AdapterClasses adapterClasses;
    ArrayList<Classroom> classesArrayList;
    Student student;
    Classroom c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_choose_classes);
        lvClassesStudent = findViewById(R.id.lvClassesStudent);
        btnJoinClass = findViewById(R.id.btnJoinClass);
        btnJoinClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentChooseClasses.this, Student_join_classes.class);
                startActivity(intent);

            }
        });
        tvMyStudentClasses = findViewById(R.id.tvMyStudentClasses);
        sp=getSharedPreferences("details1",0);
        uid = sp.getString("uid","");
        role = sp.getString("role","");

        getUser();
        lvClassesStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                c = classesArrayList.get(position);
                Intent intent = new Intent(StudentChooseClasses.this, ClassActivity.class);
                intent.putExtra("c", c.getId());
                intent.putExtra("c2", c.getStudentsId());
                startActivity(intent);
            }
        });
    }
    private void getUser() {
        Log.d("TAG",uid);
        myRef = database.getReference("Persons").child(uid);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                student = dataSnapshot.getValue(Student.class);
                if (student.getClassrooms()!=null){
                    tvMyStudentClasses.setText("הכיתות של "+ student.getUsername());
                    retriveData();
                }
                else
                    tvMyStudentClasses.setText("לא נמצאו כיתות לתלמיד: "+ student.getUsername());
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }



    public void retriveData()
    {
        classRef = FirebaseDatabase.getInstance().getReference("Classes");
        classRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                classesArrayList = new ArrayList<>();

                for(DataSnapshot data: dataSnapshot.getChildren()) {
                    Classroom c = data.getValue(Classroom.class);
                    if(student.getClassrooms().contains(c.getId())) {
                        classesArrayList.add(c);
                    }
                }
                adapterClasses = new AdapterClasses(StudentChooseClasses.this, 0, 0, classesArrayList);
                lvClassesStudent.setAdapter(adapterClasses);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuui, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // Get the context of the activity
        Context context = getApplicationContext();

        // Handle menu item clicks
        if (id == R.id.menu_about) {
            // Handle "About" menu item click
            startActivity(new Intent(context, AboutPage.class));
            return true;
        } else if (id == R.id.menu_main_menu) {
            // Handle "Main Menu" menu item click
            startActivity(new Intent(context, HomePage.class));
            return true;
        } else if (id == R.id.menu_login) {
            // Handle "Login" menu item click
            startActivity(new Intent(context, LoginPage.class));
            return true;
        } else if (id == R.id.menu_signup) {
            // Handle "Sign Up" menu item click
            startActivity(new Intent(context, SignupPage.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
