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

        bindPreferenceSummaryToValue (findPreference (getString (R.string.pref_location_key)));

    }

    @Override
    public boolean onPreferenceChange (Preference preference, Object value)
    {
        String stringValue = value.toString ();

        preference.setSummary (stringValue);

        return true;
    }


}
