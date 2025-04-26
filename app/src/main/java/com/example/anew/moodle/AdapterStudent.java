package com.example.anew.moodle;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.anew.R;

import java.util.List;

//קוד ההגדרה של האדפטר של תלמידים, משוייך ומיועד לליסטויו
public class AdapterStudent extends ArrayAdapter<Student> {
    Context context;
    List<Student> objects;


    public AdapterStudent(Context context, int resource, int textViewResourceId, List<Student> objects) {
        super(context, resource, textViewResourceId, objects);

        this.context=context;
        this.objects=objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.student_teachers, parent, false);// יוצר שורה בליסטוויו

        TextView tvNameStudentTeacher = view.findViewById(R.id.tvNameStudentTeacher);
        Student temp = objects.get(position);
        tvNameStudentTeacher.setText(temp.getUsername());
        TextView tvMailStudentTeacher = view.findViewById(R.id.tvMailStudentTeacher);
        tvMailStudentTeacher.setText(temp.getEmail());

        return view;
    }

}
