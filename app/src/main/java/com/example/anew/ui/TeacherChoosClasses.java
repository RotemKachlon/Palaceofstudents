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
import com.example.anew.moodle.Teacher;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//הקוד מגדיר את הדף של המורה, שבו הכיתות שלו שיצר מוצגות בליסטויו, והוא יכול להכנס לפרטי הכיתה בעזרת לחיצה. בנוסף ניתן לגשת לדף יצירת הכיתה דרך הדף. בנוסף, ניתן דרך הדף לגשת לתפריט השלוש נקודות - דרכו המשתמש יכול לגשת חזרה ל-דף הבית, דף כניסת משתמש, דף הרשמת משתמש, ודף אודות האפליקציה
public class TeacherChoosClasses extends AppCompatActivity {
    ListView lvClassesTeacher;
    TextView tvMyClasses;
    SharedPreferences sp;
    String uid, role, schoolId;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef, classRef;
    AdapterClasses adapterClasses;
    ArrayList<Classroom> classesArrayList;
    Teacher teacher;
    Button btnAddClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_choos_classes);

        lvClassesTeacher = findViewById(R.id.lvClassesTeacher);
        lvClassesTeacher.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Classroom c= classesArrayList.get(position);
                Intent intent = new Intent(TeacherChoosClasses.this,ClassActivity.class);
                intent.putExtra("c", c.getId());
                intent.putExtra("c2", c.getStudentsId());
                startActivity(intent);
            }
        });
        btnAddClass = findViewById(R.id.btnAddClass);
        btnAddClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherChoosClasses.this, TeacherCreateClasses.class);
                startActivity(intent);
            }

        });
        tvMyClasses = findViewById(R.id.tvMyClasses);

        sp=getSharedPreferences("details1",0);
        uid = sp.getString("uid","");
        role = sp.getString("role","");
        getUser();
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
                teacher = dataSnapshot.getValue(Teacher.class);

                if(teacher.getClassesArrayList()!=null) {
                    retriveData();
                    tvMyClasses.setText("הכיתות של "+ teacher.getUsername());
                }
                else tvMyClasses.setText("לא נמצאו כיתות למורה:  "+ teacher.getUsername());


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
                for (DataSnapshot data: dataSnapshot.getChildren()) {

                    Classroom c = data.getValue(Classroom.class);
                    if (teacher.getId().equals(c.getTeacherID()))
                        classesArrayList.add(c);
                }
                adapterClasses = new AdapterClasses(TeacherChoosClasses.this, 0, 0, classesArrayList);
                lvClassesTeacher.setAdapter(adapterClasses);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TAG", "תקלה בקריאת הכיתות");
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
