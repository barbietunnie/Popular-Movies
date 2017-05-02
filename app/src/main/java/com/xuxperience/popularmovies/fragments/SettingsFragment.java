package com.xuxperience.popularmovies.fragments;


import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.xuxperience.popularmovies.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML file
        addPreferencesFromResource(R.xml.preferences);
    }
}
