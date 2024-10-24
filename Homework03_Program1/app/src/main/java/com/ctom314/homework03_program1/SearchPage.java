package com.ctom314.homework03_program1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;

public class SearchPage extends AppCompatActivity 
{
    // Vars
    EditText et_j_sp_fname;
    EditText et_j_sp_lname;
    EditText et_j_sp_username;
    EditText et_j_sp_gpa;
    TextView tv_j_sp_error;

    Spinner sp_j_sp_major;
    Spinner sp_j_sp_gpaOps;

    Button btn_j_sp_search;
    Button btn_j_sp_back;

    Switch sw_j_sp_toggleMajor;

    // Page Intents
    Intent intent_j_MainActivity;
    Intent intent_j_SearchResults;

    // Spinner adapters
    ArrayAdapter<String> majorAdapter;
    ArrayAdapter<String> gpaAdapter;

    // Gpa Operators List
    ArrayList<String> gpaOps = new ArrayList<>();
    {{
        gpaOps.add("Greater than");
        gpaOps.add("Less than");
        gpaOps.add("Equal to");
        gpaOps.add("Greater/Equal to");
        gpaOps.add("Less/Equal to");
    }}

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_page);

        // Connect vars
        et_j_sp_fname = findViewById(R.id.et_v_sp_fname);
        et_j_sp_lname = findViewById(R.id.et_v_sp_lname);
        et_j_sp_username = findViewById(R.id.et_v_sp_username);
        et_j_sp_gpa = findViewById(R.id.et_v_sp_gpa);
        sp_j_sp_major = findViewById(R.id.sp_v_sp_major);
        sp_j_sp_gpaOps = findViewById(R.id.sp_v_sp_gpaOps);
        btn_j_sp_search = findViewById(R.id.btn_v_sp_search);
        btn_j_sp_back = findViewById(R.id.btn_v_sp_back);
        tv_j_sp_error = findViewById(R.id.tv_v_sp_error);
        sw_j_sp_toggleMajor = findViewById(R.id.sw_v_sp_toggleMajor);

        // Hide error message
        tv_j_sp_error.setVisibility(View.INVISIBLE);

        // Make sure the status bar is a static color
        getWindow().setStatusBarColor(Color.parseColor("#492A82"));

        // Clear all fields
        clearFields();

        // Setup adapters
        majorAdapter = new ArrayAdapter<>
                (this,
                        android.R.layout.simple_spinner_dropdown_item, MainActivity.getMajorNames());
        sp_j_sp_major.setAdapter(majorAdapter);

        gpaAdapter = new ArrayAdapter<>
                (this,
                        android.R.layout.simple_spinner_dropdown_item, gpaOps);
        sp_j_sp_gpaOps.setAdapter(gpaAdapter);

        // Setup intents
        intent_j_MainActivity = new Intent(SearchPage.this, MainActivity.class);
        intent_j_SearchResults = new Intent(SearchPage.this, SearchResults.class);

        // Setup Major state
        setMajorState(sw_j_sp_toggleMajor.isChecked());

        // Button/Event Listeners
        searchButtonListener();
        backButtonListener();
        majorSearchSwitchListener();
    }

    // Buttons
    private void searchButtonListener()
    {
        btn_j_sp_search.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // Get search query data
                String fName = et_j_sp_fname.getText().toString();
                String lName = et_j_sp_lname.getText().toString();
                String username = et_j_sp_username.getText().toString();
                String gpaOp = sp_j_sp_gpaOps.getSelectedItem().toString();
                String gpa = et_j_sp_gpa.getText().toString();
                String major = sp_j_sp_major.getSelectedItem().toString();
                boolean searchByMajor = sw_j_sp_toggleMajor.isChecked();

                // Check if all fields are empty AND not searching by major
                if (fName.isEmpty() && lName.isEmpty() && username.isEmpty() && gpa.isEmpty() && !searchByMajor)
                {
                    // Show error message
                    tv_j_sp_error.setVisibility(View.VISIBLE);
                    tv_j_sp_error.setText(MainActivity.errorMsgs.get(4));
                    Log.e("SEARCH", "All fields are empty");
                }

                // Check if GPA is not a number
                else if (!gpa.isEmpty() && !isNumber(gpa))
                {
                    // Show error message
                    tv_j_sp_error.setVisibility(View.VISIBLE);
                    tv_j_sp_error.setText(MainActivity.errorMsgs.get(5));
                    Log.e("SEARCH", "GPA is not a number");

                    // Reset all ET backgrounds, but set gpa to red
                    resetETBackgrounds(null);
                    etBackgroundRed(et_j_sp_gpa);
                }
                else
                {
                    // No errors
                    tv_j_sp_error.setVisibility(View.INVISIBLE);

                    // Reset all ET backgrounds
                    resetETBackgrounds(null);

                    // Make Query
                    Query q = new Query(fName, lName, username, gpaOp, gpa, major, searchByMajor);

                    // Pass query to SearchResults
                    intent_j_SearchResults.putExtra("query", q);

                    // Go to search results
                    startActivity(intent_j_SearchResults);
                }
            }
        });
    }

    private void backButtonListener()
    {
        btn_j_sp_back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // Log cancel
                Log.e("BACK", "Student Search Cancelled");

                // Go back to the main activity
                startActivity(intent_j_MainActivity);
            }
        });
    }

    private void majorSearchSwitchListener()
    {
        sw_j_sp_toggleMajor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean state)
            {
                setMajorState(state);
            }
        });
    }



    // ========================================================================
    //                            Helper Functions
    // ========================================================================

    private void setMajorState(boolean state)
    {
        // Disable major spinner if not searching by major
        if (!state)
        {
            // Disabled
            sp_j_sp_major.setEnabled(false);
        }
        else
        {
            // Enabled
            sp_j_sp_major.setEnabled(true);
        }
    }

    // Check if a String is a number
    private boolean isNumber(String str)
    {
        try
        {
            Double.parseDouble(str);
            return true;
        }

        // If the string is not a number, will throw error. Therefore, return false.
        catch (NumberFormatException e)
        {
            return false;
        }
    }

    // Clear all fields. Used when entering activity
    private void clearFields()
    {
        et_j_sp_fname.setText("");
        et_j_sp_lname.setText("");
        et_j_sp_username.setText("");
        et_j_sp_gpa.setText("");
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
            et_j_sp_fname.setBackgroundColor(Color.parseColor("#2A2A2A"));
            et_j_sp_lname.setBackgroundColor(Color.parseColor("#2A2A2A"));
            et_j_sp_username.setBackgroundColor(Color.parseColor("#2A2A2A"));
            et_j_sp_gpa.setBackgroundColor(Color.parseColor("#2A2A2A"));
        }
    }

    // Get which ETs are empty. Will not work on Spinner.
    private ArrayList<EditText> emptyFields()
    {
        ArrayList<EditText> emptyFields = new ArrayList<>();
        String fname = et_j_sp_fname.getText().toString();
        String lname = et_j_sp_lname.getText().toString();
        String username = et_j_sp_username.getText().toString();
        String gpa = et_j_sp_gpa.getText().toString();

        // If theyre not empty, set the background to normal
        if (fname.isEmpty())
        {
            emptyFields.add(et_j_sp_fname);
        }
        else
        {
            et_j_sp_fname.setBackgroundColor(Color.parseColor("#2A2A2A"));
        }

        if (lname.isEmpty())
        {
            emptyFields.add(et_j_sp_lname);
        }
        else
        {
            et_j_sp_lname.setBackgroundColor(Color.parseColor("#2A2A2A"));
        }

        if (username.isEmpty())
        {
            emptyFields.add(et_j_sp_username);
        }
        else
        {
            et_j_sp_username.setBackgroundColor(Color.parseColor("#2A2A2A"));
        }

        if (gpa.isEmpty())
        {
            emptyFields.add(et_j_sp_gpa);
        }
        else
        {
            et_j_sp_gpa.setBackgroundColor(Color.parseColor("#2A2A2A"));
        }

        return emptyFields;
    }
}