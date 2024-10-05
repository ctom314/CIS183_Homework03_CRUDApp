package com.ctom314.homework03_program1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class AddMajor extends AppCompatActivity
{
    // Vars
    TextView tv_j_am_error;

    EditText et_j_am_majorName;
    EditText et_j_am_majorPrefix;

    Button btn_j_am_addMajor;
    Button btn_j_am_cancel;

    // Page Intents
    Intent intent_j_AddStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_major);

        // Connect vars
        tv_j_am_error = findViewById(R.id.tv_v_am_error);
        et_j_am_majorName = findViewById(R.id.et_v_am_majorName);
        et_j_am_majorPrefix = findViewById(R.id.et_v_am_majorPrefix);
        btn_j_am_addMajor = findViewById(R.id.btn_v_am_addMajor);
        btn_j_am_cancel = findViewById(R.id.btn_v_am_cancel);

        // Hide error message
        tv_j_am_error.setVisibility(TextView.INVISIBLE);

        // Make sure the status bar is a static color
        getWindow().setStatusBarColor(Color.parseColor("#492A82"));

        // Setup intents
        intent_j_AddStudent = new Intent(AddMajor.this, AddStudent.class);

        // Button Listeners
        addMajorButtonListener();
        cancelMajorButtonListener();

    }

    private void addMajorButtonListener()
    {
        btn_j_am_addMajor.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // TODO: Add Major to database

                // Get vars
                String majorName = et_j_am_majorName.getText().toString();
                String majorPrefix = et_j_am_majorPrefix.getText().toString();

                // Error handling
                if (!allFieldsFilled())
                {
                    // Not all fields are filled
                    tv_j_am_error.setText(AddStudent.errorMsgs.get(0));
                    tv_j_am_error.setVisibility(TextView.VISIBLE);
                    Log.e("ERROR", "Not all fields filled out");

                    // Add red out to empty fields
                    for (EditText et : emptyFields())
                    {
                        etBackgroundRed(et);
                    }
                }

                else if (majorExists(majorName))
                {
                    // Major already exists
                    tv_j_am_error.setText(AddStudent.errorMsgs.get(3));
                    tv_j_am_error.setVisibility(View.VISIBLE);
                    Log.e("ERROR", "Major '" + majorName + "' already exists");

                    // Reset all ET backgrounds, but set major name to red
                    resetETBackgrounds(null);
                    etBackgroundRed(et_j_am_majorName);
                }

                else
                {
                    // No errors
                    tv_j_am_error.setVisibility(TextView.INVISIBLE);

                    // Set the background of all ETs to normal
                    resetETBackgrounds(null);

                    // Get id for new major
                    int majorId = MainActivity.majorList.size() + 1;

                    // Create major
                    Student.Major major = new Student.Major(majorId, majorName, majorPrefix);
                    MainActivity.majorList.add(major);

                    // Log major
                    Log.i("MAJOR", "Major '" + majorName + "' added");

                    // Go back to Add Student
                    startActivity(intent_j_AddStudent);
                }


            }
        });
    }

    private void cancelMajorButtonListener()
    {
        btn_j_am_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // Log cancel
                Log.e("CANCEL", "Major Add Cancelled");

                // Go back to the main activity
                startActivity(intent_j_AddStudent);
            }
        });
    }

    // ========================================================================
    //                            Helper Functions
    // ========================================================================

    private boolean majorExists(String majorName)
    {
        // TODO: When the database is implemented, check if the major exists inside the database
        // TODO: instead of checking if the majorId is in the list of majors

        // Get lower case version of majorName
        majorName = majorName.toLowerCase();

        for (Student.Major m : MainActivity.majorList)
        {
            String mName = m.getName();

            // Compare, ignore case
            if (mName.equalsIgnoreCase(majorName))
            {
                return true;
            }
        }

        return false;
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
            et_j_am_majorName.setBackgroundColor(Color.parseColor("#2A2A2A"));
            et_j_am_majorPrefix.setBackgroundColor(Color.parseColor("#2A2A2A"));
        }
    }

    // Check if all fields are filled out
    private boolean allFieldsFilled()
    {
        String majorName = et_j_am_majorName.getText().toString();
        String majorPrefix = et_j_am_majorPrefix.getText().toString();

        if (majorName.isEmpty() || majorPrefix.isEmpty())
        {
            return false;
        }

        return true;
    }

    // Get which ETs are empty. Will not work on Spinner.
    private ArrayList<EditText> emptyFields()
    {
        ArrayList<EditText> emptyFields = new ArrayList<>();
        String majorName = et_j_am_majorName.getText().toString();
        String majorPrefix = et_j_am_majorPrefix.getText().toString();

        // If theyre not empty, set the background to normal
        if (majorName.isEmpty())
        {
            emptyFields.add(et_j_am_majorName);
        }
        else
        {
            et_j_am_majorName.setBackgroundColor(Color.parseColor("#2A2A2A"));
        }

        if (majorPrefix.isEmpty())
        {
            emptyFields.add(et_j_am_majorPrefix);
        }
        else
        {
            et_j_am_majorPrefix.setBackgroundColor(Color.parseColor("#2A2A2A"));
        }

        return emptyFields;
    }
}