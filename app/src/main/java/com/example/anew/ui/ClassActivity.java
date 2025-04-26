package com.example.anew.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.anew.R;
import com.example.anew.moodle.AdapterFile;
import com.example.anew.moodle.AdapterStudent;
import com.example.anew.moodle.AddFile;
import com.example.anew.moodle.Classroom;
import com.example.anew.moodle.Student;
import com.example.anew.moodle.StudentTask;
import com.example.anew.moodle.StudentTask;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//קוד זה מגדיר פעילות להצגת מידע כיתתי, כולל תלמידים ומשימות/קבצים, ומאפשר למשתמשים ליצור אינטראקציה עם הנתונים על ידי הוספת/הצגת קבצים
public class ClassActivity extends AppCompatActivity {
    Button btnChangeStudentsToFiles, btnAddTask;
    TextView tvClassNameClass,tvLayerClass;
    ListView lvFilesStudentClass;
    String cid, uid, role;
    Boolean flag;
    Classroom c;
    ArrayList<String> filesID, studentsID;
    ArrayList<StudentTask> studyfiles;
    ArrayList<Student> students;
    AdapterFile adapterFile;
    AdapterStudent adapterStudent;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef, classRef, fileRef, studentRef;
    ProgressDialog progressDialog;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);
        sp=getSharedPreferences("details1",0);
        uid = sp.getString("uid", "");
        role = sp.getString("role","");
        flag = true;
        tvClassNameClass = findViewById(R.id.tvClassNameClass);
        tvLayerClass = findViewById(R.id.tvLayerClass);
        lvFilesStudentClass = findViewById(R.id.lvFilesStudentClass);
        btnAddTask = findViewById(R.id.btnAddTask);
        if(role.equals("Teacher")){
            btnAddTask.setVisibility(View.VISIBLE);
        }
        btnAddTask.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(ClassActivity.this, AddFile.class);
                intent.putExtra("cid", cid);
                intent.putExtra("classroom", c.getClassName());
                startActivity(intent);
            }
        });
        btnChangeStudentsToFiles = findViewById(R.id.btnChangeStudentsToFiles);
        btnChangeStudentsToFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag == true){
                    btnChangeStudentsToFiles.setText("החלף לרשימת הקבצים");
                    flag = false;
                    getStudentsList();

                }
                else{
                    btnChangeStudentsToFiles.setText("החלף לרשימת התלמידים");
                    flag = true;
                    getTasksList();
                }
            }
        });
        final Intent intent = getIntent();
        cid = intent.getStringExtra("c");
        studentsID = intent.getStringArrayListExtra("c2");
        getClassroom();


        lvFilesStudentClass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(flag == true) {
                    StudentTask studyfile = c.getTasks().get(i);
                    if(!studyfile.getSubject().equals("לא נוספו משימות")){
                        Intent intent1 = new Intent(ClassActivity.this, StudyfileActivity.class);
                        intent1.putExtra("studyfile", studyfile.getId());
                        intent1.putExtra("cid", cid);
                        intent1.putStringArrayListExtra("studentsID", studentsID);
                        startActivity(intent1);
                    }
                }
            }
        });
    }

    public void getStudentsList(){
        studentRef = FirebaseDatabase.getInstance().getReference("Persons");
        studentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                students= new ArrayList<>();
                if(studentsID == null){
                    Student g = new Student("id", "לא התווספו תלמידים", "", "password", "phone", "Student", "idschool", false, "layer");
                    students.add(g);
                }
                else{
                    for(DataSnapshot data: dataSnapshot.getChildren()) {
                        Student s = data.getValue(Student.class);
                        if(studentsID.contains(s.getId())) {
                            students.add(s);
                        }
                    }
                }
                adapterStudent = new AdapterStudent(ClassActivity.this, 0, 0, students);
                lvFilesStudentClass.setAdapter(adapterStudent);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    public void getTasksList(){
        classRef = FirebaseDatabase.getInstance().getReference("Classes").child(cid);
        classRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                c = snapshot.getValue(Classroom.class);
                if(c.getTasks()==null) {
                    StudentTask t = new StudentTask("id", "fileUrl", "", "לא נוספו משימות", "profession");
                    c.AddTask(t);
                }
                adapterFile = new AdapterFile(ClassActivity.this, 0, 0, c.getTasks());
                lvFilesStudentClass.setAdapter(adapterFile);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void getClassroom() {
        classRef = FirebaseDatabase.getInstance().getReference("Classes").child(cid);
        classRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                c = snapshot.getValue(Classroom.class);
                tvClassNameClass.setText(c.getClassName());
                if(c.getStudentsId()!=null) {
                    tvLayerClass.setText("מספר התלמידים בכיתה זאת: " + c.getStudentsId().size());
                    getStudent();
                }
                else{
                    tvLayerClass.setText("מספר התלמידים בכיתה זאת: 0");
                }
                getTasksList();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }



    public void getStudent() {
        students = new ArrayList<>();
        studentRef = FirebaseDatabase.getInstance().getReference("Classes").child(cid);
        studentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()) {
                    Student s = dataSnapshot.getValue(Student.class);
                    if(c.getStudentsId().contains(s.getId())) {
                        students.add(s);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}


