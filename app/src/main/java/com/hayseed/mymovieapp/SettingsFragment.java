package com.hayseed.mymovieapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

/**
 *
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener
{
    public interface OnSettingsChangedListener
    {
        public void OnSettingsSelected (String key);
    }

    @Override
    public void onSharedPreferenceChanged (SharedPreferences sharedPreferences, String key)
    {
        //OnSettingsChangedListener listener = (OnSettingsChangedListener) getActivity ();

        //listener.OnSettingsSelected (key);
    }

    @Override
    public void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);

        // This static call will reset default values only on the first ever read
        // Didn't make any difference
        PreferenceManager.setDefaultValues (getActivity (), R.xml.preferences, false);


        addPreferencesFromResource (R.xml.preferences);
    }

    @Override
    public void onPause ()
    {
        super.onPause ();

        getPreferenceScreen ().getSharedPreferences ().unregisterOnSharedPreferenceChangeListener (this);
    }

    @Override
    public void onResume ()
    {
        super.onResume ();

        getPreferenceScreen ().getSharedPreferences ().registerOnSharedPreferenceChangeListener (this);
    }
}
