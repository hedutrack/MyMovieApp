package com.hayseed.mymovieapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 *
 */
public class MovieDetailFragment extends Fragment
{
    private Integer selectedItem;

    public interface OnMovieDetailBackListener
    {
        public void OnDetailBack ();
    }

    public MovieDetailFragment ()
    {}

    @Override
    public void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setHasOptionsMenu (true);

        ((AppCompatActivity) getActivity ()).getSupportActionBar ().setDisplayOptions (android.support.v7.app.ActionBar.DISPLAY_HOME_AS_UP);
        ((AppCompatActivity) getActivity ()).getSupportActionBar ().setTitle ("Movie Details");
        ((AppCompatActivity) getActivity ()).getSupportActionBar ().setDisplayShowTitleEnabled (true);

    }

    @Nullable
    @Override
    /**
     *
     */
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate (R.layout.fragment_detail, container, false);

        Integer pos = getArguments ().getInt ("Position");
        Toast.makeText (container.getContext (), String.valueOf (pos), Toast.LENGTH_LONG).show ();

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater)
    {
        inflater.inflate (R.menu.movie_detail_menu, menu);
    }

    /**
     * Handles back button
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected (MenuItem item)
    {
        if (item.getItemId () == android.R.id.home)
        {
            OnMovieDetailBackListener listener = (OnMovieDetailBackListener) getActivity ();
            listener.OnDetailBack ();
            return true;
        }

        return super.onOptionsItemSelected (item);
    }
}
