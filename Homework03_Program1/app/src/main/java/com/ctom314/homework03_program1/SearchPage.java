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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

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

        // Hide error message
        tv_j_sp_error.setVisibility(View.INVISIBLE);

        // Make sure the status bar is a static color
        getWindow().setStatusBarColor(Color.parseColor("#492A82"));

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

        // Button/Event Listeners
        searchButtonListener();
        backButtonListener();
        
    }

    // Buttons
    private void searchButtonListener()
    {
        btn_j_sp_search.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // TODO: Search for students

                // Go to search results
                startActivity(intent_j_SearchResults);
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
}