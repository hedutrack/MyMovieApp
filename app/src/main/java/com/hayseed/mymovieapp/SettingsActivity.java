package com.hayseed.mymovieapp;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 *
 */
public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener
{
    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);

        getFragmentManager ().beginTransaction ()
                .replace (android.R.id.content, new SettingsFragment ())
                .commit ();

        bindPreferenceSummaryToValue (findPreference (getString (R.string.pref_sort_key)));

    }

    @Override
    public boolean onPreferenceChange (Preference preference, Object value)
    {
        String stringValue = value.toString ();

        preference.setSummary (stringValue);

        return true;
    }

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
