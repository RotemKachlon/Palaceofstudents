package com.example.anew.moodle;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.anew.R;

import java.util.List;

//קוד ההגדרה של האדפטר של כיתות, משוייך ומיועד לליסטויו
public class AdapterClasses extends ArrayAdapter<Classroom> {

    Context context;
    List<Classroom> objects;

    public AdapterClasses(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<Classroom> objects) {
        super(context, resource, textViewResourceId, objects);
        this.context=context;
        this.objects=objects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.class_for_list, parent, false);//מייצר שורה של ליסטוויו

        TextView tvTitle = view.findViewById(R.id.tvClassNameClass);
        TextView tvFileNum = view.findViewById(R.id.tvNumOfFiles);
        Classroom temp = objects.get(position);
        tvTitle.setText(temp.getClassName());
        if(temp.getTasks()!=null)
        {
            tvFileNum.setText(temp.getTasks().size()+"");
        }
        else{
            tvFileNum.setText("no tasks for this class");
        }


        return view;
    }
}
