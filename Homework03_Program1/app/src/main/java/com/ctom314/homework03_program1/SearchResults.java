package com.ctom314.homework03_program1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class SearchResults extends AppCompatActivity
{
    // Vars
    TextView tv_j_sr_numResults;

    Button btn_j_sr_search;
    Button btn_j_sr_home;

    ListView lv_j_sr_studentsList;

    // Page Intents
    Intent intent_j_sr_MainActivity;
    Intent intent_j_sr_StudentSearch;
    Intent intent_j_StudentDetails;

    // Search Results
    ArrayList<Student> searchResults;
    ResultsListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_results);

        // Connect vars
        btn_j_sr_search = findViewById(R.id.btn_v_sr_search);
        btn_j_sr_home = findViewById(R.id.btn_v_sr_home);
        lv_j_sr_studentsList = findViewById(R.id.lv_v_sr_studentsList);
        tv_j_sr_numResults = findViewById(R.id.tv_v_sr_numResults);

        // Make sure the status bar is a static color
        getWindow().setStatusBarColor(Color.parseColor("#492A82"));

        // Setup intents
        intent_j_sr_MainActivity = new Intent(SearchResults.this, MainActivity.class);
        intent_j_sr_StudentSearch = new Intent(SearchResults.this, SearchPage.class);
        intent_j_StudentDetails = new Intent(SearchResults.this, StudentDetails.class);

        // Get query from search page
        Intent cameFrom = getIntent();
        if ((cameFrom.getSerializableExtra("query")) != null)
        {
            Query q = (Query) cameFrom.getSerializableExtra("query");

            // Search for students in database
            searchResults = MainActivity.dbHelper.studentSearch(q);

            // Calculate number of results
            calculateNumResults();

            // Fill list view
            fillListView();
        }

        // Button/Event Listeners
        searchButtonListener();
        homeButtonListener();
        resultsListClickEvent();
    }

    // Buttons
    private void searchButtonListener()
    {
        btn_j_sr_search.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // Go to search page
                startActivity(intent_j_sr_StudentSearch);
            }
        });
    }

    private void homeButtonListener()
    {
        btn_j_sr_home.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // Go to main activity
                startActivity(intent_j_sr_MainActivity);
            }
        });
    }

    private void resultsListClickEvent()
    {
        // Short click opens student details page
        lv_j_sr_studentsList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                // Get student and pass to StudentDetails
                Student selectedStudent = MainActivity.getSelectedStudent(i);
                intent_j_StudentDetails.putExtra("studentData", selectedStudent);

                // Go to StudentDetails
                startActivity(intent_j_StudentDetails);
            }
        });
    }

    private void calculateNumResults()
    {
        int numResults = searchResults.size();

        // If no results, change text color to red
        if (numResults == 0)
        {
            tv_j_sr_numResults.setTextColor(Color.parseColor("#FF5050"));

            // Set text
            tv_j_sr_numResults.setText(numResults + " Results Found");
        }

        // If only one result, use singular form
        else if (numResults == 1)
        {
            tv_j_sr_numResults.setTextColor(Color.WHITE);

            // Set text
            tv_j_sr_numResults.setText(numResults + " Result Found");
        }

        else
        {
            tv_j_sr_numResults.setTextColor(Color.WHITE);

            // Set text
            tv_j_sr_numResults.setText(numResults + " Results Found");
        }
    }

    private void fillListView()
    {
        // Create adapter
        adapter = new ResultsListAdapter(this, searchResults);

        // Set adapter
        lv_j_sr_studentsList.setAdapter(adapter);
    }
}