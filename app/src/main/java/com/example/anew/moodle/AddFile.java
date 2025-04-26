package com.example.anew.moodle;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.anew.R;
import com.example.anew.ui.ClassActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//הקוד מגדיר את הדף של המורה שבו הוא מוסיף לכיתה שלו משימה, בשביל להוסיף קובץ - יש קריאה לHandelFiles()
public class AddFile extends AppCompatActivity {
    EditText etProffasion,etSubject, etSubTheme;
    Button btnAddFile, btnOkAddFile;
    Uri fileeUri = null; // הקובץ עצמו
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef, classRef;
    String cid;
    ArrayList<String> studentsID;
    Classroom c;
    private ActivityResultLauncher<Intent> filePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_file);

        btnAddFile = findViewById(R.id.btnAddFile);
        btnOkAddFile = findViewById(R.id.btnOkAddFile);
        etProffasion = findViewById(R.id.etProffasion);
        etSubject = findViewById(R.id.etSubject);
        Intent intentGet = getIntent();
        cid = intentGet.getStringExtra("cid");
        studentsID = intentGet.getStringArrayListExtra("studentsID");
        etProffasion.setText(intentGet.getStringExtra("classroom"));
        getClassInfo();

        filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        fileeUri = result.getData().getData();
                        // Optional: show a Toast or Log
                        Toast.makeText(this, "File Selected!", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        etSubTheme = findViewById(R.id.etSubTheme);
        btnOkAddFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String profession, subject, subTheme;
                profession = etProffasion.getText().toString();
                subject = etSubject.getText().toString();
                subTheme = etSubTheme.getText().toString();
                if (fileeUri == null)
                    Toast.makeText(AddFile.this, "בחר קובץ", Toast.LENGTH_SHORT).show();
                else if (etProffasion.equals("") || etSubject.equals("") || etSubTheme.equals("")) {
                    Toast.makeText(AddFile.this, " נא למלא את כל הפרטים בטופס", Toast.LENGTH_SHORT).show();
                } else {
                    myRef = database.getReference("Files").push();
                    StudentTask temp = new StudentTask(myRef.getKey(),fileeUri.toString(), subTheme, subject, profession);
                    myRef.setValue(temp);
                    c.AddTask(temp);
                    classRef = FirebaseDatabase.getInstance().getReference("Classes").child(cid);
                    classRef.setValue(c);
                    HandelFiles.saveFileToStorage(fileeUri, myRef.getKey()); //שמירת הקובץ באחסון
                    Intent reintent = new Intent(AddFile.this, ClassActivity.class);
                    reintent.putExtra("c", cid);
                    reintent.putStringArrayListExtra("c2", studentsID);
                    startActivity(reintent);

                }
            }


        });
        btnAddFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("/");
                filePickerLauncher.launch(intent);
            }
        });
    }


    private void getClassInfo() {
        classRef = FirebaseDatabase.getInstance().getReference("Classes").child(cid);
        classRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                c = dataSnapshot.getValue(Classroom.class);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)//למקרה שמדובר בקובץ שנבחר מהדרייב
        {
            if (resultCode == RESULT_OK) {
                this.fileeUri = data.getData();
                //this.fileeUri.
                //Toast.makeText(this, this.fileeUri.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }
}