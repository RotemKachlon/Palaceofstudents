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

//קוד ההגדרה של האדפטר של משימות, משוייך ומיועד לליסטויו
public class AdapterFile extends ArrayAdapter<StudentTask> {

    Context context;
    List<StudentTask> objects;


    public AdapterFile(Context context, int resource, int textViewResourceId, List<StudentTask> objects) {
        super(context, resource, textViewResourceId, objects);

        this.context=context;
        this.objects=objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.file_for_list, parent, false);// מייצר שורה של ליסטוויו

        TextView tvFileSubjectList = view.findViewById(R.id.tvFileSubjectList);
        TextView tvFileSubjectThemeList = view.findViewById(R.id.tvFileSubjectThemeList);
        TextView tvFileRateList = view.findViewById(R.id.tvFileRateList);
        StudentTask temp = objects.get(position);
        tvFileSubjectList.setText(temp.getSubject());
        tvFileSubjectThemeList.setText(temp.getSubTheme());
        tvFileRateList.setText("");

        return view;
    }
}
