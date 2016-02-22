package com.hayseed.mymovieapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

/**
 *
 */
public class SettingsFragment extends PreferenceFragment
{
    @Override
    public void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);

        PreferenceManager.setDefaultValues (getActivity (), R.xml.preferences, false);

        addPreferencesFromResource (R.xml.preferences);
    }
}
