package com.ctom314.homework03_program1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ResultsListAdapter extends BaseAdapter
{
    Context context;
    ArrayList<Student> students;

    // Constructor
    public ResultsListAdapter(Context c, ArrayList<Student> ls)
    {
        context = c;
        students = ls;
    }


    @Override
    public int getCount() 
    {
        return students.size();
    }

    @Override
    public Object getItem(int i) 
    {
        return students.get(i);
    }

    @Override
    public long getItemId(int i) 
    {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) 
    {
        if (view == null)
        {
            // Get the LayoutInflater service from the context
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(MainActivity.LAYOUT_INFLATER_SERVICE);

            // Inflate new view
            view = mInflater.inflate(R.layout.search_results_cell, null);
        }
        
        // Find GUI elements
        TextView fName = view.findViewById(R.id.tv_v_rl_fname);
        TextView lName = view.findViewById(R.id.tv_v_rl_lname);
        TextView username = view.findViewById(R.id.tv_v_rl_username);
        TextView gpa = view.findViewById(R.id.tv_v_rl_gpa);
        TextView major = view.findViewById(R.id.tv_v_rl_major);

        // Get student object
        Student student = students.get(i);

        // Set GUI elements
        fName.setText(student.getFName());
        lName.setText(student.getLName());
        username.setText(student.getUsername());
        gpa.setText(String.valueOf(student.getGpa()));
        major.setText(student.getMajor().getName());

        return view;
    }
}
