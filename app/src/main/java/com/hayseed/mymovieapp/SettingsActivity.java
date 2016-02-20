package com.hayseed.mymovieapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

/**
 *
 */
public class SettingsActivity extends AppCompatActivity
{

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);

        getFragmentManager ().beginTransaction ()
                .replace (android.R.id.content, new SettingsFragment ())
                .commit ();
    }
}
