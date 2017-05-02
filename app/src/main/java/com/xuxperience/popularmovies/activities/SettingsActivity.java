package com.xuxperience.popularmovies.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.xuxperience.popularmovies.fragments.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Display the fragment as the main content
        if(getFragmentManager().findFragmentById(android.R.id.content) == null) {
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new SettingsFragment())
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Go to the previous activity when the home icon is pressed
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
