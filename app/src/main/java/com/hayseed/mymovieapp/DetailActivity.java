package com.hayseed.mymovieapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


/**
 *
 */
public class DetailActivity extends AppCompatActivity
{
    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_detail);

        if (savedInstanceState == null)
        {
            Bundle b = new Bundle ();
            Intent intent = getIntent ();
            if (intent != null)
            {
                b.putInt ("moviePos", intent.getIntExtra ("moviePos", -1));
            }

            MovieDetailFragment detailFragment = new MovieDetailFragment ();
            detailFragment.setArguments (b);

            FragmentManager fragmentManager = getFragmentManager ();
            FragmentTransaction transaction = fragmentManager.beginTransaction ();
            transaction.replace (R.id.content_frame, detailFragment);
            transaction.addToBackStack (null);
            transaction.commit ();
        }
    }
}
