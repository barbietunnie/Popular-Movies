package com.xuxperience.popularmovies.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.xuxperience.popularmovies.R;

public class BaseActivity extends AppCompatActivity {
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_settings) {
            openSettingsScreen();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Display the Settings screen
     */
    private void openSettingsScreen() {
        Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
        startActivity(startSettingsActivity);
    }

    public SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }
}
