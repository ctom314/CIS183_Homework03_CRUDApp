package com.ctom314.homework03_program1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    // Vars
    Button btn_j_addStudent;
    Button btn_j_searchStudents;

    ListView lv_j_studentsList;

    // Page Intents
    Intent intent_j_AddStudent;
    Intent intent_j_SearchStudents;

    // Student List
    static ArrayList<Student> studentList = new ArrayList<>();

    StudentsListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Connect vars
        btn_j_addStudent = findViewById(R.id.btn_v_addStudent);
        btn_j_searchStudents = findViewById(R.id.btn_v_searchStudents);
        lv_j_studentsList = findViewById(R.id.lv_v_studentsList);

        // Make sure the status bar is a static color
        getWindow().setStatusBarColor(Color.parseColor("#492A82"));

        // Setup intents
        intent_j_AddStudent = new Intent(MainActivity.this, AddStudent.class);
        // TODO: Make SearchStudents Activity. Uncomment the following line when done
        // intent_j_SearchStudents = new Intent(MainActivity.this, SearchStudents.class);

        // Button Listeners
        addStudentButtonListener();

        fillListView();
    }

    // Buttons
    private void addStudentButtonListener()
    {
        btn_j_addStudent.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // Go to AddStudent
                startActivity(intent_j_AddStudent);
            }
        });
    }

    // TODO: Make searchStudentsButtonListener

    private void fillListView()
    {
        // Create new adapter
        adapter = new StudentsListAdapter(this, studentList);

        // Set the adapter
        lv_j_studentsList.setAdapter(adapter);
    }
}