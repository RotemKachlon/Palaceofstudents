package com.example.anew.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.anew.R;
import com.example.anew.moodle.Classroom;
import com.example.anew.moodle.HandelFiles;
import com.example.anew.moodle.StudentTask;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//הקוד מגדיר את הדף הכולל של משימה שנתן המורה, דרך הדף הזה ניתן לגשת להצגת הקובץ שצירף המורה ולבקר את המשימה בהתאם לרמת שביעות הרצון של המשתמש
public class StudyfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView tvFileSubTheme, tvFileSubject, tvProffession, tvFileUnderstand, tvFileDoNotUnderstand, tvAVG;
    Button btnShowFile, btnFileUnderstand, btnFileDoNotUnderstand;
    Spinner spRate;
    StudentTask studyfile;
    EditText etFileAddComment;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference fileRef, classRef;
    SharedPreferences sp;
    String uid, sfID, cid;
    Classroom c;
    int pos; //the position of the task in class task list
    ArrayAdapter<String> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studyfile);

        spRate = findViewById(R.id.spRate);
        etFileAddComment = findViewById(R.id.etFileAddComment);
        tvFileSubTheme = findViewById(R.id.tvFileSubTheme);
        tvFileSubject = findViewById(R.id.tvFileSubject);
        tvProffession = findViewById(R.id.tvProffession);
        tvFileUnderstand = findViewById(R.id.tvFileUnderstand);
        tvFileDoNotUnderstand = findViewById(R.id.tvFileDoNotUnderstand);
        tvAVG = findViewById(R.id.tvAVG);
        btnShowFile = findViewById(R.id.btnShowFile);
        btnFileUnderstand = findViewById(R.id.btnFileUnderstand);
        btnFileDoNotUnderstand = findViewById(R.id.btnFileDoNotUnderstand);

        sp = getSharedPreferences("details1", 0);
        uid = sp.getString("uid", "");

        spRate = findViewById(R.id.spRate);
        Intent intent = getIntent();
        sfID = intent.getStringExtra("studyfile");
        cid = intent.getStringExtra("cid");
        Log.d("sfid", sfID + "");
        getStudyFile();

        btnFileDoNotUnderstand.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (studyfile.getDontUnderstand() == null) {
                    ArrayList<String> doNot = new ArrayList<>();
                    doNot.add(uid);
                    studyfile.setDontUnderstand(doNot);
                } else {
                    if (!studyfile.getDontUnderstand().contains(uid))
                        studyfile.getDontUnderstand().add(uid);
                }
                if (studyfile.getUnderstand() != null && studyfile.getUnderstand().contains(uid))
                    studyfile.getUnderstand().remove(uid);
                c.getTasks().set(pos, studyfile);
                classRef.setValue(c);
            }
        });

        btnFileUnderstand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (studyfile.getUnderstand() == null) {
                    ArrayList<String> understand = new ArrayList<>();
                    understand.add(uid);
                    studyfile.setUnderstand(understand);
                } else {
                    if (!studyfile.getUnderstand().contains(uid))
                        studyfile.getUnderstand().add(uid);
                }
                if (studyfile.getDontUnderstand() != null && studyfile.getDontUnderstand().contains(uid))
                    studyfile.getDontUnderstand().remove(uid);
                c.getTasks().set(pos, studyfile);
                classRef.setValue(c);
            }
        });

        btnShowFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HandelFiles.getFile(studyfile.getFileUrl(), studyfile.getId(), StudyfileActivity.this);
            }
        });

        ArrayList<String> rates = new ArrayList<>();
        rates.add("1");
        rates.add("2");
        rates.add("3");
        rates.add("4");
        rates.add("5");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, rates);
        spRate.setAdapter(dataAdapter);
        spRate.setOnItemSelectedListener(this);
    }

    private void getStudyFile() {
        classRef = FirebaseDatabase.getInstance().getReference("Classes").child(cid);
        classRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                c = dataSnapshot.getValue(Classroom.class);
                for (int i = 0; i < c.getTasks().size(); i++) {
                    if (sfID.equals(c.getTasks().get(i).getId())) {
                        studyfile = c.getTasks().get(i);
                        pos = i;
                    }
                }
                if (studyfile.getDontUnderstand() == null) {
                    tvFileDoNotUnderstand.setText("לא הבינו: " + 0);
                } else {
                    tvFileDoNotUnderstand.setText("לא הבינו: " + studyfile.getDontUnderstand().size());
                }
                if (studyfile.getGrade() == 0)
                    tvAVG.setText("היה הראשון לדרג קובץ זה");
                else
                    tvAVG.setText("דירוג: " + studyfile.Avg());

                tvFileSubject.setText(studyfile.getSubject());
                tvFileSubTheme.setText(studyfile.getSubTheme());

                if (studyfile.getUnderstand() == null)
                    tvFileUnderstand.setText("הבינו: " + 0);
                else
                    tvFileUnderstand.setText("הבינו: " + studyfile.getUnderstand().size());

                tvProffession.setText(studyfile.getProfession());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("StudyfileActivity", "DatabaseError: " + databaseError.getMessage());
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        studyfile.setGrade(studyfile.getGrade() + i + 1);
        studyfile.setNumOfStudents(studyfile.getNumOfStudents() + 1);
        c.getTasks().set(pos, studyfile);
        classRef.setValue(c);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // no-op
    }
}