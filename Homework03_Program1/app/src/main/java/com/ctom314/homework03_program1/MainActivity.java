package com.ctom314.homework03_program1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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
    Intent intent_j_StudentDetails;
    Intent intent_j_SearchStudents;

    // Student List
    static ArrayList<Student> studentList = new ArrayList<>();

    // TODO: Replace ArrayList with database
    // Until databases are added, store Majors here
    static ArrayList<Student.Major> majorList = new ArrayList<>();

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

        // If no majors exist, add default majors
        if (majorList.isEmpty())
        {
            majorList.add(new Student.Major(1, "Computer Science", "CIS"));
            majorList.add(new Student.Major(2, "Accounting", "BUS"));
            majorList.add(new Student.Major(3, "Communications", "COM"));
            majorList.add(new Student.Major(4, "Education", "EDU"));
        }

        // Setup intents
        intent_j_AddStudent = new Intent(MainActivity.this, AddStudent.class);
        intent_j_StudentDetails = new Intent(MainActivity.this, StudentDetails.class);
        // TODO: Make SearchStudents Activity. Uncomment the following line when done
        // intent_j_SearchStudents = new Intent(MainActivity.this, SearchStudents.class);

        // If student was updated, update student in list
        Intent cameFrom = getIntent();
        if ((cameFrom.getSerializableExtra("updatedStudent")) != null)
        {
            Student s = (Student) cameFrom.getSerializableExtra("updatedStudent");
            updateStudent(s);

            // Show user that student was updated
            Toast.makeText(MainActivity.this, "Student updated successfully",
                    Toast.LENGTH_SHORT).show();
        }

        // Button/Event Listeners
        addStudentButtonListener();
        studentsListClickEvent();

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

    // Event Listeners
    private void studentsListClickEvent()
    {
        // Short click opens student details page
        lv_j_studentsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                // TODO: Make StudentDetails Activity, finish this when done

                // Get student and pass to StudentDetails
                Student selectedStudent = getSelectedStudent(i);
                intent_j_StudentDetails.putExtra("studentData", selectedStudent);

                // Go to StudentDetails
                startActivity(intent_j_StudentDetails);
            }
        });

        // Long click deletes student
        lv_j_studentsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                // Get student that was clicked
                Student student = studentList.get(i);

                // TODO: Delete student from database
                // Delete student from list
                studentList.remove(i);

                // Show user that student was deleted
                Toast.makeText(MainActivity.this,
                        student.getFName() + " " + student.getLName() + " deleted successfully",
                        Toast.LENGTH_SHORT).show();

                // Log deletion
                Log.d("DELETION", "Student '" + student.getFName() + " " + student.getLName() + "' deleted");

                fillListView();
                return true;
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

    public static ArrayList<String> getMajorNames()
    {
        ArrayList<String> majorNames = new ArrayList<>();

        for (Student.Major major : majorList)
        {
            majorNames.add(major.getName());
        }

        return majorNames;
    }

    private Student getSelectedStudent(int position)
    {
        return studentList.get(position);
    }

    public void updateStudent(Student s)
    {
        // Find student in list
        for (int i = 0; i < studentList.size(); i++)
        {
            Student student = studentList.get(i);

            // If student is found, update
            if (student.getUsername().equals(s.getUsername()))
            {
                studentList.set(i, s);
                break;
            }
        }

        fillListView();
    }
}