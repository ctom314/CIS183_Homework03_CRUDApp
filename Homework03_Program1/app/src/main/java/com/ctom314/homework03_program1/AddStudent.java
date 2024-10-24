package com.ctom314.homework03_program1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;

public class AddStudent extends AppCompatActivity
{
    // Vars
    TextView tv_j_as_newMajor;

    EditText et_j_as_fname;
    EditText et_j_as_lname;
    EditText et_j_as_username;
    EditText et_j_as_email;
    EditText et_j_as_age;
    EditText et_j_as_gpa;
    TextView tv_j_as_error;

    Spinner sp_j_as_major;

    Button btn_j_as_addStudent;
    Button btn_j_as_cancel;

    // Page Intents
    Intent intent_j_MainActivity;
    Intent intent_j_AddMajor;

    // Pre-built adapter for majors
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_student);

        // Connect vars
        tv_j_as_newMajor = findViewById(R.id.tv_v_as_addMajorText);
        et_j_as_fname = findViewById(R.id.et_v_as_fname);
        et_j_as_lname = findViewById(R.id.et_v_as_lname);
        et_j_as_username = findViewById(R.id.et_v_as_username);
        et_j_as_email = findViewById(R.id.et_v_as_email);
        et_j_as_age = findViewById(R.id.et_v_as_age);
        et_j_as_gpa = findViewById(R.id.et_v_as_gpa);
        sp_j_as_major = findViewById(R.id.sp_v_as_major);
        btn_j_as_addStudent = findViewById(R.id.btn_v_as_addStudent);
        btn_j_as_cancel = findViewById(R.id.btn_v_as_cancel);
        tv_j_as_error = findViewById(R.id.tv_v_as_error);

        // Hide error message
        tv_j_as_error.setVisibility(View.INVISIBLE);

        // Make sure the status bar is a static color
        getWindow().setStatusBarColor(Color.parseColor("#492A82"));

        // Clear all fields
        clearFields();

        // Setup adapter
        adapter = new ArrayAdapter<>
                (this,
                        android.R.layout.simple_spinner_dropdown_item, MainActivity.getMajorNames());
        sp_j_as_major.setAdapter(adapter);

        // Setup intents
        intent_j_MainActivity = new Intent(AddStudent.this, MainActivity.class);
        intent_j_AddMajor = new Intent(AddStudent.this, AddMajor.class);

        // Button/Event Listeners
        addStudentButtonListener();
        cancelButtonListener();
        addMajorEventListener();

    }

    // Buttons
    private void addStudentButtonListener()
    {
        btn_j_as_addStudent.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // Get username for error checking
                String username = et_j_as_username.getText().toString();

                // Error Handling
                if (!allFieldsFilled())
                {
                    // Not all fields filled out
                    tv_j_as_error.setText(MainActivity.errorMsgs.get(0));
                    tv_j_as_error.setVisibility(View.VISIBLE);
                    Log.e("ERROR", "Not all fields filled out");

                    // Add red outline to empty fields
                    for (EditText et : emptyFields())
                    {
                        // Change the background of each ET to a drawable
                        etBackgroundRed(et);
                    }
                }

                else if (usernameExists(username))
                {
                    // Username already exists
                    tv_j_as_error.setText(MainActivity.errorMsgs.get(1));
                    tv_j_as_error.setVisibility(View.VISIBLE);
                    Log.e("ERROR", "Username '" + username + "' already exists");

                    // Reset all ET backgrounds, but set username to red
                    resetETBackgrounds(null);
                    etBackgroundRed(et_j_as_username);
                }

                else if (!validEmail(et_j_as_email.getText().toString()))
                {
                    // Invalid email
                    tv_j_as_error.setText(MainActivity.errorMsgs.get(2));
                    tv_j_as_error.setVisibility(View.VISIBLE);
                    Log.e("ERROR", "Invalid email");

                    // Reset all ET backgrounds, but set email to red
                    resetETBackgrounds(null);
                    etBackgroundRed(et_j_as_email);
                }

                else
                {
                    // No errors
                    tv_j_as_error.setVisibility(View.INVISIBLE);

                    // Get rest of values from ETs and make Student
                    String fname = et_j_as_fname.getText().toString();
                    String lname = et_j_as_lname.getText().toString();
                    String email = et_j_as_email.getText().toString();
                    int age = Integer.parseInt(et_j_as_age.getText().toString());
                    double gpa = Double.parseDouble(et_j_as_gpa.getText().toString());
                    Student.Major major = MainActivity.majorList.get(sp_j_as_major.getSelectedItemPosition());

                    // Set background of ETs back to normal
                    resetETBackgrounds(null);

                    // Create student
                    Student student = new Student(fname, lname, username, email, age, gpa, major);

                    // Add student to database
                    MainActivity.dbHelper.addStudent(student);

                    // Update student list
                    MainActivity.updateStudentList();

                    // Log student
                    Log.d("STUDENT ADDED", student.getFName() + " " + student.getLName());

                    // Show user that student was created
                    Toast.makeText(AddStudent.this,
                            student.getFName() + " " + student.getLName() + " created successfully",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void cancelButtonListener()
    {
        btn_j_as_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // Go back to the main activity
                startActivity(intent_j_MainActivity);
            }
        });
    }

    // Event Listeners
    private void addMajorEventListener()
    {
        tv_j_as_newMajor.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // Go to AddMajor
                startActivity(intent_j_AddMajor);
            }
        });
    }



    // ========================================================================
    //                            Helper Functions
    // ========================================================================

    // Clear all fields. Used when entering activity
    private void clearFields()
    {
        et_j_as_fname.setText("");
        et_j_as_lname.setText("");
        et_j_as_username.setText("");
        et_j_as_email.setText("");
        et_j_as_age.setText("");
        et_j_as_gpa.setText("");
    }

    // Check if username already exists
    private boolean usernameExists(String u)
    {
        for (Student s : MainActivity.studentList)
        {
            if (s.getUsername().equals(u))
            {
                return true;
            }
        }

        return false;
    }

    private boolean validEmail(String e)
    {
        // Look for the @ and . in the email
        if (e.contains("@") && e.contains("."))
        {
            return true;
        }

        return false;
    }

    // Check if all fields are filled out
    private boolean allFieldsFilled()
    {
        String fname = et_j_as_fname.getText().toString();
        String lname = et_j_as_lname.getText().toString();
        String username = et_j_as_username.getText().toString();
        String email = et_j_as_email.getText().toString();
        String age = et_j_as_age.getText().toString();
        String gpa = et_j_as_gpa.getText().toString();

        if (fname.isEmpty() ||
            lname.isEmpty() ||
            username.isEmpty() ||
            email.isEmpty() ||
            age.isEmpty() ||
            gpa.isEmpty())
        {
            return false;
        }

        return true;
    }

    // Change the background of an ET to a red outline drawable
    private void etBackgroundRed(EditText et)
    {
        et.setBackground(ResourcesCompat.getDrawable(
                getResources(), R.drawable.et_redoutline, null));
    }

    private void resetETBackgrounds(EditText et)
    {
        // Reset all ET backgrounds if no ET is passed
        if (et != null)
        {
            et.setBackgroundColor(Color.parseColor("#2A2A2A"));
        }

        else
        {
            et_j_as_fname.setBackgroundColor(Color.parseColor("#2A2A2A"));
            et_j_as_lname.setBackgroundColor(Color.parseColor("#2A2A2A"));
            et_j_as_username.setBackgroundColor(Color.parseColor("#2A2A2A"));
            et_j_as_email.setBackgroundColor(Color.parseColor("#2A2A2A"));
            et_j_as_age.setBackgroundColor(Color.parseColor("#2A2A2A"));
            et_j_as_gpa.setBackgroundColor(Color.parseColor("#2A2A2A"));
        }
    }

    // Get which ETs are empty. Will not work on Spinner.
    private ArrayList<EditText> emptyFields()
    {
        ArrayList<EditText> emptyFields = new ArrayList<>();
        String fname = et_j_as_fname.getText().toString();
        String lname = et_j_as_lname.getText().toString();
        String username = et_j_as_username.getText().toString();
        String email = et_j_as_email.getText().toString();
        String age = et_j_as_age.getText().toString();
        String gpa = et_j_as_gpa.getText().toString();

        // If theyre not empty, set the background to normal
        if (fname.isEmpty())
        {
            emptyFields.add(et_j_as_fname);
        }
        else
        {
            et_j_as_fname.setBackgroundColor(Color.parseColor("#2A2A2A"));
        }

        if (lname.isEmpty())
        {
            emptyFields.add(et_j_as_lname);
        }
        else
        {
            et_j_as_lname.setBackgroundColor(Color.parseColor("#2A2A2A"));
        }

        if (username.isEmpty())
        {
            emptyFields.add(et_j_as_username);
        }
        else
        {
            et_j_as_username.setBackgroundColor(Color.parseColor("#2A2A2A"));
        }

        if (email.isEmpty())
        {
            emptyFields.add(et_j_as_email);
        }
        else
        {
            et_j_as_email.setBackgroundColor(Color.parseColor("#2A2A2A"));
        }

        if (age.isEmpty())
        {
            emptyFields.add(et_j_as_age);
        }
        else
        {
            et_j_as_age.setBackgroundColor(Color.parseColor("#2A2A2A"));
        }

        if (gpa.isEmpty())
        {
            emptyFields.add(et_j_as_gpa);
        }
        else
        {
            et_j_as_gpa.setBackgroundColor(Color.parseColor("#2A2A2A"));
        }

        return emptyFields;
    }
}