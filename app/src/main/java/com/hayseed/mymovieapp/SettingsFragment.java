package com.hayseed.mymovieapp;

import android.os.Bundle;
import android.preference.Preference;
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

    @Override
    public boolean onPreferenceChange (Preference preference, Object value)
    {
        String stringValue = value.toString ();

        preference.setSummary (stringValue);

        return true;
    }

    /**
     * Attaches a listener so the summary is always updated with the preference value.
     * Also fires the listener once, to initialize the summary (so it shows up before the value
     * is changed.)
     */
    private void bindPreferenceSummaryToValue (Preference preference)
    {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener (this);

        // Trigger the listener immediately with the preference's
        // current value.
        onPreferenceChange (preference,
                PreferenceManager
                        .getDefaultSharedPreferences (preference.getContext ())
                        .getString (preference.getKey (), ""));
    }
}
