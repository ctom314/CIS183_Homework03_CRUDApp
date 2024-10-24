package com.ctom314.homework03_program1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class StudentDetails extends AppCompatActivity
{
    // Vars
    TextView tv_j_sd_error;
    TextView tv_j_sd_header;

    EditText et_j_sd_fname;
    EditText et_j_sd_lname;
    EditText et_j_sd_username;
    EditText et_j_sd_email;
    EditText et_j_sd_age;
    EditText et_j_sd_gpa;

    Spinner sp_j_sd_major;

    Button btn_j_sd_editStudent;
    Button btn_j_sd_backButton;
    Button btn_j_sd_save;
    Button btn_j_sd_cancel;

    // Page Intents
    Intent intent_j_MainActivity;

    // Pre-built adapter for majors
    ArrayAdapter<String> adapter;
    String majorNameDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_details);

        // Connect vars
        et_j_sd_fname = findViewById(R.id.et_v_sd_fname);
        et_j_sd_lname = findViewById(R.id.et_v_sd_lname);
        et_j_sd_username = findViewById(R.id.et_v_sd_username);
        et_j_sd_email = findViewById(R.id.et_v_sd_email);
        et_j_sd_age = findViewById(R.id.et_v_sd_age);
        et_j_sd_gpa = findViewById(R.id.et_v_sd_gpa);
        tv_j_sd_error = findViewById(R.id.tv_v_sd_error);
        sp_j_sd_major = findViewById(R.id.sp_v_sd_major);
        btn_j_sd_editStudent = findViewById(R.id.btn_v_sd_editStudent);
        btn_j_sd_backButton = findViewById(R.id.btn_v_sd_backButton);
        btn_j_sd_save = findViewById(R.id.btn_v_sd_save);
        btn_j_sd_cancel = findViewById(R.id.btn_v_sd_cancel);
        tv_j_sd_header = findViewById(R.id.tv_v_sd_header);

        // Hide error message
        tv_j_sd_error.setVisibility(View.INVISIBLE);

        // Make sure the status bar is a static color
        getWindow().setStatusBarColor(Color.parseColor("#492A82"));

        // Setup adapter
        adapter = new ArrayAdapter<>
                (this,
                        android.R.layout.simple_spinner_dropdown_item, MainActivity.getMajorNames());

        // Need the adapter enabled to set spinner value
        sp_j_sd_major.setAdapter(adapter);

        // Setup fields
        Intent cameFrom = getIntent();
        if ((cameFrom.getSerializableExtra("studentData")) != null)
        {
            Student s = (Student) cameFrom.getSerializableExtra("studentData");
            getFieldData(s);
        }

        // Disable fields
        enableFields(false);

        // Username always disabled since its the primary key
        et_j_sd_username.setEnabled(false);
        et_j_sd_username.setTextColor(Color.parseColor("#959497"));

        // Setup intents
        intent_j_MainActivity = new Intent(StudentDetails.this, MainActivity.class);

        // Button/Event Listeners
        backButtonListener();
        editButtonListener();
        saveButtonListener();
        cancelButtonListener();
    }

    // Buttons
    private void editButtonListener()
    {
        btn_j_sd_editStudent.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // Log edit
                Log.d("EDIT MODE", "Edit Mode Enabled");

                // Enable Edit Mode
                tv_j_sd_header.setText("Edit Student");
                editMode(true);
            }
        });
    }
    
    private void backButtonListener()
    {
        btn_j_sd_backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // Go back to MainActivity
                startActivity(intent_j_MainActivity);
            }
        });
    }
    
    // Edit Mode buttons
    private void saveButtonListener()
    {
        btn_j_sd_save.setOnClickListener(new View.OnClickListener() 
        {
            @Override
            public void onClick(View view) 
            {
                // Get vars
                String fname = et_j_sd_fname.getText().toString();
                String lname = et_j_sd_lname.getText().toString();
                String username = et_j_sd_username.getText().toString();
                String email = et_j_sd_email.getText().toString();
                Student.Major major = MainActivity.majorList.get(sp_j_sd_major.getSelectedItemPosition());

                // Error Handling
                // Since username cannot be changed, no error handling is needed for it
                if (!allFieldsFilled())
                {
                    // Not all fields filled out
                    tv_j_sd_error.setText(MainActivity.errorMsgs.get(0));
                    tv_j_sd_error.setVisibility(View.VISIBLE);
                    Log.e("ERROR", "Not all fields filled out");

                    // Add red outline to empty fields
                    for (EditText et : emptyFields())
                    {
                        // Change the background of each ET to a drawable
                        etBackgroundRed(et);
                    }
                }
                else if (!validEmail(et_j_sd_email.getText().toString()))
                {
                    // Invalid email
                    tv_j_sd_error.setText(MainActivity.errorMsgs.get(2));
                    tv_j_sd_error.setVisibility(View.VISIBLE);
                    Log.e("ERROR", "Invalid email");

                    // Reset all ET backgrounds, but set email to red
                    resetETBackgrounds(null);
                    etBackgroundRed(et_j_sd_email);
                }
                else
                {
                    // No errors
                    tv_j_sd_error.setVisibility(View.INVISIBLE);

                    // Get number vars when theres no errors
                    int age = Integer.parseInt(et_j_sd_age.getText().toString());
                    double gpa = Double.parseDouble(et_j_sd_gpa.getText().toString());

                    // Set background of ETs back to normal
                    resetETBackgrounds(null);

                    // Create student
                    Student student = new Student(fname, lname, username, email, age, gpa, major);

                    // Send student to MainActivity
                    intent_j_MainActivity.putExtra("updatedStudent", student);

                    // Go back to the main activity
                    startActivity(intent_j_MainActivity);
                }
            }
        });
    }

    private void cancelButtonListener()
    {
        btn_j_sd_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // Log cancel
                Log.d("EDIT MODE", "Edit Mode Disabled");

                // Reset all fields
                getFieldData((Student) getIntent().getSerializableExtra("studentData"));

                // Reset errors
                tv_j_sd_error.setVisibility(View.INVISIBLE);
                resetETBackgrounds(null);

                // Exit Edit Mode
                tv_j_sd_header.setText("Student Details");
                editMode(false);
            }
        });
    }

    // Set Field text color
    private void setFieldColors(String hex)
    {
        // Username is always disabled, so it will always be grey
        et_j_sd_fname.setTextColor(Color.parseColor(hex));
        et_j_sd_lname.setTextColor(Color.parseColor(hex));
        et_j_sd_email.setTextColor(Color.parseColor(hex));
        et_j_sd_age.setTextColor(Color.parseColor(hex));
        et_j_sd_gpa.setTextColor(Color.parseColor(hex));
    }

    private void getFieldData(Student s)
    {
        et_j_sd_fname.setText(s.getFName());
        et_j_sd_lname.setText(s.getLName());
        et_j_sd_username.setText(s.getUsername());
        et_j_sd_email.setText(s.getEmail());
        et_j_sd_age.setText(String.valueOf(s.getAge()));
        et_j_sd_gpa.setText(String.valueOf(s.getGpa()));

        // Set spinner to major
        majorNameDisplay = s.getMajor().getName();
        sp_j_sd_major.setSelection(adapter.getPosition(majorNameDisplay));
    }

    // Toggle Edit Mode
    private void editMode(boolean enable)
    {
        if (enable)
        {
            // Edit mode Enabled
            
            // Enable fields
            enableFields(true);
            
            // Swap buttons
            btn_j_sd_editStudent.setVisibility(View.INVISIBLE);
            btn_j_sd_backButton.setVisibility(View.INVISIBLE);
            btn_j_sd_save.setVisibility(View.VISIBLE);
            btn_j_sd_cancel.setVisibility(View.VISIBLE);
        }
        else
        {
            // Edit mode Disabled
            
            // Disable fields
            enableFields(false);
            
            // Swap buttons
            btn_j_sd_editStudent.setVisibility(View.VISIBLE);
            btn_j_sd_backButton.setVisibility(View.VISIBLE);
            btn_j_sd_save.setVisibility(View.INVISIBLE);
            btn_j_sd_cancel.setVisibility(View.INVISIBLE);
        }
    }
    
    // Enable/Disable ETs and Spinner
    private void enableFields(boolean enable)
    {
        et_j_sd_fname.setEnabled(enable);
        et_j_sd_lname.setEnabled(enable);
        et_j_sd_email.setEnabled(enable);
        et_j_sd_age.setEnabled(enable);
        et_j_sd_gpa.setEnabled(enable);

        // Spinner
        sp_j_sd_major.setEnabled(enable);
        sp_j_sd_major.setClickable(enable);

        // Set field colors based on state
        if (enable)
        {
            // Enabled
            setFieldColors("#D2D2D2");
        }
        else
        {
            // Disabled
            setFieldColors("#959497");
        }
    }

    // ========================================================================
    //                            Helper Functions
    // ========================================================================
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
        String fname = et_j_sd_fname.getText().toString();
        String lname = et_j_sd_lname.getText().toString();
        String username = et_j_sd_username.getText().toString();
        String email = et_j_sd_email.getText().toString();
        String age = et_j_sd_age.getText().toString();
        String gpa = et_j_sd_gpa.getText().toString();

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
            et_j_sd_fname.setBackgroundColor(Color.parseColor("#2A2A2A"));
            et_j_sd_lname.setBackgroundColor(Color.parseColor("#2A2A2A"));
            et_j_sd_username.setBackgroundColor(Color.parseColor("#2A2A2A"));
            et_j_sd_email.setBackgroundColor(Color.parseColor("#2A2A2A"));
            et_j_sd_age.setBackgroundColor(Color.parseColor("#2A2A2A"));
            et_j_sd_gpa.setBackgroundColor(Color.parseColor("#2A2A2A"));
        }
    }

    // Get which ETs are empty. Will not work on Spinner.
    private ArrayList<EditText> emptyFields()
    {
        ArrayList<EditText> emptyFields = new ArrayList<>();
        String fname = et_j_sd_fname.getText().toString();
        String lname = et_j_sd_lname.getText().toString();
        String username = et_j_sd_username.getText().toString();
        String email = et_j_sd_email.getText().toString();
        String age = et_j_sd_age.getText().toString();
        String gpa = et_j_sd_gpa.getText().toString();

        // If theyre not empty, set the background to normal
        if (fname.isEmpty())
        {
            emptyFields.add(et_j_sd_fname);
        }
        else
        {
            et_j_sd_fname.setBackgroundColor(Color.parseColor("#2A2A2A"));
        }

        if (lname.isEmpty())
        {
            emptyFields.add(et_j_sd_lname);
        }
        else
        {
            et_j_sd_lname.setBackgroundColor(Color.parseColor("#2A2A2A"));
        }

        if (username.isEmpty())
        {
            emptyFields.add(et_j_sd_username);
        }
        else
        {
            et_j_sd_username.setBackgroundColor(Color.parseColor("#2A2A2A"));
        }

        if (email.isEmpty())
        {
            emptyFields.add(et_j_sd_email);
        }
        else
        {
            et_j_sd_email.setBackgroundColor(Color.parseColor("#2A2A2A"));
        }

        if (age.isEmpty())
        {
            emptyFields.add(et_j_sd_age);
        }
        else
        {
            et_j_sd_age.setBackgroundColor(Color.parseColor("#2A2A2A"));
        }

        if (gpa.isEmpty())
        {
            emptyFields.add(et_j_sd_gpa);
        }
        else
        {
            et_j_sd_gpa.setBackgroundColor(Color.parseColor("#2A2A2A"));
        }

        return emptyFields;
    }

}