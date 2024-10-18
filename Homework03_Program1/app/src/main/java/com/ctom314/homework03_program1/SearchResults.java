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

public class SearchResults extends AppCompatActivity
{
    // Vars
    Button btn_j_sr_search;
    Button btn_j_sr_home;

    ListView lv_j_sr_studentsList;

    // Page Intents
    Intent intent_j_sr_MainActivity;
    Intent intent_j_sr_StudentSearch;

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

        // Make sure the status bar is a static color
        getWindow().setStatusBarColor(Color.parseColor("#492A82"));

        // Setup intents
        intent_j_sr_MainActivity = new Intent(SearchResults.this, MainActivity.class);
        intent_j_sr_StudentSearch = new Intent(SearchResults.this, SearchPage.class);

        // Button/Event Listeners
        searchButtonListener();
        homeButtonListener();

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
}