package com.hayseed.mymovieapp;

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

        // This static call will reset default values only on the first ever read
        // Didn't make any difference
        PreferenceManager.setDefaultValues (getActivity (), R.xml.preferences, false);


        addPreferencesFromResource (R.xml.preferences);
    }
}
